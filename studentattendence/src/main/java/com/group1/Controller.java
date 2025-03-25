package com.group1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

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

    @FXML
    private ImageView profile;

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

    public class UserProfile {
        private static String username;

        public static String getUsername() {
            return username;
        }

        public static void setUsername(String username) {
            UserProfile.username = username;
        }
    }

    public void ToHomepage(ActionEvent e) throws Exception {
        LoadPage("HomePage.fxml", e);

    }

    public void ToCourse(ActionEvent e) throws Exception {
        LoadPage("CoursePage.fxml", e);
    }

    public void ToAttendance(ActionEvent e) throws Exception {
        LoadPage("AttendancePage.fxml", e);
    }

    public void ToReport(ActionEvent e) throws Exception {
        LoadPage("ReportPage.fxml", e);
    }

    public void ToClassSchedule(ActionEvent e) throws Exception {
        LoadPage("ClassSchedulePage.fxml", e);
    }

    public void LoadPage(String fxmlfile, ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group1/" + fxmlfile));
        root = loader.load();
        scene = ((Node) e.getSource()).getScene();

        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }

    @FXML
    private void uploadProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                // Define the target folder in your project
                String targetFolderPath = "src\\main\\resources\\com\\group1\\Profile\\"; // Ensure "resources" is
                                                                                          // correctly structured
                File targetFolder = new File(targetFolderPath);

                // Ensure the target folder exists
                if (!targetFolder.exists()) {
                    targetFolder.mkdirs();
                }

                // Define the target file path
                File targetFile = new File(targetFolder, selectedFile.getName());

                // Copy the selected file to the target folder
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Set the image in the ImageView
                Image logo = new Image(targetFile.toURI().toString());
                profile.setImage(logo);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private Label setWelcome;

    public void setWelcome(String username) {
        setWelcome.setText("Welcome, " + username + "!");
    }

}
