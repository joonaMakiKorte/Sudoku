package com.example.sudokuguifx;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class SudokuGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the startup menu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startup_menu.fxml"));
        Parent root = loader.load();

        // Create the scene and pass to controller
        Scene scene = new Scene(root, 600, 600);
        StartupController controller = loader.getController();
        controller.setScene(scene);

        // Set the scene
        primaryStage.setTitle("New Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}