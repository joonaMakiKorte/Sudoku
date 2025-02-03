package com.example.sudokuguifx;

public class GenerateBoard {

    int[][] board; //board as matrix
    int[][] solution; //board to store solution
    private final int N = 9; //number of rows/cols
    private final int B = 3; //number of rows/cols of box
    int K;

    GenerateBoard(int K) {
        this.K = K;

        this.board = new int[N][N]; // Game grid
        this.solution = new int[N][N]; // Solution grid
    }

    // Fills game and solution grid
    public void fillValues() {

        fillDiagonal();
        fillRemaining(0,B);
        copyBoardToSolution();
        removeKDigits();
    }

    // Fills the 3 diagonal sub-boxes
    private void fillDiagonal() {

        for (int i=0; i<N; i=i+B) {
            fillBox(i,i);
        }
    }

    // Fills box using random generator
    private void fillBox(int row, int col) {

        int num;
        for (int i=0; i<B; ++i) {
            for (int j=0; j<B; ++j) {
                do {
                    num = Utils.randomGenerator(N);
                }
                while (!Utils.unUsedInBox(board,row,col,num));

                this.board[row+i][col+j] = num;
            }
        }
    }

    // Fill the remaining cells recursively using backtracking
    private boolean fillRemaining(int i, int j) {

        // we are over the last column
        // move one row down and back to column 0
        if (j>=N && i<N-1) {
            i = i+1;
            j = 0;
        }
        // we are outside the board
        if (i>=N && j>=N) {
            return true;
        }
        // at the first row of boxes
        if (i<B) {
            // in the first box
            // move to next box to right
            if (j<B) {
                j = B;
            }
        }
        // at the two first rows of boxes
        else if (i<N-B) {
            // we are at the starting column of a box
            // move to next column of boxes
            if (j==(int)(i/B)*B) {
                j = j + B;
            }
        }
        else {
            // at the starting column of last column of boxes
            // move one row down and back to column 0
            if (j==N-B) {
                i = i+1;
                j = 0;
                // exceeded the number of rows
                if (i>=N) return true;
            }
        }

        // loop through numbers 1-9
        // add to board if valid placement
        // recursively fill board by backtracking
        for (int num=1; num<=N;num++) {
            if (Utils.checkIfSafe(board,i,j,num)) {
                this.board[i][j] = num;
                if (fillRemaining(i,j+1)) return true;
               this.board[i][j] = 0;
            }
        }
        return false;
    }

    // Copy the filled board to the solution matrix
    private void copyBoardToSolution() {
        for (int i = 0; i < N; i++) {
            // Copy each value
            System.arraycopy(board[i], 0, solution[i], 0, N);
        }
    }

    // Remove digits from K random cells
    private void removeKDigits() {
        int count = K;
        while (count != 0) {
            int cellId = Utils.randomGenerator(N*N)-1;
            int i = (cellId/N);
            int j = cellId%N;

            if (board[i][j] != 0) {
                count--;
                this.board[i][j] = 0;
            }
        }
    }
}
