package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardFormController implements Initializable {

    @FXML private AnchorPane contentArea;

    @FXML private Button btnDashboard;
    @FXML private Button btnPOS;
    @FXML private Button btnInventory;
    @FXML private Button btnSuppliers;
    @FXML private Button btnReports;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        loadUI("OverviewForm.fxml");
        setActiveButton(btnDashboard);
    }

    @FXML
    void btnPOSOnAction(ActionEvent event) {
        loadUI("POSForm.fxml");
        setActiveButton(btnPOS);
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        loadUI("AddItemForm.fxml");
        setActiveButton(btnInventory);
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        loadUI("SupplierForm.fxml");
        setActiveButton(btnSuppliers);
    }

    @FXML
    void btnReportsOnAction(ActionEvent event) {
        loadUI("ReportsForm.fxml");
        setActiveButton(btnReports);
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

    private void setActiveButton(Button activeButton) {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #a0aabf; -fx-alignment: BASELINE_LEFT; -fx-padding: 0 0 0 40; -fx-cursor: hand;";
        btnDashboard.setStyle(defaultStyle);
        btnPOS.setStyle(defaultStyle);
        btnInventory.setStyle(defaultStyle);
        btnSuppliers.setStyle(defaultStyle);
        btnReports.setStyle(defaultStyle);

        String activeStyle = "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-alignment: BASELINE_LEFT; -fx-padding: 0 0 0 40; -fx-cursor: hand;";
        activeButton.setStyle(activeStyle);
    }

    public void setRole(String role) {
        if (role.equals("Admin")) {
            loadUI("OverviewForm.fxml");
            setActiveButton(btnDashboard);

        } else if (role.equals("Staff")) {
            loadUI("POSForm.fxml");
            setActiveButton(btnPOS);

            btnDashboard.setVisible(false);
            btnDashboard.setManaged(false);

            btnReports.setVisible(false);
            btnReports.setManaged(false);

            btnSuppliers.setVisible(false);
            btnSuppliers.setManaged(false);
        }
    }
}