package com.student.app.controller;

import com.student.app.Main;
import com.student.app.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private BorderPane rootPane; // This is the BorderPane itself if we inject it, but usually we inject the
                                 // center

    // We need to access the BorderPane of this controller to set the center.
    // Since the controller is instantiated by FXMLLoader, we can't easily pass the
    // root node unless we do it from Main.
    // Alternatively, we can bind the root element in FXML to a field if it has an
    // ID? No, root doesn't work like that easily.
    // Better approach: @FXML private BorderPane mainContainer; and give ID to
    // BorderPane in FXML?
    // Actually, in the FXML, the root element IS the BorderPane.
    // Let's assume we can get the scene's root or pass it.

    // Simpler: Let Main.java handle the switching, or have this controller manage
    // the center.
    // Let's give the BorderPane an fx:id="mainContainer"

    @FXML
    private BorderPane mainContainer; // Need to add fx:id="mainContainer" to BorderPane in FXML

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        showDashboard(); // Default view
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
            // We need to access the root BorderPane.
            // Since we can't easily inject the root node into the controller of the root
            // node itself via @FXML (it's null during initialize),
            // we will rely on Main.java to give us the reference or we use a hack.
            // Wait, if we add fx:id="mainContainer" to the BorderPane in FXML, it SHOULD be
            // injected.
            if (mainContainer != null) {
                mainContainer.setCenter(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
