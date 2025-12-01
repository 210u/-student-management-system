package com.student.app.dao;

import com.student.app.model.Teacher;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Teacher Data Access Object.
 */
public interface TeacherDAO {
    void addTeacher(Teacher teacher);
    Optional<Teacher> getTeacherById(int id);
    List<Teacher> getAllTeachers();
    void updateTeacher(Teacher teacher);
    void deleteTeacher(int id);
}
