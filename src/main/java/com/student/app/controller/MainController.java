package com.student.app.controller;

import com.student.app.Main;
import com.student.app.model.Student;
import com.student.app.service.StudentService;
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
 * Main Controller for the application.
 */
public class MainController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Number> idColumn;
    @FXML
    private TableColumn<Student, String> matriculeColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> phoneColumn;
    @FXML
    private TableColumn<Student, String> majorColumn;
    @FXML
    private TableColumn<Student, Number> gpaColumn;
    @FXML
    private Label statusLabel;

    private StudentService studentService;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        studentService = new StudentService();

        // Initialize the student table with the columns.
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        matriculeColumn.setCellValueFactory(cellData -> cellData.getValue().matriculeProperty());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        majorColumn.setCellValueFactory(cellData -> cellData.getValue().majorProperty());
        gpaColumn.setCellValueFactory(cellData -> cellData.getValue().gpaProperty());

        // Load data
        loadStudentData();
    }

    private void loadStudentData() {
        studentData.clear();
        studentData.addAll(studentService.getAllStudents());
        studentTable.setItems(studentData);
        statusLabel.setText("Loaded " + studentData.size() + " students.");
    }

    @FXML
    private void handleRefresh() {
        loadStudentData();
    }

    @FXML
    private void handleDeleteStudent() {
        int selectedIndex = studentTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Student student = studentTable.getItems().get(selectedIndex);
            studentService.deleteStudent(student.getId());
            studentTable.getItems().remove(selectedIndex);
            statusLabel.setText("Deleted student ID: " + student.getId());
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddStudent() {
        Student tempStudent = new Student(0, "", "", "", "", "", "", "", "", 0.0, "");
        boolean okClicked = showStudentEditDialog(tempStudent);
        if (okClicked) {
            studentService.addStudent(tempStudent);
            loadStudentData();
            statusLabel.setText("Added new student.");
        }
    }

    @FXML
    private void handleEditStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            boolean okClicked = showStudentEditDialog(selectedStudent);
            if (okClicked) {
                studentService.updateStudent(selectedStudent);
                loadStudentData();
                statusLabel.setText("Updated student ID: " + selectedStudent.getId());
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Student Selected");
            alert.setContentText("Please select a student in the table.");
            alert.showAndWait();
        }
    }

    public boolean showStudentEditDialog(Student student) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/StudentForm.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // dialogStage.initOwner(primaryStage); // In a real app we'd pass the primary
            // stage
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the student into the controller.
            StudentFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setStudent(student);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
