package com.student.app.dao;

import com.student.app.model.Teacher;
import com.student.app.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Implementation of TeacherDAO.
 * Works with both H2 and MySQL.
 */
public class JdbcTeacherDAO implements TeacherDAO {

    @Override
    public void addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teachers (employee_id, first_name, last_name, email, phone, department, specialization) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, teacher.getEmployeeId());
            pstmt.setString(2, teacher.getFirstName());
            pstmt.setString(3, teacher.getLastName());
            pstmt.setString(4, teacher.getEmail());
            pstmt.setString(5, teacher.getPhone());
            pstmt.setString(6, teacher.getDepartment());
            pstmt.setString(7, teacher.getSpecialization());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        teacher.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Teacher> getTeacherById(int id) {
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTeacher(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teachers";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teachers.add(mapResultSetToTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public void updateTeacher(Teacher teacher) {
        String sql = "UPDATE teachers SET employee_id = ?, first_name = ?, last_name = ?, email = ?, phone = ?, department = ?, specialization = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getEmployeeId());
            pstmt.setString(2, teacher.getFirstName());
            pstmt.setString(3, teacher.getLastName());
            pstmt.setString(4, teacher.getEmail());
            pstmt.setString(5, teacher.getPhone());
            pstmt.setString(6, teacher.getDepartment());
            pstmt.setString(7, teacher.getSpecialization());
            pstmt.setInt(8, teacher.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTeacher(int id) {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Teacher mapResultSetToTeacher(ResultSet rs) throws SQLException {
        return new Teacher(
                rs.getInt("id"),
                rs.getString("employee_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("department"),
                rs.getString("specialization"));
    }
}
