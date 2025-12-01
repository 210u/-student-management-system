package com.student.app.controller;

import com.student.app.model.Teacher;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Teacher Add/Edit Form.
 */
public class TeacherFormController {

    @FXML private TextField employeeIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField departmentField;
    @FXML private TextField specializationField;

    private Stage dialogStage;
    private Teacher teacher;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;

        employeeIdField.setText(teacher.getEmployeeId());
        firstNameField.setText(teacher.getFirstName());
        lastNameField.setText(teacher.getLastName());
        emailField.setText(teacher.getEmail());
        phoneField.setText(teacher.getPhone());
        departmentField.setText(teacher.getDepartment());
        specializationField.setText(teacher.getSpecialization());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            teacher.setEmployeeId(employeeIdField.getText());
            teacher.setFirstName(firstNameField.getText());
            teacher.setLastName(lastNameField.getText());
            teacher.setEmail(emailField.getText());
            teacher.setPhone(phoneField.getText());
            teacher.setDepartment(departmentField.getText());
            teacher.setSpecialization(specializationField.getText());

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

        if (employeeIdField.getText() == null || employeeIdField.getText().length() == 0) {
            errorMessage += "No valid employee ID!\n";
        }
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (emailField.getText() == null || emailField.getText().length() == 0) {
            errorMessage += "No valid email!\n";
        }
        if (phoneField.getText() == null || phoneField.getText().length() == 0) {
            errorMessage += "No valid phone number!\n";
        }
        if (departmentField.getText() == null || departmentField.getText().length() == 0) {
            errorMessage += "No valid department!\n";
        }
        if (specializationField.getText() == null || specializationField.getText().length() == 0) {
            errorMessage += "No valid specialization!\n";
        }

        if (errorMessage.length() == 0) {
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
