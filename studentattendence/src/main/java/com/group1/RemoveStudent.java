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

public class RemoveStudent {

    @FXML
    private TextField removeemail;

    @FXML
    private TextField removeid;

    @FXML
    private TextField removename;

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void RemoveStudentOnAction(ActionEvent e) {
        if (removename.getText().isBlank() == false && removeid.getText().isBlank() == false
                && removeemail.getText().isBlank() == false) {
            removeStudent();
            ToAttendance(e);
        } else {
            showAlert("Error", "UnCOmplete Input!!");
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

    public void removeStudent() {
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectDB = connected.getConnection();
        if (connectDB == null) {
            showAlert("Error", "Unable to connect to Database");
            return;
        }
        String username = removename.getText();
        String id = removeid.getText();
        String email = removeemail.getText();

        if (!email.endsWith("@gmail.com")) {
            showAlert("Error", "Email must end with @gmail.com");
            return;
        }

        try {
            String query = "SELECT ID, Name, Email FROM studentinfo WHERE ID = ? OR Name = ? OR Email = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, username);
            statement.setString(3, email);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                showAlert("Error", "No data Exist");
            }
            String insertData = "DELETE FROM studentinfo WHERE ID = ? AND Name = ? AND Email = ?";
            PreparedStatement statement2 = connectDB.prepareStatement(insertData);
            statement2.setString(1, id);
            statement2.setString(2, username);
            statement2.setString(3, email);
            int rowInput = statement2.executeUpdate();
            // Row Updated in mySql (1) if it insert successfully
            if (rowInput > 0) {
                showAlert("Success", "Student removed successfully");
            } else {
                showAlert("Fail", "Unable to remove student");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
