package com.student.app.dao;

import com.student.app.model.StudentMajor;
import com.student.app.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStudentMajorDAO implements StudentMajorDAO {

    @Override
    public void assignMajorToStudent(StudentMajor studentMajor) {
        // First deactivate any active major for this student
        deactivateStudentMajor(studentMajor.getStudentId());

        String sql = "INSERT INTO student_majors (student_id, major_id, start_date, end_date, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, studentMajor.getStudentId());
            pstmt.setInt(2, studentMajor.getMajorId());
            pstmt.setDate(3, Date.valueOf(studentMajor.getStartDate()));
            pstmt.setDate(4, studentMajor.getEndDate() != null ? Date.valueOf(studentMajor.getEndDate()) : null);
            pstmt.setBoolean(5, studentMajor.isActive());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        studentMajor.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<StudentMajor> getActiveMajorForStudent(int studentId) {
        String sql = "SELECT sm.*, m.code AS major_code, m.name AS major_name " +
                "FROM student_majors sm " +
                "JOIN majors m ON sm.major_id = m.id " +
                "WHERE sm.student_id = ? AND sm.is_active = TRUE";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudentMajor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<StudentMajor> getMajorHistoryForStudent(int studentId) {
        List<StudentMajor> history = new ArrayList<>();
        String sql = "SELECT sm.*, m.code AS major_code, m.name AS major_name " +
                "FROM student_majors sm " +
                "JOIN majors m ON sm.major_id = m.id " +
                "WHERE sm.student_id = ? " +
                "ORDER BY sm.start_date DESC";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.add(mapResultSetToStudentMajor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    @Override
    public void updateStudentMajor(StudentMajor studentMajor) {
        String sql = "UPDATE student_majors SET major_id = ?, start_date = ?, end_date = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentMajor.getMajorId());
            pstmt.setDate(2, Date.valueOf(studentMajor.getStartDate()));
            pstmt.setDate(3, studentMajor.getEndDate() != null ? Date.valueOf(studentMajor.getEndDate()) : null);
            pstmt.setBoolean(4, studentMajor.isActive());
            pstmt.setInt(5, studentMajor.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deactivateStudentMajor(int studentId) {
        String sql = "UPDATE student_majors SET is_active = FALSE, end_date = ? WHERE student_id = ? AND is_active = TRUE";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<StudentMajor> getActiveMajorsByStudentId(int studentId) {
        List<StudentMajor> activeMajors = new ArrayList<>();
        String sql = "SELECT sm.*, m.code AS major_code, m.name AS major_name " +
                "FROM student_majors sm " +
                "JOIN majors m ON sm.major_id = m.id " +
                "WHERE sm.student_id = ? AND sm.is_active = TRUE " +
                "ORDER BY sm.start_date DESC";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    activeMajors.add(mapResultSetToStudentMajor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeMajors;
    }

    private StudentMajor mapResultSetToStudentMajor(ResultSet rs) throws SQLException {
        StudentMajor sm = new StudentMajor();
        sm.setId(rs.getInt("id"));
        sm.setStudentId(rs.getInt("student_id"));
        sm.setMajorId(rs.getInt("major_id"));
        sm.setStartDate(rs.getDate("start_date").toLocalDate());
        Date endDate = rs.getDate("end_date");
        sm.setEndDate(endDate != null ? endDate.toLocalDate() : null);
        sm.setActive(rs.getBoolean("is_active"));
        sm.setMajorCode(rs.getString("major_code"));
        sm.setMajorName(rs.getString("major_name"));
        return sm;
    }
}
