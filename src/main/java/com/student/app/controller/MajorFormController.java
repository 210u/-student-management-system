package com.student.app.controller;

import com.student.app.model.Course;
import com.student.app.model.Major;
import com.student.app.service.MajorService;
import com.student.app.dao.JdbcCourseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class MajorFormController {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox activeCheckBox;
    @FXML private ListView<Course> availableCoursesListView;
    @FXML private ListView<Course> selectedCoursesListView;

    private Stage dialogStage;
    private Major major;
    private boolean okClicked = false;
    private MajorService majorService;
    private JdbcCourseDAO courseDAO;

    @FXML
    private void initialize() {
        majorService = new MajorService();
        courseDAO = new JdbcCourseDAO();

        // Load all courses
        List<Course> allCourses = courseDAO.getAllCourses();
        availableCoursesListView.setItems(FXCollections.observableArrayList(allCourses));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMajor(Major major) {
        this.major = major;

        codeField.setText(major.getCode());
        nameField.setText(major.getName());
        descriptionArea.setText(major.getDescription());
        activeCheckBox.setSelected(major.isActive());

        // Load courses for this major if editing
        if (major.getId() > 0) {
            List<Course> majorCourses = majorService.getCoursesForMajor(major.getId());
            selectedCoursesListView.setItems(FXCollections.observableArrayList(majorCourses));

            // Remove already selected courses from available list
            ObservableList<Course> available = FXCollections.observableArrayList(availableCoursesListView.getItems());
            available.removeAll(majorCourses);
            availableCoursesListView.setItems(available);
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleAddCourse() {
        Course selected = availableCoursesListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedCoursesListView.getItems().add(selected);
            availableCoursesListView.getItems().remove(selected);
        }
    }

    @FXML
    private void handleRemoveCourse() {
        Course selected = selectedCoursesListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availableCoursesListView.getItems().add(selected);
            selectedCoursesListView.getItems().remove(selected);
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            major.setCode(codeField.getText());
            major.setName(nameField.getText());
            major.setDescription(descriptionArea.getText());
            major.setActive(activeCheckBox.isSelected());

            // Save major first if new
            if (major.getId() == 0) {
                majorService.addMajor(major);
            } else {
                majorService.updateMajor(major);
            }

            // Update major-course associations
            majorService.clearAllCoursesForMajor(major.getId());
            for (Course course : selectedCoursesListView.getItems()) {
                majorService.addCourseToMajor(major.getId(), course.getCode());
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (codeField.getText() == null || codeField.getText().isEmpty()) {
            errorMessage += "No valid code!\n";
        }
        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "No valid name!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
