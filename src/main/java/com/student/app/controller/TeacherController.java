package com.student.app.controller;

import com.student.app.Main;
import com.student.app.model.Teacher;
import com.student.app.service.TeacherService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for managing teachers.
 */
public class TeacherController {

    @FXML
    private TableView<Teacher> teacherTable;
    @FXML
    private TableColumn<Teacher, Number> idColumn;
    @FXML
    private TableColumn<Teacher, String> employeeIdColumn;
    @FXML
    private TableColumn<Teacher, String> firstNameColumn;
    @FXML
    private TableColumn<Teacher, String> lastNameColumn;
    @FXML
    private TableColumn<Teacher, String> emailColumn;
    @FXML
    private TableColumn<Teacher, String> phoneColumn;
    @FXML
    private TableColumn<Teacher, String> departmentColumn;
    @FXML
    private TableColumn<Teacher, String> specializationColumn;
    @FXML
    private Label statusLabel;

    private TeacherService teacherService;
    private ObservableList<Teacher> teacherData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        teacherService = new TeacherService();

        // Initialize the teacher table with the columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        employeeIdColumn.setCellValueFactory(cellData -> cellData.getValue().employeeIdProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        specializationColumn.setCellValueFactory(cellData -> cellData.getValue().specializationProperty());

        // Load data
        loadTeacherData();
    }

    private void loadTeacherData() {
        teacherData.clear();
        teacherData.addAll(teacherService.getAllTeachers());
        teacherTable.setItems(teacherData);
        statusLabel.setText("Loaded " + teacherData.size() + " teachers.");
    }

    @FXML
    private void handleRefresh() {
        loadTeacherData();
    }

    @FXML
    private void handleDeleteTeacher() {
        int selectedIndex = teacherTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Teacher teacher = teacherTable.getItems().get(selectedIndex);
            teacherService.deleteTeacher(teacher.getId());
            teacherTable.getItems().remove(selectedIndex);
            statusLabel.setText("Deleted teacher ID: " + teacher.getId());
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Teacher Selected");
            alert.setContentText("Please select a teacher in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddTeacher() {
        Teacher tempTeacher = new Teacher(0, "", "", "", "", "", "", "");
        boolean okClicked = showTeacherEditDialog(tempTeacher);
        if (okClicked) {
            teacherService.addTeacher(tempTeacher);
            loadTeacherData();
            statusLabel.setText("Added new teacher.");
        }
    }

    @FXML
    private void handleEditTeacher() {
        Teacher selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
        if (selectedTeacher != null) {
            boolean okClicked = showTeacherEditDialog(selectedTeacher);
            if (okClicked) {
                teacherService.updateTeacher(selectedTeacher);
                loadTeacherData();
                statusLabel.setText("Updated teacher ID: " + selectedTeacher.getId());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Teacher Selected");
            alert.setContentText("Please select a teacher in the table.");
            alert.showAndWait();
        }
    }

    private boolean showTeacherEditDialog(Teacher teacher) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/TeacherForm.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Teacher");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TeacherFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTeacher(teacher);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
