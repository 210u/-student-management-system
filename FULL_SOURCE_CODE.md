# Code source complet pour la gestion des Majors

## IMPORTANT: Ce fichier contient TOUT le code manquant à implémenter

---

## 📁 dao/JdbcMajorDAO.java

```java
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
```

---

## 📁 dao/StudentMajorDAO.java

```java
package com.student.app.dao;

import com.student.app.model.StudentMajor;
import java.util.List;
import java.util.Optional;

public interface StudentMajorDAO {
    void assignMajorToStudent(StudentMajor studentMajor);
    Optional<StudentMajor> getActiveMajorForStudent(int studentId);
    List<StudentMajor> getMajorHistoryForStudent(int studentId);
    void updateStudentMajor(StudentMajor studentMajor);
    void deactivateStudentMajor(int studentId);
}
```

---

## 📁 dao/JdbcStudentMajorDAO.java

```java
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
```

---

## 📁 dao/MajorCourseDAO.java

```java
package com.student.app.dao;

import com.student.app.model.Course;
import java.util.List;

public interface MajorCourseDAO {
    void addCourseToMajor(int majorId, String courseCode);
    void removeCourseFromMajor(int majorId, String courseCode);
    List<Course> getCoursesForMajor(int majorId);
    void clearAllCoursesForMajor(int majorId);
}
```

---

## 📁 dao/JdbcMajorCourseDAO.java

```java
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
```

---

## 📁 service/MajorService.java

```java
package com.student.app.service;

import com.student.app.dao.JdbcMajorDAO;
import com.student.app.dao.MajorDAO;
import com.student.app.dao.MajorCourseDAO;
import com.student.app.dao.JdbcMajorCourseDAO;
import com.student.app.model.Major;
import com.student.app.model.Course;

import java.util.List;
import java.util.Optional;

public class MajorService {

    private final MajorDAO majorDAO;
    private final MajorCourseDAO majorCourseDAO;

    public MajorService() {
        this.majorDAO = new JdbcMajorDAO();
        this.majorCourseDAO = new JdbcMajorCourseDAO();
    }

    public void addMajor(Major major) {
        if (major.getName().isEmpty()) {
            throw new IllegalArgumentException("Major name cannot be empty");
        }
        majorDAO.addMajor(major);
    }

    public List<Major> getAllMajors() {
        return majorDAO.getAllMajors();
    }

    public List<Major> getActiveMajors() {
        return majorDAO.getActiveMajors();
    }

    public Optional<Major> getMajorById(int id) {
        return majorDAO.getMajorById(id);
    }

    public void updateMajor(Major major) {
        majorDAO.updateMajor(major);
    }

    public void deleteMajor(int id) {
        majorDAO.deleteMajor(id);
    }

    // Major-Course management
    public void addCourseToMajor(int majorId, String courseCode) {
        majorCourseDAO.addCourseToMajor(majorId, courseCode);
    }

    public void removeCourseFromMajor(int majorId, String courseCode) {
        majorCourseDAO.removeCourseFromMajor(majorId, courseCode);
    }

    public List<Course> getCoursesForMajor(int majorId) {
        return majorCourseDAO.getCoursesForMajor(majorId);
    }

    public void clearAllCoursesForMajor(int majorId) {
        majorCourseDAO.clearAllCoursesForMajor(majorId);
    }
}
```

---

*Fichier généré le 2025-12-01. Contient TOUS les DAOs et Services pour les Majors.*

## ⚠️ INSTRUCTIONS POUR COPIER CE CODE

1. Copiez chaque section dans le fichier correspondant
2. Vérifiez les imports
3. Compilez le projet: `mvn clean compile`
4. Les controllers et FXML seront créés dans un prochain fichier

