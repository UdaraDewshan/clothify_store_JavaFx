package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.entity.Order;
import model.entity.OrderDetail;
import model.entity.Product;
import model.tm.CartTm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class POSFormController implements Initializable {

    @FXML private GridPane gridProducts;
    @FXML private Label lblDiscount;
    @FXML private Label lblNetTotal;
    @FXML private Label lblOrderId;
    @FXML private Label lblSubTotal;

    @FXML private TableView<CartTm> tblCart;
    @FXML private TableColumn<CartTm, String> colName;
    @FXML private TableColumn<CartTm, Integer> colQty;
    @FXML private TableColumn<CartTm, Double> colTotal;
    @FXML private TableColumn<CartTm, Button> colAction;

    private ObservableList<CartTm> cartList = FXCollections.observableArrayList();
    private double netTotal = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        generateNextOrderId();
        loadProductsToGrid();
    }

    private void loadProductsToGrid() {
        gridProducts.getChildren().clear();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery("FROM Product", Product.class).list();

            int column = 0;
            int row = 1;

            for (Product product : products) {
                VBox card = createProductCard(product);

                if (column == 3) {
                    column = 0;
                    row++;
                }
                gridProducts.add(card, column++, row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createProductCard(Product product) {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10));
        vBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        vBox.setPrefWidth(160);
        vBox.setPrefHeight(200);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(140);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            try {
                imageView.setImage(new Image("file:" + product.getImagePath()));
            } catch (Exception e) {
            }
        }

        Label lblName = new Label(product.getName());
        lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblPrice = new Label("LKR " + product.getSellingPrice());
        lblPrice.setStyle("-fx-text-fill: #3b82f6; -fx-font-weight: bold;");

        Button btnAdd = new Button("Add to Cart");
        btnAdd.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-cursor: hand; -fx-pref-width: 140;");

        btnAdd.setOnAction(event -> addToCart(product));

        vBox.getChildren().addAll(imageView, lblName, lblPrice, btnAdd);
        return vBox;
    }

    private void addToCart(Product product) {
        for (CartTm tm : cartList) {
            if (tm.getCode().equals(product.getProductId())) {
                tm.setQty(tm.getQty() + 1);
                tm.setTotal(tm.getQty() * tm.getUnitPrice());
                tblCart.refresh();
                calculateTotal();
                return;
            }
        }

        Button btnRemove = new Button("X");
        btnRemove.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 2 5; -fx-cursor: hand;");

        CartTm newTm = new CartTm(
                product.getProductId(),
                product.getName(),
                product.getSellingPrice(),
                1,
                product.getSellingPrice(),
                btnRemove
        );

        btnRemove.setOnAction(event -> {
            cartList.remove(newTm);
            calculateTotal();
        });

        cartList.add(newTm);
        tblCart.setItems(cartList);
        calculateTotal();
    }

    private void calculateTotal() {
        double subTotal = 0;
        for (CartTm tm : cartList) {
            subTotal += tm.getTotal();
        }

        double discount = subTotal * 0.05;
        netTotal = subTotal - discount;

        lblSubTotal.setText(String.format("LKR %.2f", subTotal));
        lblDiscount.setText(String.format("LKR %.2f", discount));
        lblNetTotal.setText(String.format("LKR %.2f", netTotal));
    }

    private void generateNextOrderId() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("FROM Order ORDER BY orderId DESC", Order.class);
            query.setMaxResults(1);
            Order lastOrder = query.uniqueResult();

            if (lastOrder != null) {
                String lastId = lastOrder.getOrderId();
                int nextIdNum = Integer.parseInt(lastId.replace("ORD-", "")) + 1;
                lblOrderId.setText(String.format("ORD-%03d", nextIdNum));
            } else {
                lblOrderId.setText("ORD-001");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblOrderId.setText("ORD-001");
        }
    }

    @FXML
    void placeOrderOnAction(ActionEvent event) {
        if (cartList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cart is empty!");
            alert.show();
            return;
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String orderId = lblOrderId.getText();
            Order order = new Order(orderId, new Date(), netTotal, "CUST-000", "Admin");
            session.save(order);

            for (CartTm tm : cartList) {
                OrderDetail detail = new OrderDetail(null, orderId, tm.getCode(), tm.getQty(), tm.getUnitPrice());
                session.save(detail);

                Product product = session.get(Product.class, tm.getCode());
                product.setQty(product.getQty() - tm.getQty());
                session.update(product);
            }

            transaction.commit();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!");
            alert.show();

            double subTotal = 0;
            for (CartTm tm : cartList) {
                subTotal += tm.getTotal();
            }
            double discount = subTotal * 0.05;

            util.PdfGenerator.generateBill(orderId, cartList, subTotal, discount, netTotal);
            
            cartList.clear();
            calculateTotal();
            generateNextOrderId();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to place order!").show();
        }
    }
}