package com.student.app.model;

import javafx.beans.property.*;

/**
 * Represents a Student entity.
 * Uses JavaFX properties for UI binding.
 */
public class Student {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty matricule = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty gender = new SimpleStringProperty();
    private final StringProperty major = new SimpleStringProperty();
    private final DoubleProperty gpa = new SimpleDoubleProperty();
    private final StringProperty imagePath = new SimpleStringProperty();

    public Student() {
    }

    public Student(int id, String matricule, String firstName, String lastName, String email,
            String phone, String address, String gender, String major, double gpa, String imagePath) {
        this.id.set(id);
        this.matricule.set(matricule);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
        this.gender.set(gender);
        this.major.set(major);
        this.gpa.set(gpa);
        this.imagePath.set(imagePath);
    }

    public Student(String john, String doe, String mail, String cs, double v) {
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

    // Matricule Property
    public String getMatricule() {
        return matricule.get();
    }

    public void setMatricule(String matricule) {
        this.matricule.set(matricule);
    }

    public StringProperty matriculeProperty() {
        return matricule;
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

    // Address Property
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    // Gender Property
    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
    }

    // Major Property
    public String getMajor() {
        return major.get();
    }

    public void setMajor(String major) {
        this.major.set(major);
    }

    public StringProperty majorProperty() {
        return major;
    }

    // GPA Property
    public double getGpa() {
        return gpa.get();
    }

    public void setGpa(double gpa) {
        this.gpa.set(gpa);
    }

    public DoubleProperty gpaProperty() {
        return gpa;
    }

    // Image Path Property
    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }
}
