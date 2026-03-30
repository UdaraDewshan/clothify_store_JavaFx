package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardFormController {

    @FXML
    private AnchorPane contentArea;

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        // System.out.println("Dashboard Clicked");
        // loadUI("OverviewForm.fxml");
    }

    @FXML
    void btnPOSOnAction(ActionEvent event) {
        // loadUI("POSForm.fxml");
        System.out.println("POS Clicked! (UI not created yet)");
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        System.out.println("Loading Inventory...");
        loadUI("AddItemForm.fxml");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        System.out.println("Reports Clicked!");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        System.out.println("Suppliers Clicked!");
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"))));
        Stage currentStage = (Stage) contentArea.getScene().getWindow();
        currentStage.close();
        stage.show();
    }

    private void loadUI(String fileName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/" + fileName));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading form: " + fileName);
        }
    }
}