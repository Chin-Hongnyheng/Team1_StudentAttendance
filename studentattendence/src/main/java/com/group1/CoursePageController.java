package com.group1;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CoursePageController {
    @FXML
    Label setWelcome;

    @FXML
    private ImageView profile;

    @FXML
    private Button FCDSButton;

    @FXML
    private Button PASButton;

    @FXML
    private Button PDIButton;

    @FXML
    private Button SEButton;

    @FXML
    private Button STEMButton;

    @FXML
    private Button UXUIButton;

    @FXML
    private Label totalabsent;

    @FXML
    private Label totallate;

    @FXML
    private Label totalpresented;

    @FXML
    private Label totalstudents;

    private Stage stage;
    private Scene scene;
    private Parent root;

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
        manageCourseAccess();
        Header header = new Header();
        header.GettingData();

        int totalStudents = ShareData.totalStudents;
        int studentPresent = ShareData.studentPresent;
        int studentLate = ShareData.studentLate;
        int studentAbsent = ShareData.studentAbsent;

        totalstudents.setText(String.valueOf(totalStudents));
        totalpresented.setText(String.valueOf(studentPresent));
        totalabsent.setText(String.valueOf(studentAbsent));
        totallate.setText(String.valueOf(studentLate));
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

    @FXML
    private void uploadProfile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String targetFolderPath = "src\\main\\resources\\com\\group1\\Profile\\";
                File targetFolder = new File(targetFolderPath);
                if (!targetFolder.exists()) {
                    targetFolder.mkdirs();
                }

                // Define the target file path
                File targetFile = new File(targetFolder, selectedFile.getName());

                // Copy the selected file to the target folder
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                ShareData.profileImagePath = targetFile.toURI().toString();

                // Set the image in the ImageView
                Image logo = new Image(ShareData.profileImagePath);
                profile.setImage(logo);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void ToHomepage(ActionEvent e) throws Exception {
        LoadPage("HomePage.fxml", e);

    }

    public void ToCourse(ActionEvent e) throws Exception {
        LoadPage("CoursePage.fxml", e);
    }

    public void ToAttendance(ActionEvent e) throws Exception {
        if (!ShareData.hasVisitCoursePage) {
            showAlert("Error", "Please visit course page before accessing Attendance");
            return;
        }
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

    private void manageCourseAccess() {
        String courseId = ShareData.courseId;

        // Disable all buttons by default
        FCDSButton.setDisable(true);
        PASButton.setDisable(true);
        PDIButton.setDisable(true);
        SEButton.setDisable(true);
        STEMButton.setDisable(true);
        UXUIButton.setDisable(true);

        // Enable specific button based on courseId
        switch (courseId) {
            case "C001":
                PDIButton.setDisable(false);
                break;
            case "C002":
                SEButton.setDisable(false);
                break;
            case "C003":
                STEMButton.setDisable(false);
                break;
            case "C004":
                UXUIButton.setDisable(false);
                break;
            case "C005":
                FCDSButton.setDisable(false);
                break;
            case "C006":
                PASButton.setDisable(false);
                break;
            default:
                System.out.println("Invalid courseId: " + courseId);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void PDIOnAction(ActionEvent e) {
        if ("C001".equals(ShareData.courseId)) {
            navigateToPage(e);
            ShareData.hasVisitCoursePage = true;
        } else {
            showAlert("Error", "Unable to access Other Course");
        }
    }

    public void SEOnAction(ActionEvent e) {
        if ("C002".equals(ShareData.courseId)) {
            navigateToPage(e);
            ShareData.hasVisitCoursePage = true;
        } else {
            showAlert("Error", "Unable to access Other Course");
        }
    }

    public void STEMOnAction(ActionEvent e) {
        if ("C003".equals(ShareData.courseId)) {
            navigateToPage(e);
            ShareData.hasVisitCoursePage = true;
        } else {
            showAlert("Error", "Unable to access Other Course");
        }
    }

    public void UXUIOnAction(ActionEvent e) {
        if ("C004".equals(ShareData.courseId)) {
            navigateToPage(e);
            ShareData.hasVisitCoursePage = true;
        } else {
            showAlert("Error", "Unable to access Other Course");
        }
    }

    public void FCDSOnAction(ActionEvent e) {
        if ("C005".equals(ShareData.courseId)) {
            navigateToPage(e);
            ShareData.hasVisitCoursePage = true;
        } else {
            showAlert("Error", "Unable to access Other Course");
        }
    }

    public void PASOnAction(ActionEvent e) {
        if ("C006".equals(ShareData.courseId)) {
            navigateToPage(e);
            ShareData.hasVisitCoursePage = true;
        } else {
            showAlert("Error", "Unable to access Other Course");
        }
    }

    private void navigateToPage(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AttendancePage.fxml"));
            root = loader.load();
            scene = ((Node) e.getSource()).getScene();

            AttendanceController controller = loader.getController();
            controller.DisplayName();

            root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());
            scene.setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
