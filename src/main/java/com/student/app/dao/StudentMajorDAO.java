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

    List<StudentMajor> getActiveMajorsByStudentId(int studentId);
}
