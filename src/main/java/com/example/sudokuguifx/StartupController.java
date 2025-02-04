package com.example.sudokuguifx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;


public class StartupController {

    @FXML
    private ImageView sudokuImage;
    @FXML
    private Label sudokuLabel;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox; // The dropdown menu for difficulty
    @FXML
    private Button exitButton;

    public void initialize() {
        // Sudoku logo
        Image image = new Image(getClass().getResourceAsStream("/images/sudoku-logo.png"));
        sudokuImage.setImage(image);

        // Set effect for sudoku label
        setupScaleTransition(sudokuLabel);

        // Set effect for difficulty choice
        setupHoverEffect(difficultyChoiceBox, Color.DARKGREEN);
        difficultyChoiceBox.setOnAction(e -> handleStartGame());

        // Set effect for exit button
        setupHoverEffect(exitButton, Color.DARKRED);
    }

    private void setupScaleTransition(Node element) {
        // Scale transition effect
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), element);
        scaleUp.setToX(1.2); // 20% larger
        scaleUp.setToY(1.2);
        scaleUp.play();

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), element);
        scaleDown.setToX(1.0); // Back to normal size
        scaleDown.setToY(1.0);
        scaleDown.play();

        // Apply the effect
        element.setOnMouseEntered(e -> scaleUp.playFromStart());
        element.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    private void setupHoverEffect(Node element, Color shadowColor) {
        // Shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(shadowColor);
        shadow.setRadius(10);

        // Scale transition for smooth hover effect
        ScaleTransition hoverIn = new ScaleTransition(Duration.millis(200), element);
        hoverIn.setToX(1.1);
        hoverIn.setToY(1.1);

        ScaleTransition hoverOut = new ScaleTransition(Duration.millis(200), element);
        hoverOut.setToX(1.0);
        hoverOut.setToY(1.0);

        // Apply hover behavior
        element.setOnMouseEntered(e -> {
            hoverIn.playFromStart();
            element.setEffect(shadow);
        });
        element.setOnMouseExited(e -> {
            hoverOut.playFromStart();
            element.setEffect(null);
        });
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
            Stage stage = (Stage) difficultyChoiceBox.getScene().getWindow();
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
        Stage stage = (Stage) difficultyChoiceBox.getScene().getWindow();
        stage.close();

        // Ensure all JavaFX processes are stopped
        Platform.exit();

        // Forcefully terminate the JVM
        System.exit(0);
    }
}
