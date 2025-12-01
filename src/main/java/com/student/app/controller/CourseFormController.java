package com.student.app.controller;

import com.student.app.model.Course;
import com.student.app.model.Teacher;
import com.student.app.service.TeacherService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CourseFormController {

    @FXML private TextField codeField;
    @FXML private TextField titleField;
    @FXML private TextField creditHoursField;
    @FXML private ComboBox<Teacher> teacherComboBox;

    private Stage dialogStage;
    private Course course;
    private boolean okClicked = false;
    private TeacherService teacherService;

    @FXML
    private void initialize() {
        teacherService = new TeacherService();
        loadTeachers();
    }

    private void loadTeachers() {
        teacherComboBox.setItems(FXCollections.observableArrayList(teacherService.getAllTeachers()));
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null) {
            codeField.setText(course.getCode());
            titleField.setText(course.getTitle());
            creditHoursField.setText(Integer.toString(course.getCreditHours()));

            // Select the current teacher if exists
            if (course.getTeacherId() > 0) {
                for (Teacher teacher : teacherComboBox.getItems()) {
                    if (teacher.getId() == course.getTeacherId()) {
                        teacherComboBox.setValue(teacher);
                        break;
                    }
                }
            }
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            course.setCode(codeField.getText());
            course.setTitle(titleField.getText());
            course.setCreditHours(Integer.parseInt(creditHoursField.getText()));

            // Set teacher if selected
            Teacher selectedTeacher = teacherComboBox.getValue();
            if (selectedTeacher != null) {
                course.setTeacherId(selectedTeacher.getId());
                course.setTeacherName(selectedTeacher.getFirstName() + " " + selectedTeacher.getLastName());
            } else {
                course.setTeacherId(0);
                course.setTeacherName("");
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
            errorMessage += "No valid course code!\n";
        }
        if (titleField.getText() == null || titleField.getText().isEmpty()) {
            errorMessage += "No valid title!\n";
        }
        if (creditHoursField.getText() == null || creditHoursField.getText().isEmpty()) {
            errorMessage += "No valid credit hours!\n";
        } else {
            try {
                Integer.parseInt(creditHoursField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid credit hours (must be an integer)!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        }
        else {
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
