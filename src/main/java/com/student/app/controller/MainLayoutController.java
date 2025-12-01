package com.student.app.controller;

import com.student.app.Main;
import com.student.app.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private BorderPane mainContainer;

    // Sidebar buttons
    @FXML
    private Button dashboardButton;
    @FXML
    private Button studentsButton;
    @FXML
    private Button teachersButton;
    @FXML
    private Button majorsButton;
    @FXML
    private Button coursesButton;
    @FXML
    private Button gradesButton;
    @FXML
    private Button settingsButton;

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        configureUIForRole();
        showDefaultView();
    }

    private void configureUIForRole() {
        // Hide all admin menus for students
        if (AuthenticationService.isStudent()) {
            dashboardButton.setVisible(false);
            dashboardButton.setManaged(false);
            studentsButton.setVisible(false);
            studentsButton.setManaged(false);
            teachersButton.setVisible(false);
            teachersButton.setManaged(false);
            majorsButton.setVisible(false);
            majorsButton.setManaged(false);
            coursesButton.setVisible(false);
            coursesButton.setManaged(false);
            gradesButton.setVisible(false);
            gradesButton.setManaged(false);
            settingsButton.setVisible(false);
            settingsButton.setManaged(false);
        }
    }

    private void showDefaultView() {
        if (AuthenticationService.isStudent()) {
            showStudentProfile();
        } else {
            showDashboard();
        }
    }

    @FXML
    private void showDashboard() {
        loadView("/fxml/DashboardView.fxml");
    }

    @FXML
    private void showStudents() {
        loadView("/fxml/MainView.fxml");
    }

    @FXML
    private void showTeachers() {
        loadView("/fxml/TeacherView.fxml");
    }

    @FXML
    private void showMajors() {
        loadView("/fxml/MajorView.fxml");
    }

    @FXML
    private void showCourses() {
        loadView("/fxml/CourseView.fxml");
    }

    @FXML
    private void showGrades() {
        loadView("/fxml/GradesView.fxml");
    }

    @FXML
    private void showSettings() {
        // Placeholder for settings
        System.out.println("Show Settings");
    }

    private void showStudentProfile() {
        loadView("/fxml/StudentProfileView.fxml");
    }

    @FXML
    private void handleLogout() {
        AuthenticationService.logout();
        mainApp.showLogin();
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            if (mainContainer != null) {
                mainContainer.setCenter(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
