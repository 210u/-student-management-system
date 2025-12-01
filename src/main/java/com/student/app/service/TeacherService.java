package com.student.app.service;

import com.student.app.dao.JdbcTeacherDAO;
import com.student.app.dao.JdbcUserDAO;
import com.student.app.dao.TeacherDAO;
import com.student.app.dao.UserDAO;
import com.student.app.model.Teacher;
import com.student.app.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing teachers.
 * Handles business logic and delegates to DAO.
 */
public class TeacherService {

    private final TeacherDAO teacherDAO;
    private final UserDAO userDAO;

    public TeacherService() {
        this.teacherDAO = new JdbcTeacherDAO();
        this.userDAO = new JdbcUserDAO();
    }

    /**
     * Adds a new teacher to the database.
     * Validates that the first and last names are not empty.
     * Automatically creates a User account with login=email, password="1234", role=LECTURER
     *
     * @param teacher the teacher to add
     * @throws IllegalArgumentException if first or last name is empty
     */
    public void addTeacher(Teacher teacher) {
        if (teacher.getFirstName().isEmpty() || teacher.getLastName().isEmpty()) {
            throw new IllegalArgumentException("First and Last name cannot be empty");
        }
        if (teacher.getEmail() == null || teacher.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Add teacher to database
        teacherDAO.addTeacher(teacher);

        // Automatically create User account
        User userAccount = new User();
        userAccount.setUsername(teacher.getEmail()); // Login = email
        userAccount.setPassword("1234"); // Default password
        userAccount.setRole("LECTURER");
        userAccount.setStudentId(null);
        userAccount.setTeacherId(teacher.getId());

        userDAO.createUser(userAccount);
    }

    /**
     * Retrieves all teachers from the database.
     *
     * @return a list of all teachers
     */
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
    }

    /**
     * Retrieves a teacher by their ID.
     *
     * @param id the ID of the teacher
     * @return an Optional containing the teacher if found, or empty otherwise
     */
    public Optional<Teacher> getTeacherById(int id) {
        return teacherDAO.getTeacherById(id);
    }

    /**
     * Updates an existing teacher's information.
     *
     * @param teacher the teacher with updated information
     */
    public void updateTeacher(Teacher teacher) {
        teacherDAO.updateTeacher(teacher);
    }

    /**
     * Deletes a teacher by their ID.
     *
     * @param id the ID of the teacher to delete
     */
    public void deleteTeacher(int id) {
        teacherDAO.deleteTeacher(id);
    }
}
