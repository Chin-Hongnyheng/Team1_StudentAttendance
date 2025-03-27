package com.group1;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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

public class ReportController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView profile;
    @FXML
    private TableColumn<StudentDetail, String> columnemail;

    @FXML
    private TableColumn<StudentDetail, String> columngender;

    @FXML
    private TableColumn<StudentDetail, String> columnid;

    @FXML
    private TableColumn<StudentDetail, String> columnmajor;

    @FXML
    private TableColumn<StudentDetail, String> columnname;

    @FXML
    private TableColumn<StudentDetail, String> columnstatus;

    @FXML
    private TableView<StudentDetail> studenttable;

    @FXML
    private Label todayabsent;

    @FXML
    private Label todaylate;

    @FXML
    private Label todaypresented;

    @FXML
    private Label totalstudents;

    @FXML
    private Label subject;

    @FXML
    private Label teacher;

    @FXML
    private Label date;

    private ObservableList<StudentDetail> studentList = FXCollections.observableArrayList();

    @FXML
    private PieChart pChart2;

    public void initialize() {
        setUpTableColumn();
        GetStudentDataInfo();
        studenttable.setItems(studentList);
        pieChartData2();
    }

    public void DisplayName() {
        String profileImagepath = ShareData.profileImagePath;
        if (profileImagepath != null) {
            Image image = new Image(profileImagepath);
            profile.setImage(image);
        }

        Header header = new Header();
        header.GettingDataToday();
        header.GettingDataLecturer();

        int TodayTotalStudent = ShareData.TotalStudentToday;
        int TodayPresented = ShareData.PresentedToday;
        int TodayLate = ShareData.LateToday;
        int TodayAbsent = ShareData.AbsentToday;

        totalstudents.setText(String.valueOf(TodayTotalStudent));
        todaypresented.setText(String.valueOf(TodayPresented));
        todayabsent.setText(String.valueOf(TodayAbsent));
        todaylate.setText(String.valueOf(TodayLate));

        String TodaySubject = ShareData.courseName;
        String TodayLecturer = ShareData.courseLecturer;
        String TodayDate = ShareData.courseDate;

        subject.setText(TodaySubject);
        date.setText(TodayDate);
        teacher.setText("Mr " + TodayLecturer);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void GetStudentDataInfo() {
        String courseId = ShareData.courseId;
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
                    "WHERE ar.id_course = ? " +
                    "AND ar.Date = CURDATE()";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, courseId);
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
        columnid.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnname.setCellValueFactory(new PropertyValueFactory<>("name"));
        columngender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnmajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        columnstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void pieChartData2() {
        String courseId = ShareData.courseId;
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();
        String TodayDate = java.time.LocalDate.now().toString();
        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }
        try {
            int student = 0;
            String query = "SELECT Status, COUNT(*) AS count " +
                    "FROM attendancerecord " +
                    "WHERE id_course = ? " +
                    "AND Date = CURDATE() " +
                    "GROUP BY Status";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, courseId);
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
            pChart2.setData(pieData);
            pChart2.setTitle("Today's Attendance " + TodayDate);
            applyPieChartColors();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load data!!");
        }
    }

    private void applyPieChartColors() {
        int colorIndex = 0;
        String[] colors = { "#2ecc71", "#f39c12", "#e74c3c" }; // Green, Orange, Red

        for (PieChart.Data data : pChart2.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + colors[colorIndex++ % colors.length] + ";");
            }
        }
    }
}
