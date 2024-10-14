package com.example.sudokuguifx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SudokuController {

    // 9x9 integer array for Sudoku grid
    private int[][] sudokuGrid;
    // Solution grid
    private int [][] solutionGrid;

    private int mistakes;

    private int difficulty; // The difficulty level passed from StartupController

    private Timeline timer; // Top keep track of the timer
    private int timeSeconds; // Track elapsed seconds
    private boolean isTimerRunning = true; // Timer state

    @FXML
    private GridPane gridPane;

    @FXML
    private Label difficultyLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label mistakesLabel;

    @FXML
    private Button pauseButton; //Pause/Resume

    @FXML
    private Button exitButton; //Exit game

    // Set the difficulty level
    public void setDifficulty(String difficulty) {

        this.difficulty = switch (difficulty) {
            case "Easy" -> 52;
            case "Medium" -> 56;
            case "Hard" -> 62;
            default -> 68;
        };

        difficultyLabel.setText(difficulty);
    }

    public void initializeBoard() {

        // Create an instance of GenerateBoard with the difficulty level
        GenerateBoard boardGenerator = new GenerateBoard(difficulty);
        // fill the board
        boardGenerator.fillValues();
        // get the final board
        sudokuGrid = boardGenerator.board;
        solutionGrid = boardGenerator.solution;
        mistakes = 0;
        populateGrid();
        startTimer(); // Start the timer when the game starts
    }

    // Method to start the timer
    private void startTimer() {
        timeSeconds = 0; // Initialize the timer to 0 seconds
        timerLabel.setText(formatTime(timeSeconds)); // Display initial time

        // Create a Timeline to update the timer every second
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds++;
            timerLabel.setText(formatTime(timeSeconds)); // Update label text
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timer.play(); // Start the timer
    }

    // Helper method to format time (mm:ss)
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @FXML
    private void toggleTimer() {
        if (isTimerRunning) {
            timer.pause();  // Pause the timer
            pauseButton.setText("Resume");  // Update button text
        } else {
            timer.play();  // Resume the timer
            pauseButton.setText("Pause");  // Update button text
        }
        isTimerRunning = !isTimerRunning;  // Toggle the state
    }

    @FXML
    private void handleExit() {
        // Get the current stage using the button (or any UI element) and close the application
        Stage stage = (Stage) pauseButton.getScene().getWindow();
        stage.close();
    }

    private void populateGrid() {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                TextField textField = new TextField();
                textField.setPrefWidth(40); // Set preferred width
                textField.setPrefHeight(40); // Set preferred height
                textField.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                textField.setStyle("-fx-alignment: center;");

                // Check if cell is pre-filled -> make non-editable
                if (sudokuGrid[row][col]!=0) {
                    textField.setText(String.valueOf(sudokuGrid[row][col]));
                    textField.setEditable(false);
                } else {
                    final int currentRow = row;
                    final int currentCol = col;
                    // Limit input to single digit
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.matches("\\d?")) {
                            if (!newValue.isEmpty()) {

                                // Update corresponding value in sudokuGrid
                                sudokuGrid[currentRow][currentCol] = Integer.parseInt(newValue);

                                // Compare with the solution grid
                                if (sudokuGrid[currentRow][currentCol] == solutionGrid[currentRow][currentCol]) {

                                    // Correct value entered, make the TextField non-editable
                                    textField.setEditable(false);
                                    textField.setStyle("-fx-text-fill: green; -fx-alignment: center;");
                                } else {
                                    textField.setStyle("-fx-text-fill: red; -fx-alignment: center;");
                                    mistakes++; // wrong input -> a strike
                                    mistakesLabel.setText("Mistakes " + mistakes + "/3");
                                }
                            } else {
                                sudokuGrid[currentRow][currentCol] = 0;
                                textField.setStyle("-fx-text-fill: black; -fx-alignment: center;");
                            }
                        } else {
                            textField.setText(oldValue); // Restore old value if input is invalid
                        }
                    });
                }

                // Add the TextField to the GridPane at the specified position
                gridPane.add(textField, col, row);
            }
        }
    }
}
