package com.example.sudokuguifx;


public class CreateHint {

    // Interface for notifying the UI of updates
    public interface SolverCallback {
        void onCellUpdated(int row, int col, int value) throws InterruptedException;
    }

    public static void hint(int[][] board, Solver.SolverCallback callback) throws InterruptedException {

    }
}
