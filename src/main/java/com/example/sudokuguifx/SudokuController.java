package com.example.sudokuguifx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.concurrent.Task;
import javafx.animation.PauseTransition;

public class SudokuController {

    private int[][] sudokuGrid; // 9x9 integer array for Sudoku grid
    private int [][] solutionGrid; // Solution grid

    private int mistakes; // Count user mistakes
    private boolean solving = false; // Flag for checking if auto-solve is active
    private boolean usedSolver = false; // Flag for checking if was solved with auto-solver

    private int difficulty; // The difficulty level passed from StartupController
    private String difficultyS; // Difficulty as a string

    private Timeline timer; // Top keep track of the timer
    private int timeSeconds; // Track elapsed seconds
    private boolean isTimerRunning = true; // Timer state

    private AtomicInteger tries = new AtomicInteger(3);
    private int hints = 3;

    @FXML
    private GridPane gridPane;
    @FXML
    private Label difficultyLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label mistakesLabel;
    @FXML
    private Button pauseButton;
    @FXML
    private Button newGameButton;
    @FXML
    private Button solveButton;
    @FXML
    private Button hintButton;

    // Initialize images for buttons
    @FXML
    private void initialize() {
        Image lockIcon = new Image(getClass().getResourceAsStream("/images/lock-icon.png"));
        Image hintIcon = new Image(getClass().getResourceAsStream("/images/light-bulb.png"));

        ImageView solveImageView = new ImageView(lockIcon);
        ImageView hintImageView = new ImageView(hintIcon);

        solveImageView.setFitWidth(20);
        solveImageView.setFitHeight(20);

        hintImageView.setFitWidth(20);
        hintImageView.setFitHeight(20);

        solveButton.setGraphic(solveImageView);
        hintButton.setGraphic(hintImageView);
    }

    // Set the difficulty level
    public void setDifficulty(String difficulty) {

        this.difficultyS = difficulty;
        this.difficulty = switch (difficulty) {
            case "Easy" -> 45;
            case "Medium" -> 50;
            case "Hard" -> 55;
            default -> 60;
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
            gridPane.setVisible(false); // Hide the Sudoku grid
            solveButton.setVisible(false);
            hintButton.setVisible(false);
            difficultyLabel.setText("Game Paused"); // Show pause message
        } else {
            timer.play();  // Resume the timer
            pauseButton.setText("Pause");  // Update button text
            gridPane.setVisible(true); // Show the sudoku grid
            solveButton.setVisible(true);
            hintButton.setVisible(true);
            difficultyLabel.setText(difficultyS);
        }
        isTimerRunning = !isTimerRunning;  // Toggle the state
    }

    private void populateGrid() {
        // Set all boxes next to each other without gaps
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setGridLinesVisible(false);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                TextField textField = new TextField();
                textField.setPrefSize(50,50); // Uniform cell size
                textField.setFont(Font.font("Verdana", 20));
                textField.setAlignment(Pos.CENTER);

                // Apply Borders for 3x3 Sub-boxes
                int top = (row % 3 == 0) ? 2 : 1;
                int right = (col % 3 == 2) ? 2 : 1;
                int bottom = (row % 3 == 2) ? 2 : 1;
                int left = (col % 3 == 0) ? 2 : 1;

                if (row == 0) top = 4;
                if (col == 0) left = 4;
                if (row == 8) bottom = 4;
                if (col == 8) right = 4;

                textField.setBorder(new Border(new BorderStroke(
                        Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                        BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                        BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY, new BorderWidths(top, right, bottom, left), Insets.EMPTY)));

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
                                    textField.setStyle("-fx-text-fill: green");
                                } else {
                                    textField.setStyle("-fx-text-fill: red");
                                    if (!solving) mistakes++; // wrong input -> a strike
                                    mistakesLabel.setText("Mistakes " + mistakes + "/3");
                                }

                                // Check if game grid and solution grid match -> game won
                                // Also if mistakes == 3 -> game lost
                                if (Arrays.deepEquals(sudokuGrid,solutionGrid)) handleGameWon();
                                if (mistakes==3) handleGameLost();
                            } else {
                                sudokuGrid[currentRow][currentCol] = 0;
                                textField.setStyle("-fx-text-fill: black");
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

    private void handleGameWon() {
        timer.stop(); // Stop the timer
        pauseButton.setVisible(false); // Disable the pause button
        solveButton.setVisible(false);
        hintButton.setVisible(false);

        // Show winning message / solved message
        difficultyLabel.setText(usedSolver ? "Solved!" : "Congratulations, you won!");

        // Show the new game button
        newGameButton.setVisible(true);
    }

    private void handleGameLost() {
        timer.stop();
        pauseButton.setVisible(false);
        solveButton.setVisible(false);

        // Show losing message
        difficultyLabel.setText("Game Over!");

        // Show the new game button
        newGameButton.setVisible(true);
    }

    @FXML
    private void handleNewGame() {
        // Load the Sudoku grid scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startup_menu.fxml"));
            Parent root = loader.load();

            // Get the current stage using the button that triggered the event
            Stage stage = (Stage) newGameButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setTitle("New Game");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        // Get the current stage using the button (or any UI element) and close the application
        Stage stage = (Stage) pauseButton.getScene().getWindow();
        stage.close();

        // Ensure all JavaFX processes are stopped
        Platform.exit();

        // Forcefully terminate the JVM
        System.exit(0);
    }

    @FXML
    private void handleSolve() {
        timer.pause();

        // Load the password scene
        AtomicBoolean isPasswordCorrect = new AtomicBoolean(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("password_menu.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the AtomicBoolean reference
            PasswordController controller = loader.getController();
            controller.setPasswordCorrect(isPasswordCorrect,tries);

            // Load new stage
            Stage stage = new Stage();
            Scene scene = new Scene(root, 250, 150);
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with main gui
            stage.setTitle("Enter Password");
            stage.setScene(scene);
            stage.showAndWait(); // Wait for the password scene to be closed
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // If correct password activate solver
        if (isPasswordCorrect.get()) {
            activateSolve();
        } else {
            timer.play();
            pauseButton.setDisable(false);
        }

        // If tries left un-disable the solve button
        if (tries.get() != 0) {
            solveButton.setDisable(false);
        }
    }

    private void activateSolve() {
        // Disable mistake counting
        solving = true;

        // Disable buttons properly
        Platform.runLater(() -> {
            pauseButton.setDisable(true);
            solveButton.setDisable(true);
            hintButton.setDisable(true);
            difficultyLabel.setText("Solving...");
        });

        // Create a Task to run the solver on a background thread
        Task<Void> solverTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Solve the puzzle and pass a callback to update the UI
                Solver.solveSudoku(sudokuGrid, (row, col, value) -> {
                    // Update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        // Get the TextField at the current row and column
                        TextField textField = getTextFieldFromGridPane(col, row);
                        if (textField != null) {
                            // Update the TextField with the new value
                            textField.setText(value == 0 ? "" : String.valueOf(value));
                            textField.setStyle("-fx-text-fill: blue; -fx-alignment: center;"); // Mark as in-progress
                        }

                        // Introduce a delay of 500 milliseconds
                        PauseTransition pause = new PauseTransition(Duration.millis(1));
                        pause.setOnFinished(event -> {
                            // Continue solving after the delay
                        });
                        pause.play();
                    });

                    // Introduce a delay in the background thread
                    Thread.sleep(1);
                });
                return null;
            }
        };

        // When the solver finishes, call a method to update the UI
        solverTask.setOnSucceeded(event -> {
            solving = false; // Mark solving as complete
            usedSolver = true;
            handleGameWon(); // Call a method when solving is finished
        });

        // Start the solver task
        new Thread(solverTask).start();
    }

    @FXML
    private void handleHint() throws InterruptedException {
        // Get random empty cell and fill with correct value from solution grid
        CreateHint.hint(sudokuGrid,solutionGrid, (row, col, value) -> {
            // Get the TextField at the current row and column
            TextField textField = getTextFieldFromGridPane(col, row);
            if (textField != null) {
                // Update the TextField with the new value
                textField.setText(String.valueOf(value));
                textField.setStyle("-fx-text-fill: green; -fx-alignment: center;");
            }
        });

        --hints;
        hintButton.setText(String.valueOf(hints));
        if (hints == 0) hintButton.setDisable(true);
    }

    // Helper method to get a TextField from a specific cell in the GridPane
    private TextField getTextFieldFromGridPane(int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof TextField) {
                return (TextField) node;
            }
        }
        return null; // Return null if no TextField is found at the specified location
    }
}

