package com.student.app.model;

import javafx.beans.property.*;

/**
 * Represents a Teacher entity.
 * Uses JavaFX properties for UI binding.
 */
public class Teacher {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty employeeId = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty department = new SimpleStringProperty();
    private final StringProperty specialization = new SimpleStringProperty();

    public Teacher() {
    }

    public Teacher(int id, String employeeId, String firstName, String lastName,
                   String email, String phone, String department, String specialization) {
        this.id.set(id);
        this.employeeId.set(employeeId);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
        this.phone.set(phone);
        this.department.set(department);
        this.specialization.set(specialization);
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

    // Employee ID Property
    public String getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public StringProperty employeeIdProperty() {
        return employeeId;
    }

    // First Name Property
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    // Last Name Property
    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    // Email Property
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Phone Property
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    // Department Property
    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    // Specialization Property
    public String getSpecialization() {
        return specialization.get();
    }

    public void setSpecialization(String specialization) {
        this.specialization.set(specialization);
    }

    public StringProperty specializationProperty() {
        return specialization;
    }

    @Override
    public String toString() {
        return firstName.get() + " " + lastName.get() + " (" + employeeId.get() + ")";
    }
}
