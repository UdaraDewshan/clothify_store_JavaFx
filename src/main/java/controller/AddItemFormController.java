package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class AddItemFormController {

    @FXML
    private JFXComboBox<?> cmbSupplier;

    @FXML
    private TableView<?> tblItems;

    @FXML
    private JFXTextField txtBuying;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtProfit;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtSelling;

    @FXML
    private JFXTextField txtSize;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXTextField txtType;

    @FXML
    void addItem(ActionEvent event) {

    }

    @FXML
    void clearFields(ActionEvent event) {

    }

}
