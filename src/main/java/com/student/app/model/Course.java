package com.student.app.model;

import javafx.beans.property.*;

public class Course {
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final IntegerProperty creditHours = new SimpleIntegerProperty();

    public Course() {
    }

    public Course(String code, String title, int creditHours) {
        this.code.set(code);
        this.title.set(title);
        this.creditHours.set(creditHours);
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
}
