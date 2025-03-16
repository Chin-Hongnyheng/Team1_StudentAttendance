package com.group1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ControllerV2 {
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

    private ObservableList<StudentDetail> studentList = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize() {
        setUpTableColumn();
        GetStudentData();
        tablestudent.setItems(studentList);
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void GetStudentData() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }

        String query = "SELECT * FROM studentdetail";
        try {
            PreparedStatement statement = connectToDB.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("Name");
                String gender = rs.getString("Gender");
                String email = rs.getString("Email");
                String major = rs.getString("Major");
                String status = rs.getString("Status");

                StudentDetail student = new StudentDetail(id, name, gender, email, major, status);
                studentList.add(student);
                // System.out.println("Fetched student: " + id + " " + name); // Debug line
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
                late.setOnAction(e -> handleCheckboxChange(present, late, absent));
                absent.setOnAction(e -> handleCheckboxChange(present, late, absent));
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
                if (selected.isSelected()) {
                    for (CheckBox other : others) {
                        other.setSelected(false);
                    }
                    StudentDetail students = getTableRow().getItem();
                    students.setStatus(selected.getText());
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
        String updatequery = "UPDATE studentdetail SET Status = ? WHERE ID = ?";
        try {
            PreparedStatement statement = connectToDB.prepareStatement(updatequery);
            statement.setString(1, students.getStatus());
            statement.setString(2, students.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveButtonOnAction(ActionEvent e) {
        for (StudentDetail student : studentList) {
            UpdateStudentData(student);
        }
        showAlert("Success", "All Attendance record saved");
    }
}
