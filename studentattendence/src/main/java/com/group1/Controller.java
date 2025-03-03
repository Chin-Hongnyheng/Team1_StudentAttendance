package com.group1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Controller {

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
    private Label LoginMessage;

    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(ActionEvent e) {
        if (usernamefield.getText().isBlank() == false && passwordfield.getText().isBlank() == false) {
            LoginMessage.setText("Login Successfully");
        } else {
            LoginMessage.setText("Enter your username and password");
        }
    }

}
