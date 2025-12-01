-- Migration SQL pour ajouter les colonnes student_id et teacher_id à la table users
-- À exécuter si vous voulez migrer sans supprimer les données

-- Ajouter les colonnes student_id et teacher_id si elles n'existent pas
ALTER TABLE users ADD COLUMN IF NOT EXISTS student_id INT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS teacher_id INT;

-- Supprimer la colonne 'major' de la table students si elle existe (maintenant géré via student_majors)
-- ALTER TABLE students DROP COLUMN IF EXISTS major;

-- Créer la table majors si elle n'existe pas
CREATE TABLE IF NOT EXISTS majors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN DEFAULT TRUE
);

-- Créer la table student_majors si elle n'existe pas
CREATE TABLE IF NOT EXISTS student_majors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    major_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE
);

-- Créer la table major_courses si elle n'existe pas
CREATE TABLE IF NOT EXISTS major_courses (
    major_id INT,
    course_code VARCHAR(20),
    PRIMARY KEY (major_id, course_code),
    FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE
);

-- Ajouter teacher_id à la table courses si elle n'existe pas
ALTER TABLE courses ADD COLUMN IF NOT EXISTS teacher_id INT;
ALTER TABLE courses ADD CONSTRAINT IF NOT EXISTS fk_courses_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL;

-- Vérification
SELECT 'Migration completed successfully!' AS status;
