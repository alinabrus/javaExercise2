package com.abrus.user.repository;

import com.abrus.user.exception.DataProcessingException;
import com.abrus.util.db.ConnectionUtil;

import java.sql.*;

public class QueryHelper {

    private static final int MIN_AFFECTED_ROWS_COUNT = 1;

    public static void fetchData(String sqlRequest,
                                 ThrowingConsumer<PreparedStatement, SQLException> paramSetter,
                                 ThrowingConsumer<ResultSet, SQLException> rowHandler
    ) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlRequest)
        ) {
            paramSetter.accept(stmt);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rowHandler.accept(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't execute sql request: ", e);
        }
    }

    public static void insertData(String sqlRequest,
                                  ThrowingConsumer<PreparedStatement, SQLException> paramSetter,
                                  ThrowingConsumer<ResultSet, SQLException> rowHandler
    ) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlRequest, Statement.RETURN_GENERATED_KEYS)
        ) {
            paramSetter.accept(stmt);

            int affectedRows = stmt.executeUpdate();
            checkAffectedRowsNumber(affectedRows);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                while (generatedKeys.next()) {
                    rowHandler.accept(generatedKeys);
                }
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't execute sql request: ", e);
        }
    }

    private static void checkAffectedRowsNumber(int number) {
        if (number < MIN_AFFECTED_ROWS_COUNT) {
            throw new DataProcessingException("Excepted to change at least 1 row, "
                    + "but changed 0 rows.");
        }
    }
}
