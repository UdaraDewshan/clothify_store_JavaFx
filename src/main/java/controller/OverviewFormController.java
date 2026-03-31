package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.hibernate.Session;
import util.HibernateUtil;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class OverviewFormController implements Initializable {

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
        loadTotalProductsCount();

        loadChartData();
    }

    private void loadTotalProductsCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class).uniqueResult();
            lblTotalProducts.setText(count != null ? count.toString() : "0");
        } catch (Exception e) {
            e.printStackTrace();
            lblTotalProducts.setText("0");
        }
    }

    private void loadChartData() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weekly Revenue");

        series.getData().add(new XYChart.Data<>("Jan 10", 22000));
        series.getData().add(new XYChart.Data<>("Jan 11", 24500));
        series.getData().add(new XYChart.Data<>("Jan 12", 9500));
        series.getData().add(new XYChart.Data<>("Jan 13", 30000));
        series.getData().add(new XYChart.Data<>("Jan 14", 16500));

        revenueChart.getData().add(series);
    }
}