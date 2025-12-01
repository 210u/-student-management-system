package com.student.app.dao;

import com.student.app.model.Major;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Major Data Access Object.
 */
public interface MajorDAO {
    void addMajor(Major major);
    Optional<Major> getMajorById(int id);
    List<Major> getAllMajors();
    List<Major> getActiveMajors();
    void updateMajor(Major major);
    void deleteMajor(int id);
}
