package com.student.app.dao;

import com.student.app.model.Course;
import com.student.app.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMajorCourseDAO implements MajorCourseDAO {

    @Override
    public void addCourseToMajor(int majorId, String courseCode) {
        String sql = "INSERT IGNORE INTO major_courses (major_id, course_code) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, majorId);
            pstmt.setString(2, courseCode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCourseFromMajor(int majorId, String courseCode) {
        String sql = "DELETE FROM major_courses WHERE major_id = ? AND course_code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, majorId);
            pstmt.setString(2, courseCode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> getCoursesForMajor(int majorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, CONCAT(t.first_name, ' ', t.last_name) AS teacher_name " +
                     "FROM major_courses mc " +
                     "JOIN courses c ON mc.course_code = c.code " +
                     "LEFT JOIN teachers t ON c.teacher_id = t.id " +
                     "WHERE mc.major_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, majorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Course course = new Course();
                    course.setCode(rs.getString("code"));
                    course.setTitle(rs.getString("title"));
                    course.setCreditHours(rs.getInt("credit_hours"));
                    int teacherId = rs.getInt("teacher_id");
                    if (!rs.wasNull()) {
                        course.setTeacherId(teacherId);
                        course.setTeacherName(rs.getString("teacher_name"));
                    }
                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void clearAllCoursesForMajor(int majorId) {
        String sql = "DELETE FROM major_courses WHERE major_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, majorId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
