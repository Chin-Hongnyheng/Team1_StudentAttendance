package com.group1;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OptionController {
    private String courseId;

    public void PDIOnAction(ActionEvent e) {
        try {
            ShareData.courseId = "C001";
            LoadToHomepage(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void SEOnAction(ActionEvent e) {
        try {
            ShareData.courseId = "C002";
            LoadToHomepage(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void UXAndUIOnAction(ActionEvent e) {
        try {
            ShareData.courseId = "C004";
            LoadToHomepage(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void FCDSOnAction(ActionEvent e) {
        try {
            ShareData.courseId = "C005";
            LoadToHomepage(e);
        } catch (IOException ex) {
            ex.printStackTrace();
            ;
        }
    }

    public void STEMOnAction(ActionEvent e) {
        try {
            ShareData.courseId = "C003";
            LoadToHomepage(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void ProjectAndSeminarOnAction(ActionEvent e) {
        try {
            ShareData.courseId = "C006";
            LoadToHomepage(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void LoadToHomepage(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/com/group1/style.css").toExternalForm());

        HomepageController homepagecontroller = loader.getController();
        homepagecontroller.DisplayName();

        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
