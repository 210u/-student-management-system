package com.student.app.model;

/**
 * Represents the association between a Major and a Course.
 * Defines which courses are part of which major.
 */
public class MajorCourse {

    private int majorId;
    private String courseCode;

    // Optional: for display purposes
    private String courseTitle;
    private int creditHours;

    public MajorCourse() {
    }

    public MajorCourse(int majorId, String courseCode) {
        this.majorId = majorId;
        this.courseCode = courseCode;
    }

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }
}
