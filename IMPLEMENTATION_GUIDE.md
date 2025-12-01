# Guide d'implémentation - Système de gestion des Majors (Parcours)

## 📋 Vue d'ensemble

Ce document décrit l'implémentation complète du système de gestion des **Majors (Parcours)** avec:
- ✅ Modèles créés (Major, StudentMajor, MajorCourse)
- ✅ Student modifié (champ "major" retiré)
- ✅ User modifié (student_id, teacher_id ajoutés + rôle STUDENT)
- ✅ Schéma DB mis à jour (nouvelles tables)

---

## 🗄️ Schéma de base de données

### Nouvelles tables créées:

```sql
-- Table Majors
CREATE TABLE IF NOT EXISTS majors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN DEFAULT TRUE
)

-- Table Student_Majors (historique des parcours)
CREATE TABLE IF NOT EXISTS student_majors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    major_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE
)

-- Table Major_Courses (cours liés à un major)
CREATE TABLE IF NOT EXISTS major_courses (
    major_id INT,
    course_code VARCHAR(20),
    PRIMARY KEY (major_id, course_code),
    FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE
)

-- Table Users modifiée
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,  -- ADMIN, LECTURER, STUDENT
    student_id INT,
    teacher_id INT
)

-- Table Students modifiée
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    gender VARCHAR(10),
    -- major VARCHAR(50),  <-- SUPPRIMÉ
    gpa DOUBLE DEFAULT 0.0,
    image_path VARCHAR(255)
)
```

---

## 📁 Fichiers à créer (DAOs)

### 1. MajorDAO.java
```java
package com.student.app.dao;

import com.student.app.model.Major;
import java.util.List;
import java.util.Optional;

public interface MajorDAO {
    void addMajor(Major major);
    Optional<Major> getMajorById(int id);
    List<Major> getAllMajors();
    List<Major> getActiveMajors();
    void updateMajor(Major major);
    void deleteMajor(int id);
}
```

### 2. Jdbc MajorDAO.java (implémentation complète dans les prochains fichiers)

### 3. StudentMajorDAO.java
```java
package com.student.app.dao;

import com.student.app.model.StudentMajor;
import java.util.List;
import java.util.Optional;

public interface StudentMajorDAO {
    void assignMajorToStudent(StudentMajor studentMajor);
    Optional<StudentMajor> getActiveMajorForStudent(int studentId);
    List<StudentMajor> getMajorHistoryForStudent(int studentId);
    void updateStudentMajor(StudentMajor studentMajor);
    void deactivateStudentMajor(int studentId);
}
```

### 4. MajorCourseDAO.java
```java
package com.student.app.dao;

import com.student.app.model.Course;
import java.util.List;

public interface MajorCourseDAO {
    void addCourseToMajor(int majorId, String courseCode);
    void removeCourseFromMajor(int majorId, String courseCode);
    List<Course> getCoursesForMajor(int majorId);
    void clearAllCoursesForMajor(int majorId);
}
```

---

## 🔧 Fichiers à modifier

### JdbcStudentDAO.java
**Modification:** Retirer le champ "major" des requêtes SQL

**Avant:**
```java
String sql = "INSERT INTO students (matricule, first_name, last_name, email, phone, address, gender, major, gpa, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
```

**Après:**
```java
String sql = "INSERT INTO students (matricule, first_name, last_name, email, phone, address, gender, gpa, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
```

Idem pour UPDATE et mapResultSetToStudent().

### JdbcUserDAO.java
**Modification:** Ajouter gestion de student_id et teacher_id

```java
private User mapResultSetToUser(ResultSet rs) throws SQLException {
    return new User(
        rs.getInt("id"),
        rs.getString("username"),
        rs.getString("password"),
        rs.getString("role"),
        (Integer) rs.getObject("student_id"),
        (Integer) rs.getObject("teacher_id")
    );
}
```

---

## 🎯 Fonctionnalités à implémenter

### A. Gestion des Majors (Interface Admin)
- CRUD complet des Majors
- Assignation de cours à un Major
- Activation/désactivation de Majors

### B. Inscription d'un étudiant
1. Saisir infos personnelles
2. Choisir le Major actif (parmi actifs)
3. Afficher automatiquement les cours du Major
4. Permettre inscription aux cours
5. Créer automatiquement un compte User (login=email, password="1234", role=STUDENT, student_id=X)

### C. Espace étudiant
- Afficher infos personnelles
- Afficher Major actif
- Afficher cours inscrits
- Afficher notes

### D. Vue détail étudiant (Admin)
- Toutes les infos
- Historique des Majors
- Cours inscrits + notes
- Enseignants liés aux cours

---

## 📝 Prochaines étapes

1. ✅ Créer tous les DAOs (Major, StudentMajor, MajorCourse) avec implémentations JDBC
2. ✅ Mettre à jour JdbcStudentDAO et JdbcUserDAO
3. ✅ Créer MajorService
4. ✅ Créer MajorController + MajorView.fxml
5. ✅ Créer MajorFormController + MajorForm.fxml (avec gestion des cours)
6. ✅ Refaire StudentFormController pour gérer Majors + inscriptions automatiques
7. ✅ Créer StudentDetailController + StudentDetailView.fxml
8. ✅ Créer StudentDashboardController + StudentDashboardView.fxml (espace étudiant)
9. ✅ Modifier AuthenticationService pour créer comptes automatiquement
10. ✅ Modifier LoginController pour redirection par rôle
11. ✅ Ajouter bouton "Majors" dans MainLayout
12. ✅ Tester end-to-end

---

## 🚨 Points d'attention

- **Un seul Major actif** par étudiant à la fois
- **Historique complet** : date début/fin pour chaque Major
- **Création automatique de comptes** : email = username, password = "1234"
- **Rôles**: ADMIN, LECTURER, STUDENT
- **Espace personnalisé** selon le rôle connecté

---

*Document généré le 2025-12-01*
