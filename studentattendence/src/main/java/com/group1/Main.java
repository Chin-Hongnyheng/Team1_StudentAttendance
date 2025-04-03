package com.group1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/group1/Login.fxml"));
        primaryStage.setTitle("Student attendance");
        primaryStage.setScene(new Scene(root, 520, 400));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("image\\itc_logo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}