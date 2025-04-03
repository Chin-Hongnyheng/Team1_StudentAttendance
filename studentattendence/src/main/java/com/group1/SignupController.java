package com.group1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {
    @FXML
    private TextField Emailfield;

    @FXML
    private Button signinButtonfield;

    @FXML
    private PasswordField signinPasswordfield;

    @FXML
    private TextField usernameSigninfield;

    @FXML
    private PasswordField verifyfield;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void SigninButtonOnAction(ActionEvent e) {
        if (usernameSigninfield.getText().isBlank() == false && signinPasswordfield.getText().isBlank() == false
                && Emailfield.getText().isBlank() == false && verifyfield.getText().isBlank() == false) {
            verifySignin();
        } else {
            showAlert("Error", "Please fill in all fields.");
        }
    }

    public void SigninToLogin(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void ToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginPage = loader.load();
            Stage stage = (Stage) usernameSigninfield.getScene().getWindow();

            // Set the scene to the login page
            Scene loginScene = new Scene(loginPage);
            stage.setScene(loginScene);

            // Show the login page
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the login page");
        }
    }

    public void verifySignin() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        String signinUsername = usernameSigninfield.getText();
        String signinPassword = signinPasswordfield.getText();
        String Email = Emailfield.getText();
        String confirmPassword = verifyfield.getText();
        if (!Email.endsWith("@gmail.com")) {
            showAlert("Error", "Email must be a valid Gmail address!");
            return;
        }

        String query = "INSERT INTO login (username, password, Email) VALUES ('" + signinUsername + "','"
                + signinPassword + "','" + Email + "')";
        if (!signinPassword.equals(confirmPassword)) {
            showAlert("Error", "Fail to Verify your password");
        }
        try {
            Statement statement = connectToDB.createStatement();

            // affecting row modifying data in the database

            int rowInput = statement.executeUpdate(query);
            // successful insert into at least 1 row in database
            if (rowInput > 0) {
                showAlert("Successful", "Create Account Successfully");
                ToLogin();
            } else {
                showAlert("Error", "Unable to sign in");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
