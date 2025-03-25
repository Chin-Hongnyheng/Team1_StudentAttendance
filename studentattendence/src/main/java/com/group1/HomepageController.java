package com.group1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.group1.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomepageController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label setWelcome;
    @FXML
    private TableColumn<StudentDetail, String> emailcolumn2;

    @FXML
    private TableColumn<StudentDetail, String> gendercolumn2;

    @FXML
    private TableColumn<StudentDetail, String> idcolumn2;

    @FXML
    private TableColumn<StudentDetail, String> majorcolumn2;

    @FXML
    private TableColumn<StudentDetail, String> namecolumn2;

    @FXML
    private TableColumn<StudentDetail, String> statuscolumn2;

    @FXML
    private TableView<StudentDetail> studenttable2;

    @FXML
    private ImageView profile;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis Xaxis;

    @FXML
    private NumberAxis Yaxis;

    @FXML
    private PieChart pChart;

    private ObservableList<StudentDetail> studentList = FXCollections.observableArrayList();

    public void initialize() {
        setUpTableColumn();
        GetStudentDataYesterday();
        studenttable2.setItems(studentList);
        LoadStudentData();
        pieChartData();
    }

    public void DisplayName() {
        String username = ShareData.username;
        String profileImagepath = ShareData.profileImagePath;
        if (username != null) {
            setWelcome.setText("Welcome " + username);
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void GetStudentDataYesterday() {
        String courseId = "C001";
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }
        try {
            String query = "SELECT si.ID, si.Name, si.Gender, si.Email, si.Major, ar.Status " +
                    "FROM studentinfo AS si " +
                    "LEFT JOIN attendancerecord AS ar ON si.ID = ar.id_student " +
                    "LEFT JOIN courses AS c ON ar.id_course = c.courses_id " +
                    "WHERE ar.Date = CURDATE() - INTERVAL 1 DAY";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("Name");
                String gender = rs.getString("Gender");
                String email = rs.getString("Email");
                String major = rs.getString("Major");
                String status = rs.getString("Status");
                StudentDetail student = new StudentDetail(id, name, gender, email, major, status, courseId);
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUpTableColumn() {
        // Setting table column
        idcolumn2.setCellValueFactory(new PropertyValueFactory<>("id"));
        namecolumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        gendercolumn2.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emailcolumn2.setCellValueFactory(new PropertyValueFactory<>("email"));
        majorcolumn2.setCellValueFactory(new PropertyValueFactory<>("major"));
        statuscolumn2.setCellValueFactory(new PropertyValueFactory<>("status"));
        ;

    }

    public void LoadStudentData() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }
        // Create a series for each status which x axis represent the date as a string
        // and y axis represent as the number of students
        XYChart.Series<String, Number> presentSeries = new XYChart.Series<>();
        presentSeries.setName("Present");

        XYChart.Series<String, Number> lateSeries = new XYChart.Series<>();
        lateSeries.setName("Late");

        XYChart.Series<String, Number> absentSeries = new XYChart.Series<>();
        absentSeries.setName("Absent");
        try {
            String query = "SELECT Date, Status, COUNT(*) AS count " +
                    "FROM attendancerecord " +
                    "WHERE id_course = ? " +
                    "AND Date >= CURRENT_DATE - INTERVAL 4 DAY " +
                    "GROUP BY Date, Status " +
                    "ORDER BY Date";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, "C001");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String date = rs.getDate("Date").toString();
                String status = rs.getString("Status");
                int count = rs.getInt("count");
                if (status.equalsIgnoreCase("Present")) {
                    presentSeries.getData().add(new XYChart.Data<>(date, count));
                } else if (status.equalsIgnoreCase("Late")) {
                    lateSeries.getData().add(new XYChart.Data<>(date, count));
                } else if (status.equalsIgnoreCase("Absent")) {
                    absentSeries.getData().add(new XYChart.Data<>(date, count));
                }
            }
            barChart.getData().clear();
            barChart.getData().add(presentSeries);
            barChart.getData().add(lateSeries);
            barChart.getData().add(absentSeries);
            barChart.setTitle("Attendance Statistics");
            barChart.setCategoryGap(20); // Space between date groups

            // Get reference to axes (if defined in FXML)
            if (Xaxis != null) {
                Xaxis.setLabel("Date");
            }
            if (Yaxis != null) {
                Yaxis.setLabel("Number of Students");
            }
            applySeriesColors();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load data: ");
        }
    }

    private void applySeriesColors() {
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    String color = switch (series.getName()) {
                        case "Present" -> "#2ecc71"; // Green
                        case "Late" -> "#f39c12"; // Orange
                        case "Absent" -> "#e74c3c"; // Red
                        default -> "#3498db";
                    };
                    data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                }
            }
        }
    }

    public void pieChartData() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();
        String YestedayDate = java.time.LocalDate.now().minusDays(1).toString();
        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }
        try {
            int student = 0;
            String query = "SELECT Status, COUNT(*) AS count " +
                    "FROM attendancerecord " +
                    "WHERE id_course = ? " +
                    "AND Date = CURRENT_DATE - INTERVAL 1 DAY " +
                    "GROUP BY Status";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, "C001");
            ResultSet rs = statement.executeQuery();

            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            while (rs.next()) {
                String status = rs.getString("Status");
                int count = rs.getInt("count");
                pieData.add(new PieChart.Data(status, count));
                student += count;
            }
            for (PieChart.Data data : pieData) {
                double percentage = (data.getPieValue() / student) * 100;
                data.setName(String.format("%s (%.1f%%)", data.getName(), percentage));
            }
            pChart.setData(pieData);
            pChart.setTitle("Yesterday's Attendance " + YestedayDate);
            applyPieChartColors();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load data!!");
        }
    }

    private void applyPieChartColors() {
        int colorIndex = 0;
        String[] colors = { "#2ecc71", "#f39c12", "#e74c3c" }; // Green, Orange, Red

        for (PieChart.Data data : pChart.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + colors[colorIndex++ % colors.length] + ";");
            }
        }
    }
}
