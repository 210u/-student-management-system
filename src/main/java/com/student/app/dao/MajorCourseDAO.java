package com.student.app.dao;

import com.student.app.model.Course;
import java.util.List;

public interface MajorCourseDAO {
    void addCourseToMajor(int majorId, String courseCode);
    void removeCourseFromMajor(int majorId, String courseCode);
    List<Course> getCoursesForMajor(int majorId);
    void clearAllCoursesForMajor(int majorId);
}
