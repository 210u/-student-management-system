package com.student.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections.
 * Reads configuration from db.properties to switch between H2 and MySQL.
 */
public class DatabaseManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets a connection to the configured database.
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        String dbType = properties.getProperty("db.type");

        if ("mysql".equalsIgnoreCase(dbType)) {
            return DriverManager.getConnection(
                    properties.getProperty("db.mysql.url"),
                    properties.getProperty("db.mysql.user"),
                    properties.getProperty("db.mysql.password"));
        } else {
            // Default to H2
            return DriverManager.getConnection(
                    properties.getProperty("db.h2.url"),
                    properties.getProperty("db.h2.user"),
                    properties.getProperty("db.h2.password"));
        }
    }

    /**
     * Initializes the database tables if they don't exist.
     */
    public static void initDatabase() {
        try (Connection conn = getConnection();
                var stmt = conn.createStatement()) {

            // Users Table (extended with student_id and teacher_id)
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS users (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            username VARCHAR(50) UNIQUE NOT NULL,
                            password VARCHAR(100) NOT NULL,
                            role VARCHAR(20) NOT NULL,
                            student_id INT,
                            teacher_id INT
                        )
                    """);

            // Students Table (removed major field)
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS students (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            matricule VARCHAR(20) UNIQUE NOT NULL,
                            first_name VARCHAR(50) NOT NULL,
                            last_name VARCHAR(50) NOT NULL,
                            email VARCHAR(100) UNIQUE NOT NULL,
                            phone VARCHAR(20),
                            address VARCHAR(255),
                            gender VARCHAR(10),
                            gpa DOUBLE DEFAULT 0.0,
                            image_path VARCHAR(255)
                        )
                    """);

            // Teachers Table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS teachers (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            employee_id VARCHAR(20) UNIQUE NOT NULL,
                            first_name VARCHAR(50) NOT NULL,
                            last_name VARCHAR(50) NOT NULL,
                            email VARCHAR(100) UNIQUE NOT NULL,
                            phone VARCHAR(20),
                            department VARCHAR(50),
                            specialization VARCHAR(100)
                        )
                    """);

            // Courses Table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS courses (
                            code VARCHAR(20) PRIMARY KEY,
                            title VARCHAR(100) NOT NULL,
                            credit_hours INT NOT NULL,
                            teacher_id INT,
                            FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL
                        )
                    """);

            // Majors Table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS majors (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            code VARCHAR(20) UNIQUE NOT NULL,
                            name VARCHAR(100) NOT NULL,
                            description VARCHAR(500),
                            active BOOLEAN DEFAULT TRUE
                        )
                    """);

            // Student_Majors Table (junction with history)
            stmt.execute("""
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
                    """);

            // Major_Courses Table (junction between Major and Course)
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS major_courses (
                            major_id INT,
                            course_code VARCHAR(20),
                            PRIMARY KEY (major_id, course_code),
                            FOREIGN KEY (major_id) REFERENCES majors(id) ON DELETE CASCADE,
                            FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE
                        )
                    """);

            // Enrollments Table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS enrollments (
                            student_id INT,
                            course_code VARCHAR(20),
                            grade DOUBLE,
                            PRIMARY KEY (student_id, course_code),
                            FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                            FOREIGN KEY (course_code) REFERENCES courses(code) ON DELETE CASCADE
                        )
                    """);

            // Seed Admin User if not exists
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN')");
                System.out.println("Admin user created (admin/admin123)");
            }

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
