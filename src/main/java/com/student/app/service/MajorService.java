package com.student.app.service;

import com.student.app.dao.JdbcMajorDAO;
import com.student.app.dao.MajorDAO;
import com.student.app.dao.MajorCourseDAO;
import com.student.app.dao.JdbcMajorCourseDAO;
import com.student.app.model.Major;
import com.student.app.model.Course;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing majors (academic programs/parcours).
 */
public class MajorService {

    private final MajorDAO majorDAO;
    private final MajorCourseDAO majorCourseDAO;

    public MajorService() {
        this.majorDAO = new JdbcMajorDAO();
        this.majorCourseDAO = new JdbcMajorCourseDAO();
    }

    public void addMajor(Major major) {
        if (major.getName() == null || major.getName().isEmpty()) {
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
