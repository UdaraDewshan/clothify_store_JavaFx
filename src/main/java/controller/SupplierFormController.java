package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.entity.Supplier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML private TableView<Supplier> tblSuppliers;
    @FXML private TableColumn<Supplier, String> colId;
    @FXML private TableColumn<Supplier, String> colName;
    @FXML private TableColumn<Supplier, String> colCompany;
    @FXML private TableColumn<Supplier, String> colEmail;
    @FXML private TableColumn<Supplier, String> colContact;

    @FXML private TextField txtSupplierId;
    @FXML private TextField txtName;
    @FXML private TextField txtCompany;
    @FXML private TextField txtEmail;
    @FXML private TextField txtContact;
    @FXML private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        generateNextSupplierId();
        loadTableData();
    }

    private void generateNextSupplierId() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Supplier> query = session.createQuery("FROM Supplier ORDER BY supplierId DESC", Supplier.class);
            query.setMaxResults(1);
            Supplier lastSupplier = query.uniqueResult();

            if (lastSupplier != null) {
                String lastId = lastSupplier.getSupplierId();
                int nextIdNum = Integer.parseInt(lastId.replace("S", "")) + 1;
                txtSupplierId.setText(String.format("S%03d", nextIdNum));
            } else {
                txtSupplierId.setText("S001");
            }
        } catch (Exception e) {
            e.printStackTrace();
            txtSupplierId.setText("S001");
        }
    }

    private void loadTableData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Supplier> suppliers = session.createQuery("FROM Supplier", Supplier.class).list();
            ObservableList<Supplier> supplierList = FXCollections.observableArrayList(suppliers);
            tblSuppliers.setItems(supplierList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) {
        String id = txtSupplierId.getText();
        String name = txtName.getText();
        String company = txtCompany.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();

        if (id.isEmpty() || name.isEmpty() || company.isEmpty() || contact.isEmpty()) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Please fill all required fields!");
            return;
        }

        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phoneRegex = "^0\\d{9}$";

        if (!txtEmail.getText().matches(emailRegex)) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Email Format!");
            return;
        }

        if (!txtContact.getText().matches(phoneRegex)) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Contact Number! (Must be 10 digits)");
            return;
        }

        Supplier supplier = new Supplier(id, name, company, email, contact);
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(supplier);
            transaction.commit();

            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Supplier Added Successfully!");

            loadTableData();
            clearFields();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Failed to add supplier!");
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtName.clear();
        txtCompany.clear();
        txtEmail.clear();
        txtContact.clear();
        lblStatus.setText("");
        generateNextSupplierId();
    }
}