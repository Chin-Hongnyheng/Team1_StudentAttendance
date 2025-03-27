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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ClassScheduleController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label setWelcome;

    @FXML
    private ImageView profile;

    @FXML
    private Label todayabsent;

    @FXML
    private Label todaylate;

    @FXML
    private Label todaypresented;

    @FXML
    private Label totalstudents;

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
        Header header = new Header();
        header.GettingDataToday();

        int TodayTotalStudent = ShareData.TotalStudentToday;
        int TodayPresented = ShareData.PresentedToday;
        int TodayLate = ShareData.LateToday;
        int TodayAbsent = ShareData.AbsentToday;

        totalstudents.setText(String.valueOf(TodayTotalStudent));
        todaypresented.setText(String.valueOf(TodayPresented));
        todayabsent.setText(String.valueOf(TodayAbsent));
        todaylate.setText(String.valueOf(TodayLate));

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void ToMondaySchedule(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MondaySchedule.fxml"));
            Parent MondayPage = loader.load();
            MondayPage.getStylesheets().add(getClass().getResource("/com/group1/color.css").toExternalForm());
            MondayPage.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

            Monday monday = loader.getController();
            monday.initialize();

            scene = ((Node) e.getSource()).getScene();
            scene.setRoot(MondayPage);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load Monday page");
        }
    }

    public void ToTuesdaySchedule(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TuesdaySchedule.fxml"));
            Parent TuesdayPage = loader.load();
            TuesdayPage.getStylesheets().add(getClass().getResource("/com/group1/color.css").toExternalForm());
            TuesdayPage.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

            Tuesday tuesday = loader.getController();
            tuesday.initialize();

            scene = ((Node) e.getSource()).getScene();
            scene.setRoot(TuesdayPage);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load Tuesday page");
        }
    }

    public void ToWednesdaySchedule(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WednesdaySchedule.fxml"));
            Parent WednesdayPage = loader.load();
            WednesdayPage.getStylesheets().add(getClass().getResource("/com/group1/color.css").toExternalForm());
            WednesdayPage.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

            Wednesday wednesday = loader.getController();
            wednesday.initialize();

            scene = ((Node) e.getSource()).getScene();
            scene.setRoot(WednesdayPage);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load Wednesday page");
        }
    }

    public void ToThursdaySchedule(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ThursdaySchedule.fxml"));
            Parent ThursdayPage = loader.load();
            ThursdayPage.getStylesheets().add(getClass().getResource("/com/group1/color.css").toExternalForm());
            ThursdayPage.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

            Thursday Thursday = loader.getController();
            Thursday.initialize();

            scene = ((Node) e.getSource()).getScene();
            scene.setRoot(ThursdayPage);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load Thursday page");
        }
    }

    public void ToFridaySchedule(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FridaySchedule.fxml"));
            Parent FridayPage = loader.load();
            FridayPage.getStylesheets().add(getClass().getResource("/com/group1/color.css").toExternalForm());
            FridayPage.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

            Friday friday = loader.getController();
            friday.initialize();

            scene = ((Node) e.getSource()).getScene();
            scene.setRoot(FridayPage);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load Friday page");
        }
    }
}
