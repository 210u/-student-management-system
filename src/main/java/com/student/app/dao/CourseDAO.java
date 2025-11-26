package com.student.app.dao;

import com.student.app.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    void addCourse(Course course);

    Optional<Course> getCourseByCode(String code);

    List<Course> getAllCourses();

    void updateCourse(Course course);

    void deleteCourse(String code);
}
