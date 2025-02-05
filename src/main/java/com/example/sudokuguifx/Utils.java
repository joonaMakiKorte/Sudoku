package com.example.sudokuguifx;

public class Utils {

    private static final int B = 3;

    // Return random int in range 1-9
    public static int randomGenerator(int num) {

        return (int) Math.floor((Math.random()*num+1));
    }

    // returns boolean indicating if safe to add to cell
    public static boolean checkIfSafe(int[][] board, int i, int j, int num) {

        return (checkRowAndCol(board,i,j,num) &&
                unUsedInBox(board,i-i%B,j-j%B,num));
    }

    // Checks if value is unused in 3x3 sub-box
    public static boolean unUsedInBox(int[][] board, int rowStart, int colStart, int num) {

        for (int i = 0; i < B; ++i) {
            for (int j = 0; j < B; ++j) {
                if (board[rowStart + i][colStart + j] == num) return false;
            }
        }
        return true;
    }

    // returns boolean indicating if safe to add to row & col
    private static boolean checkRowAndCol(int[][] board, int row, int col, int num) {

        for(int i = 0; i < 9; i++) {
            if(board[i][col] != 0 && board[i][col] == num) return false; //check row
            if(board[row][i] != 0 && board[row][i] == num) return false; //check column
        }
        return true;
    }

    // Interface for notifying the UI of updates
    public interface SolverCallback {
        void onCellUpdated(int row, int col, int value) throws InterruptedException;
    }

    // Get cell background color depending on theme and coordinates
    public static String getBackground(int row, int col, boolean dark) {
        boolean isEven = evenBox(row,col);
        return isEven ? (dark ? "-fx-background-color: rgb(35,36,46);" : "-fx-background-color: white;") :
                (dark ? "-fx-background-color: rgb(44,43,64);" : "-fx-background-color: rgb(225,237,250);");
    }

    // Returns if box idx is odd or even
    private static boolean evenBox(int row, int col) {
        return ((row/3)*3+(col/3))%2 == 0;
    }
}
