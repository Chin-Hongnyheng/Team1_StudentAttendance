package com.group1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStudent {

    @FXML
    private TextField newemail;

    @FXML
    private TextField newgender;

    @FXML
    private TextField newid;

    @FXML
    private TextField newmajor;

    @FXML
    private TextField newname;

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void AddStudentOnAction(ActionEvent e) {
        if (newname.getText().isBlank() == false && newid.getText().isBlank() == false
                && newgender.getText().isBlank() == false && newemail.getText().isBlank() == false
                && newmajor.getText().isBlank() == false) {
            AddNewStudent();
            ToAttendance(e);
        } else {
            showAlert("Error", "UnComplete Input!!");
        }
    }

    public void ToAttendance(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AttendancePage.fxml"));
            Parent attendancePage = loader.load();
            attendancePage.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

            AttendanceController attendanceController = loader.getController();
            attendanceController.DisplayName();

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            Scene attendanceScene = new Scene(attendancePage);
            stage.setScene(attendanceScene);

            stage.setFullScreen(true);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load attendance page");
        }
    }

    public void AddNewStudent() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectDB = connected.getConnection();
        if (connectDB == null) {
            showAlert("Error", "Unable to connect to Database");
            return;
        }
        String username = newname.getText();
        String id = newid.getText();
        String gender = newgender.getText().trim().toUpperCase();
        String email = newemail.getText();
        String major = newmajor.getText();

        if (!email.endsWith("@gmail.com")) {
            showAlert("Error", "Email must end with @gmail.com");
            return;
        }
        if (!gender.equals("M") && !gender.equals("F")) {
            showAlert("Error", "Enter M or F Only!!");
            return;
        }

        try {
            String query = "SELECT ID, Name, Email FROM studentinfo WHERE ID = ? OR Name = ? OR Email = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, username);
            statement.setString(3, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String ExistId = rs.getString("ID");
                String ExistUsername = rs.getString("Name");
                String ExistEmail = rs.getString("Email");
                if (ExistId.equals(id)) {
                    showAlert("Error", "Student ID already Exist");
                    return;
                } else if (ExistUsername.equals(username)) {
                    showAlert("Error", "Student Name already Exist");
                    return;
                } else if (ExistEmail.equals(email)) {
                    showAlert("Error", "Student Email aready Exist");
                    return;
                }
            }
            String insertData = "INSERT INTO studentinfo (ID, Name, Gender, Email, Major) VALUES (?,?,?,?,?)";
            PreparedStatement statement2 = connectDB.prepareStatement(insertData);
            statement2.setString(1, id);
            statement2.setString(2, username);
            statement2.setString(3, gender);
            statement2.setString(4, email);
            statement2.setString(5, major);
            int rowInput = statement2.executeUpdate();
            // Row Updated in mySql (1) if it insert successfully
            if (rowInput > 0) {
                showAlert("Success", "Student added successfully");
            } else {
                showAlert("Fail", "Unable to add student");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
