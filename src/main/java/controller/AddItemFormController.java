package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.entity.Product;
import model.entity.Supplier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddItemFormController implements Initializable {

    @FXML private ComboBox<String> cmbCategory;
    @FXML private ComboBox<String> cmbSupplier;
    @FXML private Label lblStatus;

    @FXML private TableView<Product> tblItems;
    @FXML private TableColumn<Product, String> colCode;
    @FXML private TableColumn<Product, String> colDescription;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Integer> colQty;
    @FXML private TableColumn<Product, Double> colBuy;
    @FXML private TableColumn<Product, Double> colSell;
    @FXML private TableColumn<Product, Double> colProfit;
    @FXML private TableColumn<Product, String> colSupplier;

    @FXML private TextField txtBuying;
    @FXML private TextField txtCode;
    @FXML private TextField txtDescription;
    @FXML private TextField txtProfit;
    @FXML private TextField txtQty;
    @FXML private TextField txtSelling;
    @FXML private TextField txtSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbCategory.setItems(FXCollections.observableArrayList(
                "Men's Wear", "Women's Wear", "Kids", "Accessories", "Footwear"
        ));

        txtBuying.textProperty().addListener((obs, oldVal, newVal) -> calculateProfit());
        txtSelling.textProperty().addListener((obs, oldVal, newVal) -> calculateProfit());

        colCode.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colBuy.setCellValueFactory(new PropertyValueFactory<>("buyingPrice"));
        colSell.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        loadSuppliers();
        loadTableData();
    }

    private void calculateProfit() {
        try {
            double buying = Double.parseDouble(txtBuying.getText());
            double selling = Double.parseDouble(txtSelling.getText());
            double profit = selling - buying;
            txtProfit.setText(String.format("%.2f", profit));
        } catch (NumberFormatException e) {
            txtProfit.clear();
        }
    }

    private void loadSuppliers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Supplier> suppliers = session.createQuery("FROM Supplier", Supplier.class).list();
            ObservableList<String> supplierList = FXCollections.observableArrayList();

            for (Supplier s : suppliers) {
                supplierList.add(s.getSupplierId() + " - " + s.getName());
            }
            cmbSupplier.setItems(supplierList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createQuery("FROM Product", Product.class).list();
            ObservableList<Product> productList = FXCollections.observableArrayList(products);
            tblItems.setItems(productList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addItem(ActionEvent event) {
        try {
            String id = txtCode.getText();
            String name = txtDescription.getText();
            String category = cmbCategory.getValue();
            String size = txtSize.getText();
            int qty = Integer.parseInt(txtQty.getText());
            double buyingPrice = Double.parseDouble(txtBuying.getText());
            double sellingPrice = Double.parseDouble(txtSelling.getText());
            double profit = Double.parseDouble(txtProfit.getText());

            String supplier = cmbSupplier.getValue();
            String supplierId = (supplier != null) ? supplier.split(" - ")[0] : "Unknown";

            if (id.isEmpty() || name.isEmpty() || category == null) {
                lblStatus.setTextFill(Color.RED);
                lblStatus.setText("Please fill all required fields!");
                return;
            }

            Product product = new Product(id, name, category, size, buyingPrice, sellingPrice, profit, qty, supplierId, null);

            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.save(product);
                transaction.commit();

                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Product Added Successfully!");

                loadTableData();
                clearFields(null);
            }

        } catch (NumberFormatException e) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Number Format! Check Prices & Qty.");
        } catch (Exception e) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Error saving product!");
            e.printStackTrace();
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        txtCode.clear();
        txtDescription.clear();
        txtQty.clear();
        txtBuying.clear();
        txtSelling.clear();
        txtSize.clear();
        txtProfit.clear();
        cmbCategory.getSelectionModel().clearSelection();
        cmbSupplier.getSelectionModel().clearSelection();


        if(event != null) lblStatus.setText("");
    }
}