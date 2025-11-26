package com.student.app.dao;

import com.student.app.model.Course;
import com.student.app.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCourseDAO implements CourseDAO {

    @Override
    public void addCourse(Course course) {
        String sql = "INSERT INTO courses (code, title, credit_hours) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCode());
            pstmt.setString(2, course.getTitle());
            pstmt.setInt(3, course.getCreditHours());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Course> getCourseByCode(String code) {
        String sql = "SELECT * FROM courses WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Course(
                            rs.getString("code"),
                            rs.getString("title"),
                            rs.getInt("credit_hours")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("code"),
                        rs.getString("title"),
                        rs.getInt("credit_hours")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void updateCourse(Course course) {
        String sql = "UPDATE courses SET title = ?, credit_hours = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getTitle());
            pstmt.setInt(2, course.getCreditHours());
            pstmt.setString(3, course.getCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCourse(String code) {
        String sql = "DELETE FROM courses WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
