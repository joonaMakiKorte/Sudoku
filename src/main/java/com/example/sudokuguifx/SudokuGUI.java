package com.example.sudokuguifx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class SudokuGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SudokuGUI.class.getResource("sudoku.fxml"));
        Parent root = loader.load();

        // Set the scene and show the stage
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Sudoku Grid");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}