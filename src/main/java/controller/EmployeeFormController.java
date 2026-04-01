package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import model.entity.User;
import service.UserService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    private final UserService userService = new UserService();

    @FXML private ComboBox<String> cmbRole;
    @FXML private TextField txtName, txtEmail, txtPassword, txtPhone, txtAddress, txtJoinDate, txtHiddenId;
    @FXML private Label lblStatus;

    @FXML private TableView<User> tblEmployees;
    @FXML private TableColumn<User, Long> colId;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colPhone;
    @FXML private TableColumn<User, String> colDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Staff"));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));

        loadTableData();

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void loadTableData() {
        List<User> users = userService.getAllEmployees();
        if (users != null) {
            ObservableList<User> userList = FXCollections.observableArrayList(users);
            tblEmployees.setItems(userList);
        }
    }

    private void populateFields(User user) {
        txtHiddenId.setText(String.valueOf(user.getId()));
        txtName.setText(user.getName());
        txtEmail.setText(user.getUsername());
        txtPassword.setText(user.getPassword());
        cmbRole.setValue(user.getRole());
        txtPhone.setText(user.getPhoneNo());
        txtAddress.setText(user.getAddress());
        txtJoinDate.setText(user.getJoinDate());
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty() || cmbRole.getValue() == null) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Please fill Name, Email, Password & Role!");
            return;
        }

        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phoneRegex = "^0\\d{9}$";

        if (!txtEmail.getText().matches(emailRegex)) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Email Format! (e.g. name@clothify.com)");
            return;
        }

        if (!txtPhone.getText().matches(phoneRegex)) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Phone Number! (Must be 10 digits starting with 0)");
            return;
        }

        User user = new User();
        user.setName(txtName.getText());
        user.setUsername(txtEmail.getText());
        user.setPassword(txtPassword.getText());
        user.setRole(cmbRole.getValue());
        user.setPhoneNo(txtPhone.getText());
        user.setAddress(txtAddress.getText());
        user.setJoinDate(txtJoinDate.getText());

        if (userService.addEmployee(user)) {
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Employee Added Successfully!");
            loadTableData();
            clearFields();
        } else {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Failed to add employee!");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtHiddenId.getText().isEmpty()) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Please select an employee from the table to update!");
            return;
        }

        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phoneRegex = "^0\\d{9}$";

        if (!txtEmail.getText().matches(emailRegex)) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Email Format! (e.g. name@clothify.com)");
            return;
        }

        if (!txtPhone.getText().matches(phoneRegex)) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Invalid Phone Number! (Must be 10 digits starting with 0)");
            return;
        }

        User user = new User();
        user.setId(Long.parseLong(txtHiddenId.getText()));
        user.setName(txtName.getText());
        user.setUsername(txtEmail.getText());
        user.setPassword(txtPassword.getText());
        user.setRole(cmbRole.getValue());
        user.setPhoneNo(txtPhone.getText());
        user.setAddress(txtAddress.getText());
        user.setJoinDate(txtJoinDate.getText());

        if (userService.updateEmployee(user)) {
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Employee Updated Successfully!");
            loadTableData();
            clearFields();
        } else {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Failed to update employee!");
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtHiddenId.getText().isEmpty()) {
            lblStatus.setTextFill(Color.RED);
            lblStatus.setText("Please select an employee to delete!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Long id = Long.parseLong(txtHiddenId.getText());

            if (userService.deleteEmployee(id)) {
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Employee Deleted Successfully!");
                loadTableData();
                clearFields();
            } else {
                lblStatus.setTextFill(Color.RED);
                lblStatus.setText("Failed to delete employee!");
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtHiddenId.clear();
        txtName.clear();
        txtEmail.clear();
        txtPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        txtPhone.clear();
        txtAddress.clear();
        txtJoinDate.clear();
        lblStatus.setText("");
        tblEmployees.getSelectionModel().clearSelection();
    }
}