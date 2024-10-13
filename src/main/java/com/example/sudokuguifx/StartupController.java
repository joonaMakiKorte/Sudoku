package com.example.sudokuguifx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;

public class StartupController {

    @FXML
    private Button startButton; // Link the start button from FXML, or you can use any UI element

    @FXML
    private ChoiceBox<String> difficultyChoiceBox; // The dropdown menu for difficulty

    @FXML
    private void handleStartGame() {

        // Get the selected difficulty
        String selectedDifficulty = difficultyChoiceBox.getValue();
        if (selectedDifficulty == null) {
            // Default difficulty if none is selected
            selectedDifficulty = "Easy";
        }

        // Load the Sudoku grid scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sudoku.fxml"));
            Parent root = loader.load();

            // Get the current stage using the button that triggered the event
            Stage stage = (Stage) startButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setTitle("Sudoku");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        // Get the current stage using the button (or any UI element) and close the application
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();
    }
}
