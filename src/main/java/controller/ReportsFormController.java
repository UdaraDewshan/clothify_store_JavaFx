package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import service.ReportService;

public class ReportsFormController {

    private final ReportService reportService = new ReportService();

    @FXML
    void btnDailySalesOnAction(ActionEvent event) {
        String summary = reportService.getDailySalesSummary();
        showAlert("Daily Sales Summary", summary);
    }

    @FXML
    void btnMonthlyRevenueOnAction(ActionEvent event) {
        String summary = reportService.getMonthlyRevenueSummary();
        showAlert("Monthly Revenue", summary);
    }

    @FXML
    void btnInventoryReportOnAction(ActionEvent event) {
        String summary = reportService.getInventoryStatusReport();
        showAlert("Inventory Status Report", summary);
    }

    @FXML
    void btnSupplierReportOnAction(ActionEvent event) {
        String summary = reportService.getSupplierContactList();
        showAlert("Supplier Contact List", summary);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Generated");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}