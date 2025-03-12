package com.group1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import java.sql.Statement;

import javafx.application.Application;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private TextField Emailfield;

    @FXML
    private Button signinButtonfield;

    @FXML
    private PasswordField signinPasswordfield;

    @FXML
    private TextField usernameSigninfield;

    @FXML
    private PasswordField verifyfield;

    @FXML
    private Button forgetpasswordfield;

    // Sign in page
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
    // Homepage
    @FXML
    private Button Attendancefield;

    @FXML
    private Button ClassSchedulefield;

    @FXML
    private Button Coursefield;

    @FXML
    private Button Homefield;

    @FXML
    private Button Reportfield;

    @FXML
    private ImageView picturefield;

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

    public void SigninButtonOnAction(ActionEvent e) {
        if (usernameSigninfield.getText().isBlank() == false && signinPasswordfield.getText().isBlank() == false
                && Emailfield.getText().isBlank() == false && verifyfield.getText().isBlank() == false) {
            verifySignin();
        } else {
            showAlert("Error", "Please fill in all fields.");
        }
    }

    public void ChangePasswordButtonOnAction(ActionEvent e) {
        if (recentusername.getText().isBlank() == false && recentemail.getText().isBlank() == false
                && newpassword.getText().isBlank() == false) {
            ModifyPassword();
        } else {
            showAlert("Error", "Please provide an Input");
        }
    }

    public void loginButtonOnAction(ActionEvent e) throws Exception {
        if (usernamefield.getText().isBlank() == false && passwordfield.getText().isBlank() == false) {
            verifyLogin();
        } else {
            showAlert("Error", "No user input!");
        }
    }

    // Implement loading from one page to another page
    public void LoginToSignin(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Signin.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    public void LoginToForget(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Forget.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void LoginToHomepage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            Parent HomePage = loader.load();
            Stage stage = (Stage) passwordfield.getScene().getWindow();
            HomePage.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // Set the scene to the login page
            Scene HomeScene = new Scene(HomePage);
            stage.setScene(HomeScene);
            // stage.setResizable(false);
            stage.setFullScreen(true);
            // Show the login page
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the  Homepage");
        }
    }

    public void ToHomepage(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/Homepage.fxml"));
        scene = ((Node) e.getSource()).getScene();
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }

    public void ToCourse(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/CoursePage.fxml"));
        scene = ((Node) e.getSource()).getScene();
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }

    public void ToAttendance(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/AttendancePage.fxml"));
        scene = ((Node) e.getSource()).getScene();
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }

    public void ToReport(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/ReportPage.fxml"));
        scene = ((Node) e.getSource()).getScene();
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }

    public void ToClassSchedule(ActionEvent e) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/com/group1/ClassSchedulePage.fxml"));
        scene = ((Node) e.getSource()).getScene();
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }

    // Code Logic
    public void verifyLogin() {
        // Create an object of ConnectionToVS to call the method
        // connectDB is a connection object with interact between mysql and VScode
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectDB = connected.getConnection();

        String verifyUsername = usernamefield.getText();
        String verifyPassword = passwordfield.getText();

        // count(1) represent the number of row that match the condition and it will
        // return the value either 0 or 1 in a single column
        String query = "SELECT count(1) FROM login WHERE username = '" + verifyUsername + "'AND password = '"
                + verifyPassword + "'";
        try {
            // send SQL query to database
            Statement statement = connectDB.createStatement();
            // execute the query to retrieve data
            // ResultSet is the object that contains that data which is return as a set of
            // data
            ResultSet resultQuery = statement.executeQuery(query);

            while (resultQuery.next()) {
                // the resultQUery.getInt will return either 1 or 0 if the condition if match or
                // not in the single column which is the first index
                if (resultQuery.getInt(1) == 1) {
                    LoginToHomepage();
                    // showAlert("Successful", "Login Successfully");
                } else {
                    showAlert("Error", "Failed to log in");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                showAlert("Successful", "Sign in Successfully");
                ToLogin();
            } else {
                showAlert("Error", "Unable to sign in");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            } else {
                showAlert("Error", "Unable to change password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
