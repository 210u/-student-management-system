package com.student.app.controller;

import com.student.app.model.Student;
import com.student.app.service.AuthenticationService;
import com.student.app.service.StudentMajorService;
import com.student.app.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentProfileController {

    @FXML
    private Label matriculeLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label gpaLabel;
    @FXML
    private ListView<String> majorsListView;
    @FXML
    private ListView<String> coursesListView;

    private StudentService studentService;
    private StudentMajorService studentMajorService;

    public void initialize() {
        studentService = new StudentService();
        studentMajorService = new StudentMajorService();
        loadStudentProfile();
    }

    private void loadStudentProfile() {
        var currentUser = AuthenticationService.getCurrentUser();
        if (currentUser == null || currentUser.getStudentId() == null) {
            System.err.println("No student ID found for current user");
            return;
        }

        // Load student data
        var studentOpt = studentService.getStudentById(currentUser.getStudentId());
        if (studentOpt.isEmpty()) {
            System.err.println("Student not found with ID: " + currentUser.getStudentId());
            return;
        }

        Student student = studentOpt.get();

        // Display personal information
        matriculeLabel.setText(student.getMatricule() != null ? student.getMatricule() : "-");
        fullNameLabel.setText(student.getFirstName() + " " + student.getLastName());
        emailLabel.setText(student.getEmail() != null ? student.getEmail() : "-");
        phoneLabel.setText(student.getPhone() != null ? student.getPhone() : "-");
        addressLabel.setText(student.getAddress() != null ? student.getAddress() : "-");
        genderLabel.setText(student.getGender() != null ? student.getGender() : "-");
        gpaLabel.setText(String.format("%.2f", student.getGpa()));

        // Load active majors
        loadActiveMajors(student.getId());

        // Load enrolled courses
        loadEnrolledCourses(student.getId());
    }

    private void loadActiveMajors(int studentId) {
        var studentMajors = studentMajorService.getActiveMajorsByStudentId(studentId);
        majorsListView.getItems().clear();

        if (studentMajors.isEmpty()) {
            majorsListView.getItems().add("No major assigned");
        } else {
            studentMajors.forEach(sm -> {
                String majorInfo = sm.getMajorName() + " (" + sm.getMajorCode() + ")";
                majorsListView.getItems().add(majorInfo);
            });
        }
    }

    private void loadEnrolledCourses(int studentId) {
        coursesListView.getItems().clear();

        try (Connection conn = com.student.app.util.DatabaseManager.getConnection()) {
            String sql = """
                        SELECT c.code, c.title, e.grade
                        FROM enrollments e
                        JOIN courses c ON e.course_code = c.code
                        WHERE e.student_id = ?
                        ORDER BY c.code
                    """;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            List<String> courses = new ArrayList<>();
            while (rs.next()) {
                String courseCode = rs.getString("code");
                String courseTitle = rs.getString("title");
                double grade = rs.getDouble("grade");

                String courseInfo = String.format("%s - %s (Grade: %.2f)", courseCode, courseTitle, grade);
                courses.add(courseInfo);
            }

            if (courses.isEmpty()) {
                coursesListView.getItems().add("No courses enrolled");
            } else {
                coursesListView.getItems().addAll(courses);
            }

        } catch (Exception e) {
            e.printStackTrace();
            coursesListView.getItems().add("Error loading courses");
        }
    }
}
