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


public class StartupController {

    private Scene scene;
    private boolean dark; // Flag indicating theme

    @FXML
    private ImageView sudokuImage, themeImage;
    @FXML
    private Label sudokuLabel;
    @FXML
    private ChoiceBox<String> difficultyChoiceBox, themeChoiceBox;
    @FXML
    private Button exitButton;

    private Image lightImage, darkImage;

    @FXML
    public void initialize() {
        loadImages();
        setupUIEffects();
        setupEventHandlers();
    }

    private void loadImages() {
        sudokuImage.setImage(Utils.loadImage("/images/sudoku-logo.png"));
        this.lightImage = Utils.loadImage("/images/light-icon.png");
        this.darkImage = Utils.loadImage("/images/dark-icon.png");

        // Set theme image
        themeImage.setImage(dark ? darkImage : lightImage);
    }

    private void setupUIEffects() {
        setupScaleEffect(sudokuLabel, 1.2);
        sudokuLabel.setFont(Font.font("", FontWeight.BOLD, 40));

        setupScaleEffect(difficultyChoiceBox, 1.1);
        setupScaleEffect(exitButton, 1.1);
        exitButton.setFont(Font.font("", FontWeight.NORMAL, 20));
    }

    private void setupScaleEffect(Node element, double scaleFactor) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), element);
        scaleIn.setToX(scaleFactor);
        scaleIn.setToY(scaleFactor);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), element);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        element.setOnMouseEntered(e -> scaleIn.playFromStart());
        element.setOnMouseExited(e -> scaleOut.playFromStart());
    }

    private void setupEventHandlers() {
        difficultyChoiceBox.setOnAction(e -> handleStartGame());
        themeChoiceBox.setOnAction(e -> handleSetTheme());
    }

    public void passScene(Scene scene, boolean dark) {
        this.scene = scene;
        this.dark = dark;

        changeTheme();
    }

    private void changeTheme() {
        String bgColor = dark ? "-fx-background-color: rgb(42,43,42);" : "-fx-background-color: whitesmoke;";
        String textColor = dark ? "-fx-text-fill: white;" : "-fx-text-fill: black";
        String buttonStyle = dark ? "-fx-background-color: gray; -fx-background-radius: 5; " +
                "-fx-border-radius: 5; -fx-border-color: black;" : "";

        scene.getRoot().setStyle(bgColor);
        sudokuLabel.setStyle(textColor);
        themeImage.setImage(dark ? darkImage : lightImage);

        themeChoiceBox.setStyle("-fx-font-size: 15px; " + buttonStyle);
        difficultyChoiceBox.setStyle("-fx-font-size: 20px; " + buttonStyle);
        exitButton.setStyle(buttonStyle);
    }

    private void handleSetTheme() {
        this.dark = "Dark".equals(themeChoiceBox.getValue());
        changeTheme();
    }

    @FXML
    private void handleStartGame() {
        String selectedDifficulty = difficultyChoiceBox.getValue();
        if (selectedDifficulty == null) {
            // Default difficulty if none is selected
            selectedDifficulty = "Easy";
        }

        // Load the Sudoku grid scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sudoku.fxml"));
            Parent root = loader.load();
            SudokuController sudokuController = loader.getController();

            // Get the current stage using the button that triggered the event
            Stage stage = (Stage) exitButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);

            sudokuController.setTheme(scene, dark);
            sudokuController.setDifficulty(selectedDifficulty);
            sudokuController.initializeBoard();

            // Set the scene
            stage.setTitle("Sudoku");
            stage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Error loading Sudoku scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit(); // Ensure all JavaFX processes are stopped
        System.exit(0); // Forcefully terminate the JVM
    }
}
