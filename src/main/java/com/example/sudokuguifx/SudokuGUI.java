package com.example.sudokuguifx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class SudokuGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the startup menu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startup_menu.fxml"));
        Parent root = loader.load();

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("New Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}