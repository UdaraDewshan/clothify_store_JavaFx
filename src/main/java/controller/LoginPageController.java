package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private Button btnLogin;
    @FXML
    private CheckBox chkRemember;
    @FXML
    private Label lblMessage;
    @FXML
    private Hyperlink linkSignUp;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    @FXML
    void onLogin(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText("Please enter both Email and Password!");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Query<User> query = session.createQuery("FROM User WHERE username = :email", User.class);
            query.setParameter("email", email);
            User user = query.uniqueResult();

            if (user != null) {
                if (user.getPassword().equals(password)) {
                    lblMessage.setTextFill(Color.GREEN);
                    lblMessage.setText("Login Successful! Welcome " + user.getName());

                    goToDashboard(user.getRole());

                } else {
                    lblMessage.setTextFill(Color.RED);
                    lblMessage.setText("Incorrect Password!");
                }
            } else {
                lblMessage.setTextFill(Color.RED);
                lblMessage.setText("User not found! Please Sign Up.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setTextFill(Color.RED);
            lblMessage.setText("Database Connection Error!");
        }
    }

    private void goToDashboard(String role) {
        try {
            System.out.println("Login Success! Navigating to Dashboard as: " + role);

            /*
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            Stage currentStage = (Stage) btnLogin.getScene().getWindow();
            currentStage.close();
            stage.show();
            stage.setTitle("Clothify Dashboard - " + role);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSignUp(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/SignUpPage.fxml"))));
            Stage currentStage = (Stage) linkSignUp.getScene().getWindow();
            currentStage.close();
            stage.show();
            stage.setTitle("Sign Up");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}