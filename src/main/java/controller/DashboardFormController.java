package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardFormController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUI("OverviewForm.fxml");
    }

    @FXML
    private AnchorPane contentArea;

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        loadUI("OverviewForm.fxml");
    }

    @FXML
    void btnPOSOnAction(ActionEvent event) {
        loadUI("POSForm.fxml");
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        loadUI("AddItemForm.fxml");
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        loadUI("ReportsForm.fxml");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        loadUI("SupplierForm.fxml");
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