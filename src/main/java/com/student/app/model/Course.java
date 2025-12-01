package com.student.app.model;

import javafx.beans.property.*;

public class Course {
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final IntegerProperty creditHours = new SimpleIntegerProperty();
    private final IntegerProperty teacherId = new SimpleIntegerProperty();
    private final StringProperty teacherName = new SimpleStringProperty();

    public Course() {
    }

    public Course(String code, String title, int creditHours) {
        this.code.set(code);
        this.title.set(title);
        this.creditHours.set(creditHours);
    }

    public Course(String code, String title, int creditHours, int teacherId, String teacherName) {
        this.code.set(code);
        this.title.set(title);
        this.creditHours.set(creditHours);
        this.teacherId.set(teacherId);
        this.teacherName.set(teacherName);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public int getCreditHours() {
        return creditHours.get();
    }

    public void setCreditHours(int creditHours) {
        this.creditHours.set(creditHours);
    }

    public IntegerProperty creditHoursProperty() {
        return creditHours;
    }

    public int getTeacherId() {
        return teacherId.get();
    }

    public void setTeacherId(int teacherId) {
        this.teacherId.set(teacherId);
    }

    public IntegerProperty teacherIdProperty() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName.get();
    }

    public void setTeacherName(String teacherName) {
        this.teacherName.set(teacherName);
    }

    public StringProperty teacherNameProperty() {
        return teacherName;
    }

    @Override
    public String toString() {
        return this.code + " - " + this.getTitle();
    }
}
