package com.group1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button Loginfield;

    @FXML
    private Button Signupfield;

    @FXML
    private ImageView imagefield;

    @FXML
    private PasswordField passwordfield;

    @FXML
    private CheckBox tickfield;

    @FXML
    private TextField usernamefield;

    @FXML
    private Button cancelButton;

    @FXML
    private Button forgetpasswordfield;

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

    // Implement action on button
    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(ActionEvent e) throws Exception {
        if (usernamefield.getText().isBlank() == false && passwordfield.getText().isBlank() == false) {
            if (verifyLogin()) {
                String username = usernamefield.getText();

                ShareData.username = username;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Option.fxml"));
                root = loader.load();

                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("image\\itc_logo.png")));
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                showAlert("Error", "Incorrect username or password! Please try again.");
            }
        } else {
            showAlert("Error", "No user input!");
        }
    }

    // Implement loading from one page to another page
    public void LoginToSignin(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Signin.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image\\itc_logo.png")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void LoginToForget(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Forget.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image\\itc_logo.png")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Code Logic
    public boolean verifyLogin() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectDB = connected.getConnection();

        String verifyUsername = usernamefield.getText();
        String verifyPassword = passwordfield.getText();

        String query = "SELECT count(1) FROM login WHERE username = '" + verifyUsername + "'AND password = '"
                + verifyPassword + "'";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultQuery = statement.executeQuery(query);

            while (resultQuery.next()) {
                if (resultQuery.getInt(1) == 1) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
