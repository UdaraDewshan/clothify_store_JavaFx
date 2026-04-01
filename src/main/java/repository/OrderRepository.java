package repository;

import model.entity.Order;
import model.entity.OrderDetail;
import model.entity.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.List;

public class OrderRepository {

    public Order getLastOrder() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order ORDER BY orderId DESC", Order.class);
            query.setMaxResults(1);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean placeOrder(Order order, List<OrderDetail> details) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(order);

            for (OrderDetail detail : details) {
                session.save(detail);

                Product product = session.get(Product.class, detail.getProductId());
                if (product != null) {
                    product.setQty(product.getQty() - detail.getQty());
                    session.update(product);
                }
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getAll() {
        try (Session session = util.HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Order", Order.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}