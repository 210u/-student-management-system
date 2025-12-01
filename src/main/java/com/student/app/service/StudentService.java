package com.student.app.service;

import com.student.app.dao.JdbcStudentDAO;
import com.student.app.dao.JdbcUserDAO;
import com.student.app.dao.StudentDAO;
import com.student.app.dao.UserDAO;
import com.student.app.model.Student;
import com.student.app.model.User;
import com.student.app.util.DatabaseManager;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing students.
 * Handles business logic and delegates to DAO.
 */
public class StudentService {

    private final StudentDAO studentDAO;
    private final UserDAO userDAO;

    public StudentService() {
        // Initialize Database
        DatabaseManager.initDatabase();
        this.studentDAO = new JdbcStudentDAO();
        this.userDAO = new JdbcUserDAO();
    }

    /**
     * Adds a new student to the database.
     * Validates that the first and last names are not empty.
     * Automatically creates a User account with login=email, password="1234", role=STUDENT
     *
     * @param student the student to add
     * @throws IllegalArgumentException if first or last name is empty
     */
    public void addStudent(Student student) {
        // Validation logic
        if (student.getFirstName().isEmpty() || student.getLastName().isEmpty()) {
            throw new IllegalArgumentException("First and Last name cannot be empty");
        }
        if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Add student to database
        studentDAO.addStudent(student);

        // Automatically create User account
        User userAccount = new User();
        userAccount.setUsername(student.getEmail()); // Login = email
        userAccount.setPassword("1234"); // Default password
        userAccount.setRole("STUDENT");
        userAccount.setStudentId(student.getId());
        userAccount.setTeacherId(null);

        userDAO.createUser(userAccount);
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
