package com.student.app.controller;

import com.student.app.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalMajorsLabel;
    @FXML
    private PieChart majorPieChart;
    @FXML
    private BarChart<String, Number> gpaBarChart;

    private StudentService studentService;

    public void initialize() {
        studentService = new StudentService();
        loadStats();
    }

    private void loadStats() {
        var students = studentService.getAllStudents();

        // Total Students
        totalStudentsLabel.setText(String.valueOf(students.size()));

        // Total Majors
        long majorCount = students.stream().map(s -> s.getMajor()).distinct().count();
        totalMajorsLabel.setText(String.valueOf(majorCount));

        // Pie Chart: Students per Major
        Map<String, Long> studentsPerMajor = students.stream()
                .collect(Collectors.groupingBy(s -> s.getMajor(), Collectors.counting()));

        majorPieChart.getData().clear();
        studentsPerMajor.forEach((major, count) -> {
            majorPieChart.getData().add(new PieChart.Data(major, count));
        });

        // Bar Chart: GPA Distribution
        // Group by GPA ranges: 0-1, 1-2, 2-3, 3-4
        int[] gpaRanges = new int[4]; // 0: 0-1, 1: 1-2, 2: 2-3, 3: 3-4
        for (var s : students) {
            double gpa = s.getGpa();
            if (gpa >= 0 && gpa < 1)
                gpaRanges[0]++;
            else if (gpa >= 1 && gpa < 2)
                gpaRanges[1]++;
            else if (gpa >= 2 && gpa < 3)
                gpaRanges[2]++;
            else if (gpa >= 3 && gpa <= 4)
                gpaRanges[3]++;
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("GPA Distribution");
        series.getData().add(new XYChart.Data<>("0-1", gpaRanges[0]));
        series.getData().add(new XYChart.Data<>("1-2", gpaRanges[1]));
        series.getData().add(new XYChart.Data<>("2-3", gpaRanges[2]));
        series.getData().add(new XYChart.Data<>("3-4", gpaRanges[3]));

        gpaBarChart.getData().clear();
        gpaBarChart.getData().add(series);
    }
}
