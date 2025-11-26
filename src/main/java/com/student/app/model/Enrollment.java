package com.student.app.model;

public class Enrollment {
    private int studentId;
    private String courseCode;
    private double grade;

    // Helper fields for UI display
    private String courseTitle;
    private int creditHours;

    public Enrollment() {
    }

    public Enrollment(int studentId, String courseCode, double grade, String courseTitle, int creditHours) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.grade = grade;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
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

    @Override
    public String toString() {
        return "Enrollment{" +
                "studentId=" + studentId +
                ", courseCode='" + courseCode + '\'' +
                ", grade=" + grade +
                ", courseTitle='" + courseTitle + '\'' +
                ", creditHours=" + creditHours +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Enrollment that = (Enrollment) o;

        if (studentId != that.studentId) return false;
        if (Double.compare(that.grade, grade) != 0) return false;
        if (creditHours != that.creditHours) return false;
        if (courseCode != null ? !courseCode.equals(that.courseCode) : that.courseCode != null) return false;
        return courseTitle != null ? courseTitle.equals(that.courseTitle) : that.courseTitle == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = studentId;
        result = 31 * result + (courseCode != null ? courseCode.hashCode() : 0);
        temp = Double.doubleToLongBits(grade);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (courseTitle != null ? courseTitle.hashCode() : 0);
        result = 31 * result + creditHours;
        return result;
    }
}
