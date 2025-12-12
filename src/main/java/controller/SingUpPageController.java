package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SingUpPageController {

    @FXML
    private PasswordField NameField;

    @FXML
    private TextField addressField;

    @FXML
    private Button btnSingUp;

    @FXML
    private TextField joinDateField;

    @FXML
    private Hyperlink linkSingIn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNoField;

    @FXML
    private ChoiceBox<?> roleChoiceBox;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField usernameField;

    @FXML
    void Admin(MouseEvent event) {

    }

    @FXML
    void onSingUpAction(ActionEvent event) {

    }

    @FXML
    void switchToLogin(ActionEvent event) {

    }

}
