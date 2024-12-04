package com.abrus.user.repository;

import com.abrus.user.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getList();

    Optional<User> getById(Long id);

    Optional<User> getByEmail(String email);

    User insert(User user);
}
