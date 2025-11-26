package com.student.app.controller;

import com.student.app.Main;
import com.student.app.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private AuthenticationService authService;
    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.authService = new AuthenticationService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.login(username, password)) {
            mainApp.showMainLayout();
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }
}
