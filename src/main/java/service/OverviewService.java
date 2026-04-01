package service;

import model.entity.Order;
import model.entity.OrderDetail;
import repository.OrderRepository;
import repository.ProductRepository;
import org.hibernate.Session;
import util.HibernateUtil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OverviewService {

    private final OrderRepository orderRepo = new OrderRepository();
    private final ProductRepository productRepo = new ProductRepository();

    public boolean hasData() {
        List<Order> orders = orderRepo.getAll();
        return orders != null && !orders.isEmpty();
    }

    public double getTotalRevenue() {
        if (!hasData()) return 432900.0;
        List<Order> orders = orderRepo.getAll();
        return orders.stream().mapToDouble(Order::getTotal).sum();
    }

    public int getActiveOrdersToday() {
        if (!hasData()) return 34;
        List<Order> orders = orderRepo.getAll();
        int count = 0;
        LocalDate today = LocalDate.now();
        for (Order order : orders) {
            LocalDate orderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (orderDate.equals(today)) count++;
        }
        return count;
    }

    public int getTotalProductsCount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT COUNT(p) FROM Product p", Long.class).uniqueResult();
            return (count != null && count > 0) ? count.intValue() : 120; // 120 is Dummy Data
        } catch (Exception e) {
            return 120;
        }
    }

    public int getItemsSold() {
        if (!hasData()) return 136;
        int soldCount = 0;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<OrderDetail> details = session.createQuery("FROM OrderDetail", OrderDetail.class).list();
            for (OrderDetail od : details) {
                soldCount += od.getQty();
            }
        } catch (Exception e) {}
        return soldCount;
    }

    public Map<String, Double> getLast7DaysRevenue() {
        Map<String, Double> chartData = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        if (!hasData()) {
            chartData.put(today.minusDays(4).toString(), 22000.0);
            chartData.put(today.minusDays(3).toString(), 24500.0);
            chartData.put(today.minusDays(2).toString(), 9500.0);
            chartData.put(today.minusDays(1).toString(), 30000.0);
            chartData.put(today.toString(), 16500.0);
            return chartData;
        }

        List<Order> orders = orderRepo.getAll();
        for (int i = 6; i >= 0; i--) {
            chartData.put(today.minusDays(i).toString(), 0.0);
        }

        for (Order order : orders) {
            LocalDate orderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String dateStr = orderDate.toString();
            if (chartData.containsKey(dateStr)) {
                chartData.put(dateStr, chartData.get(dateStr) + order.getTotal());
            }
        }
        return chartData;
    }
}