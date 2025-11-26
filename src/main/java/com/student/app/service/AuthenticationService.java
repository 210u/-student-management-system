package com.student.app.service;

import com.student.app.dao.JdbcUserDAO;
import com.student.app.dao.UserDAO;
import com.student.app.model.User;
import com.student.app.util.DatabaseManager;

import java.util.Optional;

public class AuthenticationService {

    private final UserDAO userDAO;
    private static User currentUser;

    public AuthenticationService() {
        this.userDAO = new JdbcUserDAO();
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }
}
