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
import service.SupplierService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    private final SupplierService supplierService = new SupplierService();

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

        txtSupplierId.setText(supplierService.generateNextSupplierId());
        loadTableData();
    }

    private void loadTableData() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers != null) {
            ObservableList<Supplier> supplierList = FXCollections.observableArrayList(suppliers);
            tblSuppliers.setItems(supplierList);
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

        if (supplierService.addSupplier(supplier)) {
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Supplier Added Successfully!");
            loadTableData();
            clearFields();
        } else {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Failed to add supplier!");
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

        txtSupplierId.setText(supplierService.generateNextSupplierId());
    }
}