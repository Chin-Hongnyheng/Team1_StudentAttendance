package com.group1;

import java.sql.Connection;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestSignup {
    @FXML
    private ImageView logoImage;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createAccountButton;

    public void display(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0c4da2;");

        // Logo Section
        VBox logoSection = new VBox();
        logoSection.setAlignment(Pos.CENTER);
        logoSection.setPrefWidth(300);

        ImageView logoImage = new ImageView(new Image(
                "com/group1/itc_logo.png")); // Replace
                                             // with
                                             // actual
                                             // image
                                             // path
        logoImage.setFitWidth(200);
        logoImage.setPreserveRatio(true);

        logoSection.getChildren().add(logoImage);

        // Form Section
        VBox formSection = new VBox(10);
        formSection.setAlignment(Pos.CENTER);
        formSection.setStyle("-fx-background-color: white; -fx-padding: 40px; -fx-border-radius: 20px;");

        Label titleLabel = new Label("Create Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");

        emailField = new TextField();
        emailField.setPromptText("Email");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        createAccountButton = new Button("Create Account");
        createAccountButton.setStyle("-fx-background-color: #0c4da2; -fx-text-fill: white; -fx-padding: 10px 20px;");

        createAccountButton.setOnAction(e -> TestSigninButtonOnAction(e));

        formSection.getChildren().addAll(titleLabel, fullNameField, emailField, passwordField, createAccountButton);

        HBox mainContainer = new HBox();
        mainContainer.getChildren().addAll(logoSection, formSection);

        root.setCenter(mainContainer);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void TestshowAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void TestSigninButtonOnAction(ActionEvent e) {
        if (fullNameField.getText().isBlank() == false && passwordField.getText().isBlank() == false
                && emailField.getText().isBlank() == false) {
            TestifySignin();
        } else {
            TestshowAlert("Error", "No user input!");
        }
    }

    public void TestifySignin() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        String fullName = fullNameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        // String confirmPassword = verifyfield.getText();

        String query = "INSERT INTO login (username, password, Email) VALUES ('" + fullName + "','"
                + password + "','" + email + "')";
        // if (!signinPassword.equals(confirmPassword)) {
        // showAlert("Error", "Fail to Verify your password");
        // }
        try {
            Statement statement = connectToDB.createStatement();

            // affecting row modifying data in the database

            int rowInput = statement.executeUpdate(query);
            // successful insert into at least 1 row in database
            if (rowInput > 0) {
                TestshowAlert("Successful", "Sign in Successfully");
                // ToLogin();
            } else {
                TestshowAlert("Error", "Unable to sign in");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
