package com.example.sudokuguifx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.image.ImageView;


public class StartupController {

    @FXML
    private ImageView sudokuImage;
    @FXML
    private Label sudokuLabel;
    @FXML
    private Button startButton; // Link the start button from FXML, or you can use any UI element
    @FXML
    private ChoiceBox<String> difficultyChoiceBox; // The dropdown menu for difficulty

    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/images/sudoku-logo.png"));
        sudokuImage.setImage(image);

        sudokuLabel.setOnMouseEntered(this::onHover);
        sudokuLabel.setOnMouseExited(this::onExit);
    }

    private void onHover(MouseEvent event) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), sudokuLabel);
        scaleUp.setToX(1.2); // 20% larger
        scaleUp.setToY(1.2);
        scaleUp.play();
    }

    private void onExit(MouseEvent event) {
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), sudokuLabel);
        scaleDown.setToX(1.0); // Back to normal size
        scaleDown.setToY(1.0);
        scaleDown.play();
    }
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

            // Get the controller for the Sudoku scene
            SudokuController sudokuController = loader.getController();
            // Pass the difficulty to the SudokuController
            sudokuController.setDifficulty(selectedDifficulty);
            sudokuController.initializeBoard();

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

        // Ensure all JavaFX processes are stopped
        Platform.exit();

        // Forcefully terminate the JVM
        System.exit(0);
    }
}
