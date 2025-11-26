package com.student.app.dao;

import com.student.app.model.Enrollment;
import com.student.app.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcEnrollmentDAO implements EnrollmentDAO {

    @Override
    public void enrollStudent(int studentId, String courseCode) {
        String sql = "INSERT INTO enrollments (student_id, course_code, grade) VALUES (?, ?, 0.0)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, courseCode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = """
                    SELECT e.student_id, e.course_code, e.grade, c.title, c.credit_hours
                    FROM enrollments e
                    JOIN courses c ON e.course_code = c.code
                    WHERE e.student_id = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(new Enrollment(
                            rs.getInt("student_id"),
                            rs.getString("course_code"),
                            rs.getDouble("grade"),
                            rs.getString("title"),
                            rs.getInt("credit_hours")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }

    @Override
    public void updateGrade(int studentId, String courseCode, double grade) {
        String sql = "UPDATE enrollments SET grade = ? WHERE student_id = ? AND course_code = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, grade);
            pstmt.setInt(2, studentId);
            pstmt.setString(3, courseCode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
