package com.abrus.user.repository;

import com.abrus.user.exception.DataProcessingException;
import com.abrus.user.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private static final int MIN_AFFECTED_ROWS_COUNT = 1;
    private static final String TABLE_USER = "user";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "passw";
    private static final String PHONE = "phone";

    @Override
    public List<User> getList() {
        String sqlRequest = "SELECT " + ID + ", " + EMAIL + ", " + PHONE + " FROM " + TABLE_USER;
        List<User> users = new ArrayList<>();
        QueryHelper.fetchData(sqlRequest, stmt -> { }, rs -> {
            users.add(this.parseRow(rs));
        });
        return users;
    }

    private User parseRow(ResultSet row) {
        try {
            Long id = row.getObject(ID, Long.class);
            String email = row.getString(EMAIL);
            String phone = row.getString(PHONE);
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't parse resultSet", e);
        }
    }

    @Override
    public Optional<User> getById(Long id) {
        String sqlRequest = "SELECT " + ID + ", " + EMAIL + ", " + PHONE
                + " FROM " + TABLE_USER + " WHERE id = ?";
        AtomicReference<User> user = new AtomicReference<>();
        QueryHelper.fetchData(sqlRequest, stmt -> {
            stmt.setLong(1, id);
        }, rs -> {
            user.set(this.parseRow(rs));
        });
        return Optional.ofNullable(user.get());
    }

    @Override
    public Optional<User> getByEmail(String email) {
        String sqlRequest = "SELECT " + ID + ", " + EMAIL + ", " + PHONE
                + " FROM " + TABLE_USER + " WHERE " + EMAIL + " = ?";
        AtomicReference<User> user = new AtomicReference<>();
        QueryHelper.fetchData(sqlRequest, stmt -> {
            stmt.setString(1, email);
        }, rs -> {
            user.set(this.parseRow(rs));
        });
        return Optional.ofNullable(user.get());
    }

    @Override
    public User insert(User user) {
        String sqlRequest = "INSERT INTO " + TABLE_USER + " ("
                + EMAIL + ","
                + PASSWORD + ","
                + PHONE
                + ") VALUES (?, ?, ?)";
        AtomicReference<User> userData = new AtomicReference<>(user);
        QueryHelper.insertData(sqlRequest, stmt -> {
                User u = userData.get();
                stmt.setString(1, u.getEmail());
                stmt.setString(2, u.getPassword());
                stmt.setString(3, u.getPhoneNumber());
        }, generatedKeys -> {
            Long id = id = generatedKeys.getObject(1, Long.class);
            User u = userData.get();
            u.setId(id);
            userData.set(u);
        });
        return userData.get();
    }

}
