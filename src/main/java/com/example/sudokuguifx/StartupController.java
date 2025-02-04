package com.example.sudokuguifx;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.stage.*;
import java.io.*;
import java.util.Objects;
import javafx.animation.*;
import javafx.util.*;
import javafx.scene.paint.*;
import javafx.scene.effect.*;


public class StartupController {

    private Scene scene;
    private boolean dark; // Flag indicating theme

    @FXML
    private ImageView sudokuImage;
    @FXML
    private ImageView themeImage;
    @FXML
    private Label sudokuLabel;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox; // The dropdown menu for difficulty
    @FXML
    private ChoiceBox<String> themeChoiceBox;
    @FXML
    private Button exitButton;

    private Image lightImage;
    private Image darkImage;

    @FXML
    public void initialize() {
        // Sudoku logo
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/sudoku-logo.png")));
        sudokuImage.setImage(image);

        // Light/dark theme icons
        this.lightImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/light-icon.png")));
        this.darkImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/dark-icon.png")));

        themeImage.setImage(dark ? this.darkImage : this.lightImage);

        // Set effect for sudoku label
        setupScaleTransition(sudokuLabel);
        sudokuLabel.setFont(Font.font("",FontWeight.BOLD,40));

        // Set effect for difficulty choice
        setupHoverEffect(difficultyChoiceBox, Color.DARKGREEN);
        difficultyChoiceBox.setOnAction(e -> handleStartGame());

        // Set effect for exit button
        setupHoverEffect(exitButton, Color.DARKRED);

        themeChoiceBox.setOnAction(e -> handleSetTheme());
    }

    public void passScene(Scene scene, boolean dark) {
        this.scene = scene;
        this.dark = dark;
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

    private void handleSetTheme() {
        this.dark = themeChoiceBox.getValue().equals("Dark");

        scene.getRoot().setStyle(dark ? "-fx-background-color: rgb(42,43,42);" : "-fx-background-color: whitesmoke");
        sudokuLabel.setStyle(dark ? "-fx-text-fill: white;" : "-fx-text-fill: black");
        themeImage.setImage(dark ? this.darkImage : this.lightImage);
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

            // Get the current stage using the button that triggered the event
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);

            // Pass the selected theme and scene
            sudokuController.setTheme(scene,this.dark);

            // Initialize the sudoku board
            sudokuController.setDifficulty(selectedDifficulty);
            sudokuController.initializeBoard();

            // Set the scene
            stage.setTitle("Sudoku");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        // Get the current stage using the button (or any UI element) and close the application
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

        // Ensure all JavaFX processes are stopped
        Platform.exit();

        // Forcefully terminate the JVM
        System.exit(0);
    }
}
