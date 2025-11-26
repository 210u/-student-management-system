package com.student.app.service;

import com.student.app.model.Student;
import com.student.app.util.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() throws SQLException {
        // Reset database for testing
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS students");
        }
        
        studentService = new StudentService();
    }

    @Test
    void testAddAndGetStudent() {
        Student student = new Student("John", "Doe", "john.doe@example.com", "CS", 3.8);
        studentService.addStudent(student);

        assertTrue(student.getId() > 0, "Student ID should be generated");

        Optional<Student> retrieved = studentService.getStudentById(student.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("John", retrieved.get().getFirstName());
    }

    @Test
    void testGetAllStudents() {
        studentService.addStudent(new Student("Alice", "Smith", "alice@example.com", "Math", 3.9));
        studentService.addStudent(new Student("Bob", "Jones", "bob@example.com", "Physics", 3.5));

        List<Student> students = studentService.getAllStudents();
        assertEquals(2, students.size());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student("Charlie", "Brown", "charlie@example.com", "History", 3.2);
        studentService.addStudent(student);

        student.setMajor("Philosophy");
        studentService.updateStudent(student);

        Optional<Student> updated = studentService.getStudentById(student.getId());
        assertTrue(updated.isPresent());
        assertEquals("Philosophy", updated.get().getMajor());
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student("David", "White", "david@example.com", "Biology", 3.6);
        studentService.addStudent(student);

        studentService.deleteStudent(student.getId());

        Optional<Student> deleted = studentService.getStudentById(student.getId());
        assertFalse(deleted.isPresent());
    }
}
