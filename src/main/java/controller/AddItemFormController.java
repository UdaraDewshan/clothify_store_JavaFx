package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AddItemFormController {

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private ComboBox<String> cmbSupplier;

    @FXML
    private TableColumn<?, ?> colBuy;

    @FXML
    private TableColumn<?, ?> colCategory;

    @FXML
    private TableColumn<?, ?> colCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colProfit;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colSell;

    @FXML
    private TableColumn<?, ?> colSupplier;

    @FXML
    private Label lblStatus;

    @FXML
    private TableView<?> tblItems;

    @FXML
    private TextField txtBuying;

    @FXML
    private TextField txtCode;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtProfit;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtSelling;

    @FXML
    private TextField txtSize;

    @FXML
    void addItem(ActionEvent event) {
        System.out.println("Add Item Clicked!");
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
    }

}