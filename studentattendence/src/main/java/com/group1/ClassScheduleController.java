package com.group1;

import java.util.Optional;

import com.group1.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ClassScheduleController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label setWelcome;

    @FXML
    private ImageView profile;

    public void initialize() {
        String username = ShareData.username;
        if (username != null) {
            setWelcome.setText("Welcome " + username);
        }
        String profileImagepath = ShareData.profileImagePath;
        if (profileImagepath != null) {
            Image image = new Image(profileImagepath);
            profile.setImage(image);
        }
    }

    @FXML
    private void logoutButtonOnAction(ActionEvent e) {
        // Show a confirmation dialog
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Logout Confirmation");
        confirmationDialog.setHeaderText("Are you sure you want to log out?");

        // Wait for the user's response
        Optional<ButtonType> result = confirmationDialog.showAndWait();

        // If the user clicks OK, exit the application
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit(); // Close the entire application
        }
        // If the user clicks Cancel, do nothing (stay in the application)
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

        // Retrieve the username from the shared data class
        String username = ShareData.username;

        // Pass the username to the new controller
        if (fxmlfile.equals("HomePage.fxml")) {
            HomepageController homepagecontroller = loader.getController();
            homepagecontroller.DisplayName();
            ;
        } else if (fxmlfile.equals("CoursePage.fxml")) {
            CoursePageController coursecontroller = loader.getController();
            coursecontroller.initialize();
        } else if (fxmlfile.equals("AttendancePage.fxml")) {
            AttendanceController attendanceController = loader.getController();
            attendanceController.DisplayName();
        } else if (fxmlfile.equals("ClassSchedulePage.fxml")) {
            ClassScheduleController classScheduleController = loader.getController();
            classScheduleController.initialize();
        } else if (fxmlfile.equals("ReportPage.fxml")) {
            ReportController reportController = loader.getController();
            reportController.DisplayName();
        }
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
        scene.setRoot(root);
    }
}
