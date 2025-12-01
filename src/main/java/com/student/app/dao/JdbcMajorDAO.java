package com.student.app.dao;

import com.student.app.model.Major;
import com.student.app.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMajorDAO implements MajorDAO {

    @Override
    public void addMajor(Major major) {
        String sql = "INSERT INTO majors (code, name, description, active) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, major.getCode());
            pstmt.setString(2, major.getName());
            pstmt.setString(3, major.getDescription());
            pstmt.setBoolean(4, major.isActive());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        major.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Major> getMajorById(int id) {
        String sql = "SELECT * FROM majors WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMajor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Major> getAllMajors() {
        List<Major> majors = new ArrayList<>();
        String sql = "SELECT * FROM majors";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                majors.add(mapResultSetToMajor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return majors;
    }

    @Override
    public List<Major> getActiveMajors() {
        List<Major> majors = new ArrayList<>();
        String sql = "SELECT * FROM majors WHERE active = TRUE";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                majors.add(mapResultSetToMajor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return majors;
    }

    @Override
    public void updateMajor(Major major) {
        String sql = "UPDATE majors SET code = ?, name = ?, description = ?, active = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major.getCode());
            pstmt.setString(2, major.getName());
            pstmt.setString(3, major.getDescription());
            pstmt.setBoolean(4, major.isActive());
            pstmt.setInt(5, major.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteMajor(int id) {
        String sql = "DELETE FROM majors WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Major mapResultSetToMajor(ResultSet rs) throws SQLException {
        return new Major(
                rs.getInt("id"),
                rs.getString("code"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("active")
        );
    }
}
