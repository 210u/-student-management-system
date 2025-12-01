# 🔧 Guide de résolution - Erreur "Column student_id not found"

## 🚨 Problème

```
org.h2.jdbc.JdbcSQLSyntaxErrorException: Colonne "student_id" non trouvée
```

Cette erreur se produit car la base de données H2 existante n'a pas les nouvelles colonnes `student_id` et `teacher_id` dans la table `users`.

---

## ✅ SOLUTION 1 : Supprimer et recréer la base de données (RECOMMANDÉ)

### Étape 1 : Arrêter l'application
Si l'application est en cours d'exécution, arrêtez-la (Ctrl+C dans le terminal).

### Étape 2 : Supprimer les fichiers de base de données

**Sur Windows (PowerShell ou CMD):**
```bash
cd C:\Projects\-student-management-system
del studentdb.*.db
```

**Sur Linux/Mac:**
```bash
cd /chemin/vers/student-management-system
rm -f studentdb.*
```

### Étape 3 : Relancer l'application
```bash
mvn clean javafx:run
```

**Résultat:** La base de données sera recréée automatiquement avec le nouveau schéma incluant `student_id` et `teacher_id`.

---

## ✅ SOLUTION 2 : Migration SQL (conservation des données)

Si vous avez déjà des données et ne voulez pas les perdre:

### Étape 1 : Accéder à la console H2

Ajoutez temporairement cette ligne dans `DatabaseManager.java` (méthode `initDatabase`):

```java
// TEMPORAIRE - pour accéder à la console H2
org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
```

### Étape 2 : Ouvrir le navigateur
- URL: http://localhost:8082
- JDBC URL: `jdbc:h2:./studentdb`
- User: `sa`
- Password: (laisser vide)

### Étape 3 : Exécuter le script de migration

Copiez et exécutez le contenu du fichier `src/main/resources/migration.sql`:

```sql
ALTER TABLE users ADD COLUMN IF NOT EXISTS student_id INT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS teacher_id INT;

CREATE TABLE IF NOT EXISTS majors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN DEFAULT TRUE
);

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

CREATE TABLE IF NOT EXISTS major_courses (
    major_id INT,
    course_code VARCHAR(20),
    PRIMARY KEY (major_id, course_code),
    FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE
);

ALTER TABLE courses ADD COLUMN IF NOT EXISTS teacher_id INT;
```

### Étape 4 : Relancer l'application

---

## ✅ SOLUTION 3 : Modification du code DatabaseManager (alternative)

Si vous voulez forcer la suppression et recréation automatique à chaque démarrage:

**Fichier:** `src/main/java/com/student/app/util/DatabaseManager.java`

Ajoutez cette méthode:

```java
public static void resetDatabase() {
    try (Connection conn = getConnection();
         var stmt = conn.createStatement()) {

        // DROP all tables
        stmt.execute("DROP TABLE IF EXISTS enrollments");
        stmt.execute("DROP TABLE IF EXISTS major_courses");
        stmt.execute("DROP TABLE IF EXISTS student_majors");
        stmt.execute("DROP TABLE IF EXISTS courses");
        stmt.execute("DROP TABLE IF EXISTS majors");
        stmt.execute("DROP TABLE IF EXISTS teachers");
        stmt.execute("DROP TABLE IF EXISTS students");
        stmt.execute("DROP TABLE IF EXISTS users");

        System.out.println("Database reset completed.");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```

Et dans `StudentService` (constructeur), appelez:

```java
public StudentService() {
    // TEMPORAIRE - pour forcer la recréation
    // DatabaseManager.resetDatabase();
    DatabaseManager.initDatabase();
    this.studentDAO = new JdbcStudentDAO();
    this.userDAO = new JdbcUserDAO();
}
```

---

## 🎯 Vérification que le problème est résolu

Après avoir appliqué une des solutions:

1. Lancez l'application:
```bash
mvn clean javafx:run
```

2. Connectez-vous avec:
- Login: `admin`
- Password: `admin123`

3. Vérifiez que:
- ✅ La connexion fonctionne
- ✅ Le bouton "Majors" est visible
- ✅ Vous pouvez créer un Major
- ✅ Vous pouvez créer un étudiant (compte User créé automatiquement)

---

## 📊 Structure de la nouvelle base de données

Après correction, votre base doit avoir:

```
TABLES:
├── users (id, username, password, role, student_id, teacher_id)  ✅ NOUVEAU SCHÉMA
├── students (id, matricule, first_name, ..., major)
├── teachers (id, employee_id, first_name, ...)
├── majors (id, code, name, description, active)                   ✅ NOUVELLE
├── student_majors (id, student_id, major_id, start_date, ...)    ✅ NOUVELLE
├── major_courses (major_id, course_code)                         ✅ NOUVELLE
├── courses (code, title, credit_hours, teacher_id)
└── enrollments (student_id, course_code, grade)
```

---

## 🆘 Si le problème persiste

1. Vérifiez que les fichiers `.db` ont bien été supprimés:
```bash
ls -la studentdb.*
# Devrait afficher "No such file or directory"
```

2. Vérifiez les logs au démarrage de l'application:
```
Database initialized successfully.
Admin user created (admin/admin123)
```

3. Si l'erreur persiste, supprimez aussi le cache Maven:
```bash
mvn clean
rm -rf target/
```

---

## ✅ RÉSOLU !

Une fois la base de données recréée, vous pourrez:
- ✅ Gérer les Majors (Parcours)
- ✅ Créer des étudiants avec création automatique de compte User
- ✅ Créer des enseignants avec création automatique de compte User
- ✅ Assigner des cours aux Majors
- ✅ Tester la connexion avec les comptes créés

---

*Document de dépannage créé le 2025-12-01*
