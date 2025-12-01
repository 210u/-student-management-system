package com.student.app.controller;

import com.student.app.Main;
import com.student.app.model.Major;
import com.student.app.service.MajorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MajorController {

    @FXML private TableView<Major> majorTable;
    @FXML private TableColumn<Major, Number> idColumn;
    @FXML private TableColumn<Major, String> codeColumn;
    @FXML private TableColumn<Major, String> nameColumn;
    @FXML private TableColumn<Major, String> descriptionColumn;
    @FXML private TableColumn<Major, Boolean> activeColumn;
    @FXML private Label statusLabel;

    private MajorService majorService;
    private ObservableList<Major> majorData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        majorService = new MajorService();

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        activeColumn.setCellValueFactory(cellData -> cellData.getValue().activeProperty());

        loadMajorData();
    }

    private void loadMajorData() {
        majorData.clear();
        majorData.addAll(majorService.getAllMajors());
        majorTable.setItems(majorData);
        statusLabel.setText("Loaded " + majorData.size() + " majors.");
    }

    @FXML
    private void handleRefresh() {
        loadMajorData();
    }

    @FXML
    private void handleAddMajor() {
        Major tempMajor = new Major();
        tempMajor.setActive(true);
        boolean okClicked = showMajorEditDialog(tempMajor);
        if (okClicked) {
            majorService.addMajor(tempMajor);
            loadMajorData();
            statusLabel.setText("Added new major.");
        }
    }

    @FXML
    private void handleEditMajor() {
        Major selectedMajor = majorTable.getSelectionModel().getSelectedItem();
        if (selectedMajor != null) {
            boolean okClicked = showMajorEditDialog(selectedMajor);
            if (okClicked) {
                majorService.updateMajor(selectedMajor);
                loadMajorData();
                statusLabel.setText("Updated major ID: " + selectedMajor.getId());
            }
        } else {
            showAlert("No Selection", "Please select a major.");
        }
    }

    @FXML
    private void handleDeleteMajor() {
        Major selectedMajor = majorTable.getSelectionModel().getSelectedItem();
        if (selectedMajor != null) {
            majorService.deleteMajor(selectedMajor.getId());
            loadMajorData();
            statusLabel.setText("Deleted major ID: " + selectedMajor.getId());
        } else {
            showAlert("No Selection", "Please select a major.");
        }
    }

    private boolean showMajorEditDialog(Major major) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/MajorForm.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Major");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MajorFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMajor(major);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
