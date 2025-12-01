package com.student.app.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Represents the association between a Student and a Major with history.
 * A student can have multiple majors over time, but only one active.
 */
public class StudentMajor {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty studentId = new SimpleIntegerProperty();
    private final IntegerProperty majorId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
    private final BooleanProperty active = new SimpleBooleanProperty();

    // Additional fields for display
    private final StringProperty majorCode = new SimpleStringProperty();
    private final StringProperty majorName = new SimpleStringProperty();

    public StudentMajor() {
    }

    public StudentMajor(int id, int studentId, int majorId, LocalDate startDate, LocalDate endDate, boolean active) {
        this.id.set(id);
        this.studentId.set(studentId);
        this.majorId.set(majorId);
        this.startDate.set(startDate);
        this.endDate.set(endDate);
        this.active.set(active);
    }

    // ID Property
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Student ID Property
    public int getStudentId() {
        return studentId.get();
    }

    public void setStudentId(int studentId) {
        this.studentId.set(studentId);
    }

    public IntegerProperty studentIdProperty() {
        return studentId;
    }

    // Major ID Property
    public int getMajorId() {
        return majorId.get();
    }

    public void setMajorId(int majorId) {
        this.majorId.set(majorId);
    }

    public IntegerProperty majorIdProperty() {
        return majorId;
    }

    // Start Date Property
    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    // End Date Property
    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    // Active Property
    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    // Major Code (for display)
    public String getMajorCode() {
        return majorCode.get();
    }

    public void setMajorCode(String majorCode) {
        this.majorCode.set(majorCode);
    }

    public StringProperty majorCodeProperty() {
        return majorCode;
    }

    // Major Name (for display)
    public String getMajorName() {
        return majorName.get();
    }

    public void setMajorName(String majorName) {
        this.majorName.set(majorName);
    }

    public StringProperty majorNameProperty() {
        return majorName;
    }
}
