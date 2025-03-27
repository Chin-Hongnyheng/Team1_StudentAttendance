package com.group1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;

public class Header {
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void GettingData() {
        String courseId = ShareData.courseId;
        // String courseId = "C001";
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }

        try {
            String query = "SELECT COUNT(DISTINCT id_student) AS totalstudent, " +
                    "SUM(CASE WHEN Status = 'Present' THEN 1 ELSE 0 END) AS studentpresent, " +
                    "SUM(CASE WHEN Status = 'Late' THEN 1 ELSE 0 END) AS studentlate, " +
                    "SUM(CASE WHEN Status = 'Absent' THEN 1 ELSE 0 END) AS studentabsent " +
                    "FROM attendancerecord " +
                    "WHERE id_course = ? AND Date = CURDATE() - INTERVAL 1 DAY";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, courseId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ShareData.totalStudents = rs.getInt("totalstudent");
                ShareData.studentPresent = rs.getInt("studentpresent");
                ShareData.studentLate = rs.getInt("studentlate");
                ShareData.studentAbsent = rs.getInt("studentabsent");
                // System.out.println(ShareData.totalStudents);

            } else {
                showAlert("No Data", "No attendance records found for the given course and date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GettingDataToday() {
        String courseId = ShareData.courseId;
        // String courseId = "C001";
        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();

        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }

        try {
            String query = "SELECT COUNT(DISTINCT id_student) AS Todaystudent, " +
                    "SUM(CASE WHEN Status = 'Present' THEN 1 ELSE 0 END) AS Todaypresent, " +
                    "SUM(CASE WHEN Status = 'Late' THEN 1 ELSE 0 END) AS Todaylate, " +
                    "SUM(CASE WHEN Status = 'Absent' THEN 1 ELSE 0 END) AS Todayabsent " +
                    "FROM attendancerecord " +
                    "WHERE id_course = ? AND Date = CURDATE()";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, courseId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ShareData.TotalStudentToday = rs.getInt("Todaystudent");
                ShareData.PresentedToday = rs.getInt("Todaypresent");
                ShareData.LateToday = rs.getInt("Todaylate");
                ShareData.AbsentToday = rs.getInt("Todayabsent");
                // System.out.println(ShareData.totalStudents);

            } else {
                showAlert("No Data", "No attendance records found for the given course and date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GettingDataLecturer() {
        String courseId = ShareData.courseId;

        ConnectionToVS connected = new ConnectionToVS();
        Connection connectToDB = connected.getConnection();
        String todayDate = java.time.LocalDate.now().toString();
        if (connectToDB == null) {
            showAlert("Error", "Unable to Connect database");
            return;
        }

        try {
            String query = "SELECT courses_name, courses_lecturer FROM courses WHERE courses_id = ?";
            PreparedStatement statement = connectToDB.prepareStatement(query);
            statement.setString(1, courseId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                ShareData.courseName = rs.getString("courses_name");
                ShareData.courseLecturer = rs.getString("courses_lecturer");
                ShareData.courseDate = todayDate;

            } else {
                showAlert("No Data", "No attendance records found for the given course and date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
