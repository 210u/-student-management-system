package com.student.app.dao;

import com.student.app.model.Enrollment;
import java.util.List;

public interface EnrollmentDAO {
    void enrollStudent(int studentId, String courseCode);

    List<Enrollment> getEnrollmentsByStudentId(int studentId);

    void updateGrade(int studentId, String courseCode, double grade);
}
