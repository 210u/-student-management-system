package com.student.app.service;

import com.student.app.dao.JdbcStudentMajorDAO;
import com.student.app.dao.StudentMajorDAO;
import com.student.app.model.StudentMajor;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing student-major associations (with history).
 */
public class StudentMajorService {

    private final StudentMajorDAO studentMajorDAO;

    public StudentMajorService() {
        this.studentMajorDAO = new JdbcStudentMajorDAO();
    }

    public void assignMajorToStudent(StudentMajor studentMajor) {
        studentMajorDAO.assignMajorToStudent(studentMajor);
    }

    public Optional<StudentMajor> getActiveMajorForStudent(int studentId) {
        return studentMajorDAO.getActiveMajorForStudent(studentId);
    }

    public List<StudentMajor> getMajorHistoryForStudent(int studentId) {
        return studentMajorDAO.getMajorHistoryForStudent(studentId);
    }

    public void updateStudentMajor(StudentMajor studentMajor) {
        studentMajorDAO.updateStudentMajor(studentMajor);
    }

    public void deactivateStudentMajor(int studentId) {
        studentMajorDAO.deactivateStudentMajor(studentId);
    }

    public List<StudentMajor> getActiveMajorsByStudentId(int studentId) {
        return studentMajorDAO.getActiveMajorsByStudentId(studentId);
    }
}
