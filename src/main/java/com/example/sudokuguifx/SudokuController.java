package com.example.sudokuguifx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {

    // 9x9 integer array for Sudoku grid
    private int[][] sudokuGrid = new int[9][9];
    private TextField[][] textFields = new TextField[9][9];

    private String difficulty; // The difficulty level passed from StartupController

    // Reference to the GridPane from the FXML file
    @FXML
    private GridPane gridPane;

    // Set the difficulty level
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;

    }

    // Initialize the grid and attach listeners
    @FXML
    public void initialize() {
        // Dynamically create a 9x9 grid of TextFields
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Create a new TextField for each cell
                TextField textField = new TextField();
                textField.setPrefWidth(40); // Set preferred width
                textField.setPrefHeight(40); // Set preferred height
                textField.setStyle("-fx-alignment: center;");

                // Limit input to single digit
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.matches("\\d?")) {
                        if (!newValue.isEmpty()) {
                            // Update corresponding value in sudokuGrid
                            sudokuGrid[GridPane.getRowIndex(textField)][GridPane.getColumnIndex(textField)] = Integer.parseInt(newValue);
                        } else {
                            sudokuGrid[GridPane.getRowIndex(textField)][GridPane.getColumnIndex(textField)] = 0;
                        }
                    } else {
                        textField.setText(oldValue); // Restore old value if input is invalid
                    }
                });

                // Add the TextField to the GridPane at the specified position
                gridPane.add(textField, col, row);
                textFields[row][col] = textField; // Keep reference for potential further use
            }
        }
    }
}