package com.example.graduation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sign-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 695, 400);
        stage.setTitle("UPoD HOTEL MANAGEMENT SYSTEM!");

        Image logo = new Image(HelloApplication.class.getResourceAsStream("upodlogo.png"));
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}