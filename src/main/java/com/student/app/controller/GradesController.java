package com.student.app.controller;

import com.student.app.dao.*;
import com.student.app.model.Course;
import com.student.app.model.Enrollment;
import com.student.app.model.Student;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

public class GradesController {

    @FXML
    private ComboBox<Student> studentComboBox;
    @FXML
    private TableView<Enrollment> enrollmentTable;
    @FXML
    private TableColumn<Enrollment, String> courseCodeColumn;
    @FXML
    private TableColumn<Enrollment, String> courseTitleColumn;
    @FXML
    private TableColumn<Enrollment, Double> gradeColumn;
    @FXML
    private ComboBox<Course> courseComboBox;

    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private ObservableList<Enrollment> enrollmentData = FXCollections.observableArrayList();
    private Student currentStudent;

    @FXML
    private void initialize() {
        enrollmentDAO = new JdbcEnrollmentDAO();
        courseDAO = new JdbcCourseDAO();
        studentDAO = new JdbcStudentDAO();

        setupTableColumns();
        loadCoursesIntoComboBox();
        loadStudentsIntoComboBox();

        studentComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                currentStudent = newSelection;
                loadEnrollments();
            }
        });
    }

    private void setupTableColumns() {
        courseCodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseCode()));
        courseTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseTitle()));
        gradeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGrade()).asObject());

        // Make grade column editable
        gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        gradeColumn.setOnEditCommit(event -> {
            Enrollment enrollment = event.getRowValue();
            enrollment.setGrade(event.getNewValue());
            enrollmentDAO.updateGrade(enrollment.getStudentId(), enrollment.getCourseCode(), event.getNewValue());
        });
    }

    private void loadStudentsIntoComboBox() {
        studentComboBox.setItems(FXCollections.observableArrayList(studentDAO.getAllStudents()));
        studentComboBox.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student != null ? student.getFirstName() + " " + student.getLastName() : "";
            }

            @Override
            public Student fromString(String string) {
                return null; // Not needed
            }
        });
    }

    private void loadCoursesIntoComboBox() {
        courseComboBox.setItems(FXCollections.observableArrayList(courseDAO.getAllCourses()));
        courseComboBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course object) {
                return object != null ? object.getCode() + " - " + object.getTitle() : "";
            }

            @Override
            public Course fromString(String string) {
                return null; // Not needed
            }
        });
    }

    private void loadEnrollments() {
        if (currentStudent != null) {
            enrollmentData.clear();
            enrollmentData.addAll(enrollmentDAO.getEnrollmentsByStudentId(currentStudent.getId()));
            enrollmentTable.setItems(enrollmentData);
        }
    }

    @FXML
    private void handleEnroll() {
        Course selectedCourse = courseComboBox.getValue();
        if (currentStudent != null && selectedCourse != null) {
            enrollmentDAO.enrollStudent(currentStudent.getId(), selectedCourse.getCode());
            loadEnrollments();
        } else {
            showAlert("Error", "Please select a student and a course.");
        }
    }

    @FXML
    private void handleSaveGrades() {
        // Grades are saved on commit, but we can add a manual save/refresh here if needed
        loadEnrollments();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
