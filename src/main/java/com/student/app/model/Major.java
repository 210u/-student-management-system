package com.student.app.model;

import javafx.beans.property.*;

/**
 * Represents a Major (Academic Program/Parcours).
 * Example: Computer Science, Business Administration, etc.
 */
public class Major {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final BooleanProperty active = new SimpleBooleanProperty();

    public Major() {
    }

    public Major(int id, String code, String name, String description, boolean active) {
        this.id.set(id);
        this.code.set(code);
        this.name.set(name);
        this.description.set(description);
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

    // Code Property
    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    // Name Property
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Description Property
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
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

    @Override
    public String toString() {
        return code.get() + " - " + name.get();
    }
}
