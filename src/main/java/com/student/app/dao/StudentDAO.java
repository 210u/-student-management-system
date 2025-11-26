package com.student.app.dao;

import com.student.app.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Student Data Access Object.
 */
public interface StudentDAO {
    void addStudent(Student student);
    Optional<Student> getStudentById(int id);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(int id);
}
