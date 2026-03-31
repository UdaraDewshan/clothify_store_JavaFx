package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.entity.Product;
import model.entity.Supplier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @FXML private ImageView imgProduct;
    private String selectedImagePath = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextProductId();

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

    @FXML
    void chooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path destinationPath = Paths.get("saved_images", fileName);

                Files.createDirectories(destinationPath.getParent());

                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                selectedImagePath = destinationPath.toString();
                Image image = new Image("file:" + selectedImagePath);
                imgProduct.setImage(image);

            } catch (IOException e) {
                e.printStackTrace();
                lblStatus.setTextFill(Color.RED);
                lblStatus.setText("Failed to save image!");
            }
        }
    }

    private void generateNextProductId() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product ORDER BY productId DESC", Product.class);
            query.setMaxResults(1);
            Product lastProduct = query.uniqueResult();

            if (lastProduct != null) {
                String lastId = lastProduct.getProductId();
                int nextIdNum = Integer.parseInt(lastId.replace("P", "")) + 1;
                txtCode.setText(String.format("P%03d", nextIdNum));
            } else {
                txtCode.setText("P001");
            }
        } catch (Exception e) {
            e.printStackTrace();
            txtCode.setText("P001");
        }
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

            Product product = new Product(id, name, category, size, buyingPrice, sellingPrice, profit, qty, supplierId, selectedImagePath);

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
        txtDescription.clear();
        txtQty.clear();
        txtBuying.clear();
        txtSelling.clear();
        txtSize.clear();
        txtProfit.clear();
        cmbCategory.getSelectionModel().clearSelection();
        cmbSupplier.getSelectionModel().clearSelection();

        imgProduct.setImage(null);
        selectedImagePath = null;

        if(event != null) lblStatus.setText("");
        generateNextProductId();
    }
}