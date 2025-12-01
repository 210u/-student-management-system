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
        String sql = "INSERT INTO courses (code, title, credit_hours, teacher_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCode());
            pstmt.setString(2, course.getTitle());
            pstmt.setInt(3, course.getCreditHours());
            if (course.getTeacherId() > 0) {
                pstmt.setInt(4, course.getTeacherId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Course> getCourseByCode(String code) {
        String sql = "SELECT c.*, CONCAT(t.first_name, ' ', t.last_name) AS teacher_name " +
                     "FROM courses c LEFT JOIN teachers t ON c.teacher_id = t.id " +
                     "WHERE c.code = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCourse(rs));
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
        String sql = "SELECT c.*, CONCAT(t.first_name, ' ', t.last_name) AS teacher_name " +
                     "FROM courses c LEFT JOIN teachers t ON c.teacher_id = t.id";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void updateCourse(Course course) {
        String sql = "UPDATE courses SET title = ?, credit_hours = ?, teacher_id = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getTitle());
            pstmt.setInt(2, course.getCreditHours());
            if (course.getTeacherId() > 0) {
                pstmt.setInt(3, course.getTeacherId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, course.getCode());
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

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCode(rs.getString("code"));
        course.setTitle(rs.getString("title"));
        course.setCreditHours(rs.getInt("credit_hours"));

        int teacherId = rs.getInt("teacher_id");
        if (!rs.wasNull()) {
            course.setTeacherId(teacherId);
            String teacherName = rs.getString("teacher_name");
            if (teacherName != null) {
                course.setTeacherName(teacherName);
            }
        }
        return course;
    }
}
