package com.student.app.dao;

import com.student.app.model.User;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findByUsername(String username);

    void createUser(User user);
}
