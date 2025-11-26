package com.student.app.service;

import com.student.app.dao.JdbcStudentDAO;
import com.student.app.dao.StudentDAO;
import com.student.app.model.Student;
import com.student.app.util.DatabaseManager;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing students.
 * Handles business logic and delegates to DAO.
 */
public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService() {
        // Initialize Database
        DatabaseManager.initDatabase();
        this.studentDAO = new JdbcStudentDAO();
    }

    /**
     * Adds a new student to the database.
     * Validates that the first and last names are not empty.
     * 
     * @param student the student to add
     * @throws IllegalArgumentException if first or last name is empty
     */
    public void addStudent(Student student) {
        // Validation logic can go here
        if (student.getFirstName().isEmpty() || student.getLastName().isEmpty()) {
            throw new IllegalArgumentException("First and Last name cannot be empty");
        }
        studentDAO.addStudent(student);
    }

    /**
     * Retrieves all students from the database.
     * 
     * @return a list of all students
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    /**
     * Retrieves a student by their ID.
     * 
     * @param id the ID of the student
     * @return an Optional containing the student if found, or empty otherwise
     */
    public Optional<Student> getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    /**
     * Updates an existing student's information.
     * 
     * @param student the student with updated information
     */
    public void updateStudent(Student student) {
        studentDAO.updateStudent(student);
    }

    /**
     * Deletes a student by their ID.
     * 
     * @param id the ID of the student to delete
     */
    public void deleteStudent(int id) {
        studentDAO.deleteStudent(id);
    }
}
