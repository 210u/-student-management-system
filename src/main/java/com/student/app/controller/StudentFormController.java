package com.student.app.controller;

import com.student.app.model.Major;
import com.student.app.model.Student;
import com.student.app.service.MajorService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controller for the Student Add/Edit Form.
 */
public class StudentFormController {

    @FXML private TextField matriculeField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<Major> majorComboBox;

    private Stage dialogStage;
    private Student student;
    private boolean okClicked = false;
    private MajorService majorService;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        majorService = new MajorService();

        // Load active majors into ComboBox
        majorComboBox.getItems().setAll(majorService.getActiveMajors());

        // Set custom display for ComboBox items
        majorComboBox.setConverter(new StringConverter<Major>() {
            @Override
            public String toString(Major major) {
                return major != null ? major.getCode() + " - " + major.getName() : "";
            }

            @Override
            public Major fromString(String string) {
                return null; // Not needed for our use case
            }
        });
    }

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

        // Select the major in ComboBox if editing an existing student
        if (student.getMajor() != null && !student.getMajor().isEmpty()) {
            for (Major major : majorComboBox.getItems()) {
                if (major.getCode().equals(student.getMajor()) || major.getName().equals(student.getMajor())) {
                    majorComboBox.getSelectionModel().select(major);
                    break;
                }
            }
        }
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

            // Get selected major from ComboBox
            Major selectedMajor = majorComboBox.getSelectionModel().getSelectedItem();
            if (selectedMajor != null) {
                student.setMajor(selectedMajor.getCode());
            }

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
        if (majorComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "No major selected!\n";
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

