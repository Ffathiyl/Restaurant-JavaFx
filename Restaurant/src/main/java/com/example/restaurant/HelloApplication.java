package com.example.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Form Login");
        stage.setScene(scene);

        // Set jendela menjadi fullscreen
        stage.setFullScreen(true);

        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}