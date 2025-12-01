package com.student.app.controller;

import com.student.app.Main;
import com.student.app.dao.CourseDAO;
import com.student.app.dao.JdbcCourseDAO;
import com.student.app.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseController {

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> codeColumn;
    @FXML
    private TableColumn<Course, String> titleColumn;
    @FXML
    private TableColumn<Course, Number> creditColumn;
    @FXML
    private TableColumn<Course, String> teacherColumn;

    private CourseDAO courseDAO;
    private ObservableList<Course> courseData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        courseDAO = new JdbcCourseDAO();

        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        creditColumn.setCellValueFactory(cellData -> cellData.getValue().creditHoursProperty());
        teacherColumn.setCellValueFactory(cellData -> cellData.getValue().teacherNameProperty());

        loadCourses();
    }

    private void loadCourses() {
        courseData.clear();
        courseData.addAll(courseDAO.getAllCourses());
        courseTable.setItems(courseData);
    }

    @FXML
    private void handleAddCourse() {
        Course tempCourse = new Course();
        boolean okClicked = showCourseEditDialog(tempCourse);
        if (okClicked) {
            courseDAO.addCourse(tempCourse);
            loadCourses();
        }
    }

    @FXML
    private void handleEditCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean okClicked = showCourseEditDialog(selected);
            if (okClicked) {
                courseDAO.updateCourse(selected);
                loadCourses();
            }
        } else {
            showAlert("No Selection", "Please select a course to edit.");
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            courseDAO.deleteCourse(selected.getCode());
            loadCourses();
        } else {
            showAlert("No Selection", "Please select a course.");
        }
    }

    private boolean showCourseEditDialog(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/CourseForm.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Course");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CourseFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCourse(course);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
