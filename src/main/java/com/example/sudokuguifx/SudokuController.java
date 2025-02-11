package com.example.sudokuguifx;

import javafx.application.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.stage.*;
import javafx.util.*;
import javafx.concurrent.*;
import java.util.Arrays;
import java.util.concurrent.atomic.*;
import java.io.*;

public class SudokuController {

    private int[][] sudokuGrid, solutionGrid; // 9x9 integer array for Sudoku grid and solution

    private int mistakes; // Count user mistakes
    private boolean solving; // Flag for checking if auto-solve is active
    private boolean usedSolver; // Flag for checking if was solved with auto-solver

    private int difficulty; // The difficulty level passed from StartupController
    private String difficultyS; // Difficulty as string

    private Timeline timer; // Top keep track of the timer
    private int timeSeconds; // Track elapsed seconds

    private AtomicInteger tries = new AtomicInteger(3);
    private int hints = 3;

    private boolean dark; // Flag indicating theme

    @FXML
    private GridPane gridPane;
    @FXML
    private Label difficultyLabel, timerLabel, mistakesLabel;
    @FXML
    private Button pauseButton, newGameButton, quitButton, solveButton, hintButton;
    @FXML
    private ImageView pauseImage, resumeImage;

    @FXML
    private void initialize() {
        loadImages();
        setFonts();
    }

    private void loadImages() {
        pauseImage = createImageView("/images/pause-logo.png", 25, 25);
        resumeImage = createImageView("/images/resume-logo.png", 30, 30);

        solveButton.setGraphic(createImageView("/images/lock-icon.png", 20, 20));
        hintButton.setGraphic(createImageView("/images/light-bulb.png", 20, 20));
        pauseButton.setGraphic(pauseImage);
    }

    private ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(Utils.loadImage(path));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private void setFonts() {
        difficultyLabel.setFont(Font.font("",FontWeight.BOLD,30));
        timerLabel.setFont(Font.font("",FontWeight.SEMI_BOLD,20));
        mistakesLabel.setFont(Font.font("",FontWeight.SEMI_BOLD,20));

        solveButton.setFont(Font.font("",FontWeight.NORMAL,15));
        hintButton.setFont(Font.font("",FontWeight.NORMAL,15));
        newGameButton.setFont(Font.font("",FontWeight.NORMAL,15));
        pauseButton.setFont(Font.font("",FontWeight.NORMAL,15));
        quitButton.setFont(Font.font("",FontWeight.NORMAL,15));
    }

    public void setTheme(Scene scene, boolean dark) {
        this.dark = dark;

        String bgColor = dark ? "-fx-background-color: rgb(42,43,42);" : "-fx-background-color: whitesmoke";
        String textColor = dark ? "-fx-text-fill: white;" : "-fx-text-fill: black";
        String buttonStyle = dark ? "-fx-background-color: gray;" +
                " -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: black;" : "";

        // Style elements by theme
        scene.getRoot().setStyle(bgColor);

        difficultyLabel.setStyle(textColor);
        timerLabel.setStyle(textColor);
        mistakesLabel.setStyle(textColor);

        solveButton.setStyle(buttonStyle);
        hintButton.setStyle(buttonStyle);
        newGameButton.setStyle(buttonStyle);
        pauseButton.setStyle(buttonStyle);
        quitButton.setStyle(buttonStyle);
    }

    public void setDifficulty(String difficulty) {
        this.difficultyS = difficulty;

        // Set the difficulty level
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
        boardGenerator.fillValues();

        // Set game board and solution
        this.sudokuGrid = boardGenerator.board;
        this.solutionGrid = boardGenerator.solution;
        populateGrid();

        this.mistakes = 0;
        this.solving = false;
        this.usedSolver = false;

        startTimer();
    }

    private void startTimer() {
        timeSeconds = 0;
        timerLabel.setText(formatTime(timeSeconds)); // Display initial time

        // Create a Timeline to update the timer every second
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds++;
            timerLabel.setText(formatTime(timeSeconds)); // Update label text
        }));

        timer.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        timer.play();
    }

    // Helper method to format time (mm:ss)
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @FXML
    private void toggleTimer() {
        boolean running = timer.getStatus() == Animation.Status.RUNNING;

        if (running) {
            timer.pause();
            difficultyLabel.setText("Game Paused");
            pauseButton.setGraphic(resumeImage);
        } else {
            timer.play();
            difficultyLabel.setText(difficultyS);
            pauseButton.setGraphic(pauseImage);
        }

        gridPane.setVisible(!running);
        solveButton.setVisible(!running);
        hintButton.setVisible(!running);
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
                textField.setStyle(this.dark ? "-fx-text-fill: white; " + Utils.getBackground(row,col,true) :
                        "-fx-text-fill: black; " + Utils.getBackground(row,col,false));

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
                                    textField.setStyle(this.dark ? "-fx-text-fill: lightskyblue; " + Utils.getBackground(currentRow,currentCol,true) :
                                            "-fx-text-fill: green; " + Utils.getBackground(currentRow,currentCol,false));
                                } else {
                                    textField.setStyle("-fx-text-fill: red; "  + Utils.getBackground(currentRow,currentCol,this.dark));
                                    if (!solving) {
                                        mistakes++; // wrong input -> a strike
                                        // Clear invalid input after a small delay
                                        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                                        pause.setOnFinished(event -> textField.setText(""));
                                        pause.play();
                                    }
                                    mistakesLabel.setText("Mistakes " + mistakes + "/3");
                                }

                                // Check if game grid and solution grid match -> game won
                                // Also if mistakes == 3 -> game lost
                                if (Arrays.deepEquals(sudokuGrid,solutionGrid)) handleGameWon();
                                if (mistakes==3) handleGameLost();
                            } else {
                                sudokuGrid[currentRow][currentCol] = 0;
                                textField.setStyle(this.dark ? "-fx-text-fill: white; " + Utils.getBackground(currentRow,currentCol,true) :
                                        "-fx-text-fill: black; " + Utils.getBackground(currentRow,currentCol,false));
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
        hintButton.setVisible(false);
        solving = true;

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

            // Get the controller for startup
            StartupController startupController = loader.getController();

            // Get the current stage using the button that triggered the event
            Stage stage = (Stage) newGameButton.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);

            // Pass the scene and theme to startup
            startupController.passScene(scene,this.dark);

            stage.setTitle("New Game");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void handleSolve() {
        timer.pause();
        solveButton.setDisable(true);

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
        if (tries.get() != 0) solveButton.setDisable(false);
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
                            textField.setStyle(dark ? "-fx-text-fill: green; " + Utils.getBackground(row,col,true) :
                                    "-fx-text-fill: blue; " + Utils.getBackground(row,col,false)); // Mark as in-progress
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
                textField.setStyle(dark ? "-fx-text-fill: yellow; " + Utils.getBackground(row,col,true) :
                        "-fx-text-fill: rgb(222,132,35); " + Utils.getBackground(col,row,false));
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

