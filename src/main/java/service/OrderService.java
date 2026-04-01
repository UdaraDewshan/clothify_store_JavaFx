package service;

import model.entity.Order;
import model.entity.OrderDetail;
import repository.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepo = new OrderRepository();

    public String generateNextOrderId() {
        Order lastOrder = orderRepo.getLastOrder();
        if (lastOrder != null) {
            String lastId = lastOrder.getOrderId();
            int nextIdNum = Integer.parseInt(lastId.replace("ORD-", "")) + 1;
            return String.format("ORD-%03d", nextIdNum);
        }
        return "ORD-001";
    }

    public boolean placeOrder(Order order, List<OrderDetail> details) {
        return orderRepo.placeOrder(order, details);
    }
}