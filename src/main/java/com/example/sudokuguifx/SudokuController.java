package com.example.sudokuguifx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {

    // 9x9 integer array for Sudoku grid
    private int[][] sudokuGrid;
    // Solution grid
    private int [][] solutionGrid;


    private int difficulty; // The difficulty level passed from StartupController

    // Reference to the GridPane from the FXML file
    @FXML
    private GridPane gridPane;

    // Set the difficulty level
    public void setDifficulty(String difficulty) {

        this.difficulty = switch (difficulty) {
            case "Easy" -> 52;
            case "Medium" -> 56;
            case "Hard" -> 62;
            default -> 68;
        };
    }

    public void initializeBoard() {

        // Create an instance of GenerateBoard with the difficulty level
        GenerateBoard boardGenerator = new GenerateBoard(difficulty);
        // fill the board
        boardGenerator.fillValues();
        // get the final board
        sudokuGrid = boardGenerator.board;
        populateGrid();
    }

    private void populateGrid() {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                TextField textField = new TextField();
                textField.setPrefWidth(40); // Set preferred width
                textField.setPrefHeight(40); // Set preferred height
                textField.setStyle("-fx-alignment: center;");

                // Check if cell already filled -> make non-editable
                if (sudokuGrid[row][col]!=0) {
                    textField.setText(String.valueOf(sudokuGrid[row][col]));
                    textField.setEditable(false);
                } else {
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
                }
                // Add the TextField to the GridPane at the specified position
                gridPane.add(textField, col, row);
            }
        }
    }
}
