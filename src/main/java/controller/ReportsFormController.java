package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import model.entity.Order;
import model.entity.Product;
import model.entity.Supplier;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReportsFormController {

    @FXML
    void btnDailySalesOnAction(ActionEvent event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Order> orders = session.createQuery("FROM Order", Order.class).list();

            double dailyTotal = 0;
            int orderCount = 0;
            LocalDate today = LocalDate.now();

            for (Order order : orders) {
                LocalDate orderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (orderDate.equals(today)) {
                    dailyTotal += order.getTotal();
                    orderCount++;
                }
            }

            showAlert("Daily Sales Summary",
                    "Today's Date: " + today + "\n" +
                            "Total Orders Today: " + orderCount + "\n" +
                            "Total Revenue Today: LKR " + String.format("%.2f", dailyTotal));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate Daily Sales Report!");
        }
    }

    @FXML
    void btnMonthlyRevenueOnAction(ActionEvent event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Order> orders = session.createQuery("FROM Order", Order.class).list();

            double monthlyTotal = 0;
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();

            for (Order order : orders) {
                LocalDate orderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (orderDate.getMonthValue() == currentMonth && orderDate.getYear() == currentYear) {
                    monthlyTotal += order.getTotal();
                }
            }

            showAlert("Monthly Revenue",
                    "Month: " + LocalDate.now().getMonth() + " " + currentYear + "\n" +
                            "Total Revenue: LKR " + String.format("%.2f", monthlyTotal));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate Monthly Revenue Report!");
        }
    }

    @FXML
    void btnInventoryReportOnAction(ActionEvent event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery("FROM Product", Product.class).list();

            int totalItems = products.size();
            int totalStockQty = 0;
            double totalStockValue = 0;

            for (Product p : products) {
                totalStockQty += p.getQty();
                totalStockValue += (p.getQty() * p.getBuyingPrice());
            }

            showAlert("Inventory Status Report",
                    "Total Product Categories: " + totalItems + "\n" +
                            "Total Items in Stock: " + totalStockQty + "\n" +
                            "Estimated Stock Value: LKR " + String.format("%.2f", totalStockValue));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate Inventory Report!");
        }
    }

    @FXML
    void btnSupplierReportOnAction(ActionEvent event) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Supplier> suppliers = session.createQuery("FROM Supplier", Supplier.class).list();

            StringBuilder report = new StringBuilder("Active Suppliers:\n----------------------\n");

            for (Supplier s : suppliers) {
                report.append(s.getName()).append(" (").append(s.getCompany()).append(") - ").append(s.getContactNo()).append("\n");
            }

            showAlert("Supplier Contact List", report.toString());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate Supplier Report!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Generated");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}