package service;

import model.entity.Order;
import model.entity.Product;
import model.entity.Supplier;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.SupplierRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReportService {

    private final OrderRepository orderRepo = new OrderRepository();
    private final ProductRepository productRepo = new ProductRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();

    public String getDailySalesSummary() {
        List<Order> orders = orderRepo.getAll();
        if (orders == null) return "Failed to retrieve data!";

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

        return "Today's Date: " + today + "\n" +
                "Total Orders Today: " + orderCount + "\n" +
                "Total Revenue Today: LKR " + String.format("%.2f", dailyTotal);
    }

    public String getMonthlyRevenueSummary() {
        List<Order> orders = orderRepo.getAll();
        if (orders == null) return "Failed to retrieve data!";

        double monthlyTotal = 0;
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        for (Order order : orders) {
            LocalDate orderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (orderDate.getMonthValue() == currentMonth && orderDate.getYear() == currentYear) {
                monthlyTotal += order.getTotal();
            }
        }

        return "Month: " + LocalDate.now().getMonth() + " " + currentYear + "\n" +
                "Total Revenue: LKR " + String.format("%.2f", monthlyTotal);
    }

    public String getInventoryStatusReport() {
        List<Product> products = productRepo.getAll();
        if (products == null) return "Failed to retrieve data!";

        int totalItems = products.size();
        int totalStockQty = 0;
        double totalStockValue = 0;

        for (Product p : products) {
            totalStockQty += p.getQty();
            totalStockValue += (p.getQty() * p.getBuyingPrice());
        }

        return "Total Product Categories: " + totalItems + "\n" +
                "Total Items in Stock: " + totalStockQty + "\n" +
                "Estimated Stock Value: LKR " + String.format("%.2f", totalStockValue);
    }

    public String getSupplierContactList() {
        List<Supplier> suppliers = supplierRepo.getAll();
        if (suppliers == null) return "Failed to retrieve data!";

        StringBuilder report = new StringBuilder("Active Suppliers:\n----------------------\n");

        for (Supplier s : suppliers) {
            report.append(s.getName()).append(" (").append(s.getCompany()).append(") - ").append(s.getContactNo()).append("\n");
        }

        return report.toString();
    }
}