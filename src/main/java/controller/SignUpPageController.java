package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.io.IOException;

public class SignUpPageController {

    @FXML
    private TextField addressField;

    @FXML
    private Button btnSingUp;

    @FXML
    private TextField joinDateField;

    @FXML
    private Hyperlink linkSingIn;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNoField;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField usernameField;

    @FXML
    void onSingUpAction(ActionEvent event) {
        String name = nameField.getText();
        String address = addressField.getText();
        String joinDate = joinDateField.getText();
        String phoneNo = phoneNoField.getText();
        String email = usernameField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please fill Name, Email and Password!");
            return;
        }

        String role = "Staff";

        if (email.endsWith("@clothify.admin.com")) {
            role = "Admin";
        } else if (!email.endsWith("@clothify.com")) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please use a valid company email!");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setJoinDate(joinDate);
        user.setPhoneNo(phoneNo);
        user.setUsername(email);
        user.setPassword(password);
        user.setRole(role);

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();

            statusLabel.setTextFill(Color.GREEN);
            statusLabel.setText("Success! Registered as " + role);

            clearFields();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Error: Could not register user.");
        }
    }

    private void clearFields() {
        nameField.clear();
        addressField.clear();
        joinDateField.clear();
        phoneNoField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    void switchToLogin(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"))));
            Stage currentStage = (Stage) linkSingIn.getScene().getWindow();
            currentStage.close();
            stage.show();
            stage.setTitle("Sign In");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Error loading Login Page!");
        }
    }

}