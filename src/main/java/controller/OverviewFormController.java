package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import service.OverviewService;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

public class OverviewFormController implements Initializable {

    private final OverviewService overviewService = new OverviewService();

    @FXML private Label lblActiveOrders;
    @FXML private Label lblDate;
    @FXML private Label lblItemsSold;
    @FXML private Label lblTotalProducts;
    @FXML private Label lblTotalRevenue;

    @FXML private BarChart<String, Number> revenueChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy | hh:mm a");
        lblDate.setText(formatter.format(new Date()));

        loadSummaryCards();
        loadChartData();
    }

    private void loadSummaryCards() {
        lblTotalRevenue.setText(String.format("LKR %.2f", overviewService.getTotalRevenue()));
        lblActiveOrders.setText(String.valueOf(overviewService.getActiveOrdersToday()));
        lblTotalProducts.setText(String.valueOf(overviewService.getTotalProductsCount()));
        lblItemsSold.setText(String.valueOf(overviewService.getItemsSold()));
    }

    private void loadChartData() {
        revenueChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue Trends");
        Map<String, Double> chartData = overviewService.getLast7DaysRevenue();

        for (Map.Entry<String, Double> entry : chartData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        revenueChart.getData().add(series);
    }
}