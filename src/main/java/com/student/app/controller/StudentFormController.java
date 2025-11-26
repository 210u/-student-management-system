package com.student.app.controller;

import com.student.app.model.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Student Add/Edit Form.
 */
public class StudentFormController {

    @FXML private TextField matriculeField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField majorField;
    @FXML private TextField gpaField;

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;

    /**
     * Sets the stage of this dialog.
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the student to be edited in the dialog.
     * @param student
     */
    public void setStudent(Student student) {
        this.student = student;

        matriculeField.setText(student.getMatricule());
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        emailField.setText(student.getEmail());
        phoneField.setText(student.getPhone());
        majorField.setText(student.getMajor());
        gpaField.setText(Double.toString(student.getGpa()));
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            student.setMatricule(matriculeField.getText());
            student.setFirstName(firstNameField.getText());
            student.setLastName(lastNameField.getText());
            student.setEmail(emailField.getText());
            student.setPhone(phoneField.getText());
            student.setMajor(majorField.getText());
            student.setGpa(Double.parseDouble(gpaField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (matriculeField.getText() == null || matriculeField.getText().length() == 0) {
            errorMessage += "No valid matricule!\n";
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
        if (majorField.getText() == null || majorField.getText().length() == 0) {
            errorMessage += "No valid major!\n";
        }

        if (gpaField.getText() == null || gpaField.getText().length() == 0) {
            errorMessage += "No valid GPA!\n";
        } else {
            // try to parse the GPA into a double
            try {
                Double.parseDouble(gpaField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid GPA (must be a number)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
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

