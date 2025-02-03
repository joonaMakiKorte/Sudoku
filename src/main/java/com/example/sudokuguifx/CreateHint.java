package com.example.sudokuguifx;

import java.util.ArrayList;
import java.util.Random;

public class CreateHint {

    public static void hint(int[][] sudokuGrid, int[][] solutionGrid, Utils.SolverCallback callback) throws InterruptedException {
        ArrayList<int[]> emptyCells = new ArrayList<>();

        // Get all empty cells
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudokuGrid[row][col] == 0) {
                    emptyCells.add(new int[]{row, col});
                }
            }
        }

        // Choose a random empty cell and get the corresponding correct value from the solution grid
        Random random = new Random();
        int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
        int value = solutionGrid[cell[0]][cell[1]];
        sudokuGrid[cell[0]][cell[1]] = value;

        // Update on UI
        if (callback != null) {
            callback.onCellUpdated(cell[0],cell[1],value);
        }
    }
}
