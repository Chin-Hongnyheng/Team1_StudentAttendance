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

public class ForgetController {
    @FXML
    private Button CancelPassword;

    @FXML
    private Button changepassword;

    @FXML
    private PasswordField newpassword;

    @FXML
    private TextField recentemail;

    @FXML
    private TextField recentusername;
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

    public void ChangePasswordButtonOnAction(ActionEvent e) {
        if (recentusername.getText().isBlank() == false && recentemail.getText().isBlank() == false
                && newpassword.getText().isBlank() == false) {
            ModifyPassword();
        } else {
            showAlert("Error", "Please provide an Input");
        }
    }

    public void ToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent loginPage = loader.load();
            Stage stage = (Stage) newpassword.getScene().getWindow();

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

    public void SigninToLogin(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Login.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void ModifyPassword() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        String recentUsername = recentusername.getText();
        String recentEmail = recentemail.getText();
        String newPassword = newpassword.getText();
        if (!recentEmail.endsWith("@gmail.com")) {
            showAlert("Error", "Email must be a valid Gmail address!");
            return;
        }

        String query = "UPDATE login SET password = '" + newPassword + "' WHERE username = '" + recentUsername
                + "' AND email = '" + recentEmail + "'";
        try {
            Statement statement = connectToDB.createStatement();
            int rowAffected = statement.executeUpdate(query);
            if (rowAffected > 0) {
                showAlert("Successful", "Change password successful");
                ToLogin();
            } else {
                showAlert("Error", "Unable to change password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
