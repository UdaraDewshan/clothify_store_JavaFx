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
import service.OrderService;
import service.ProductService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class POSFormController implements Initializable {

    private final ProductService productService = new ProductService();
    private final OrderService orderService = new OrderService();

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

        lblOrderId.setText(orderService.generateNextOrderId());
        loadProductsToGrid();
    }

    private void loadProductsToGrid() {
        gridProducts.getChildren().clear();

        List<Product> products = productService.getAllProducts();

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

    @FXML
    void placeOrderOnAction(ActionEvent event) {
        if (cartList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Cart is empty!");
            alert.show();
            return;
        }

        String orderId = lblOrderId.getText();

        Order order = new Order(orderId, new Date(), netTotal, "CUST-000", "Admin");

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartTm tm : cartList) {
            orderDetails.add(new OrderDetail(null, orderId, tm.getCode(), tm.getQty(), tm.getUnitPrice()));
        }

        boolean isPlaced = orderService.placeOrder(order, orderDetails);

        if (isPlaced) {
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
            lblOrderId.setText(orderService.generateNextOrderId());

            loadProductsToGrid();

        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to place order!").show();
        }
    }
}