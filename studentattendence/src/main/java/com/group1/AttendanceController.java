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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AttendanceController {
    @FXML
    private TableColumn<StudentDetail, String> Idcolume;

    @FXML
    private TableColumn<StudentDetail, String> emailcolume;

    @FXML
    private TableColumn<StudentDetail, String> gendercolume;

    @FXML
    private TableColumn<StudentDetail, String> majorcolume;

    @FXML
    private TableColumn<StudentDetail, String> namecolume;

    @FXML
    private TableColumn<StudentDetail, Void> statuscolume; // Note: Void for checkbox column

    @FXML
    private TableView<StudentDetail> tablestudent;

    @FXML
    private ImageView profile;

    @FXML
    private Label subject;

    @FXML
    private Label teacher;

    @FXML
    private Label date;

    private ObservableList<StudentDetail> studentList = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    Label setWelcome;

    public void initialize() {
        setUpTableColumn();
        GetStudentData();
        tablestudent.setItems(studentList);
    }

    public void DisplayName() {
        String username = ShareData.username;
        if (username != null) {
            setWelcome.setText("Welcome " + username);
        }
        String profileImagepath = ShareData.profileImagePath;
        if (profileImagepath != null) {
            Image image = new Image(profileImagepath);
            profile.setImage(image);
        }
        Header head = new Header();
        head.GettingDataLecturer();
        String TodaySubject = ShareData.courseName;
        String TodayLecturer = ShareData.courseLecturer;
        String TodayDate = ShareData.courseDate;

        subject.setText(TodaySubject);
        date.setText(TodayDate);
        teacher.setText("Mr " + TodayLecturer);
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void GetStudentData() {
        String courseId = ShareData.courseId;
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }
        try {
            String query = "SELECT DISTINCT si.ID, si.Name, si.Gender, si.Email, si.Major, si.Status " +
                    "FROM studentinfo AS si " +
                    "LEFT JOIN attendancerecord AS ar ON si.ID = ar.id_student " +
                    "AND ar.id_course = ? ";
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
        Idcolume.setCellValueFactory(new PropertyValueFactory<>("id"));
        namecolume.setCellValueFactory(new PropertyValueFactory<>("name"));
        gendercolume.setCellValueFactory(new PropertyValueFactory<>("gender"));
        emailcolume.setCellValueFactory(new PropertyValueFactory<>("email"));
        majorcolume.setCellValueFactory(new PropertyValueFactory<>("major"));

        // Set up the 'Status' column with checkboxes
        statuscolume.setCellFactory(param -> new TableCell<>() {
            private final CheckBox present = new CheckBox("Present");
            private final CheckBox late = new CheckBox("Late");
            private final CheckBox absent = new CheckBox("Absent");
            // Setting up what happen when you click checkbox
            {
                present.setOnAction(e -> handleCheckboxChange(present, late, absent));
                late.setOnAction(e -> handleCheckboxChange(late, present, absent));
                absent.setOnAction(e -> handleCheckboxChange(absent, present, late));
            }

            // Call when a row appear on screen (set a condition)
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    // Pass to a StudentDetail class
                    StudentDetail student = getTableRow().getItem();
                    present.setSelected("Present".equals(student.getStatus()));
                    late.setSelected("Late".equals(student.getStatus()));
                    absent.setSelected("Absent".equals(student.getStatus()));

                    HBox hbox = new HBox(10, present, late, absent);
                    setGraphic(hbox);
                }
            }

            private void handleCheckboxChange(CheckBox selected, CheckBox... others) {
                StudentDetail students = getTableRow().getItem();
                if (selected.isSelected()) {
                    for (CheckBox other : others) {
                        other.setSelected(false);
                    }
                    students.setStatus(selected.getText());
                } else {
                    // If the selected checkbox is unchecked, clear the status
                    students.setStatus(null);
                }
            }
        });
    }

    public void UpdateStudentData(StudentDetail students) {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }
        String todayDate = java.time.LocalDate.now().toString();
        String updatequery = "INSERT INTO attendancerecord (id_student, id_course, Status, Date) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE Status = VALUES(Status), Date = VALUES(Date)";
        try {
            PreparedStatement statement = connectToDB.prepareStatement(updatequery);
            statement.setString(1, students.getId());
            statement.setString(2, students.getCourseId());
            statement.setString(3, students.getStatus());
            statement.setString(4, todayDate);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveButtonOnAction(ActionEvent e) {
        String todayDate = java.time.LocalDate.now().toString();

        for (StudentDetail student : studentList) {
            // Check if attendance for today already exists
            if (isAttendanceRecorded(student.getId(), student.getCourseId(), todayDate)) {
                showAlert("Error", "Student have been recorded already");
                return;// Skip saving for this student
            }
            // Save the attendance if it doesn't already exist
            UpdateStudentData(student);
        }

        showAlert("Success", "All Attendance records saved.");
    }

    private boolean isAttendanceRecorded(String idStudent, String courseId, String date) {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return false;
        }

        String query = "SELECT COUNT(*) FROM attendancerecord WHERE id_student = ? AND id_course = ? AND Date = ?";
        try (PreparedStatement statement = connectToDB.prepareStatement(query)) {
            statement.setString(1, idStudent);
            statement.setString(2, courseId);
            statement.setString(3, date);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // if count == 1 it mean the attendance exist
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void AddStudentOnAction(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddStudent.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image\\itc_logo.png")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void RemoveStudentOnAction(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RemoveStudent.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image\\itc_logo.png")));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
