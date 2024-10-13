package com.example.sudokuguifx;

public class GenerateBoard {

    int[][] board; //board as matrix
    int[][] solution; //board to store solution
    int N; //number of rows/cols
    int B; //number of rows/cols of box
    int K;

    GenerateBoard(int K) {
        this.N = 9;
        this.B = 3;
        this.K = K;

        this.board = new int[N][N];
        this.solution = new int[N][N];
    }

    public void fillValues() {

        fillDiagonal();
        fillRemaining(0,B);
        copyBoardToSolution();
        removeKDigits();
    }


    private void fillDiagonal() {

        for (int i=0; i<N; i=i+B) {
            fillBox(i,i);
        }
    }

    private boolean unUsedInBox(int rowStart, int colStart, int num) {

        for (int i = 0; i < B; ++i) {
            for (int j = 0; j < B; ++j) {
                if (board[rowStart + i][colStart + j] == num) return false;
            }
        }
        return true;
    }

    private void fillBox(int row, int col) {

        int num;
        for (int i=0; i<B; ++i) {
            for (int j=0; j<B; ++j) {
                do {
                    num = randomGenerator(N);
                }
                while (!unUsedInBox(row,col,num));

                this.board[row+i][col+j] = num;
            }
        }
    }

    private int randomGenerator(int num) {

        return (int) Math.floor((Math.random()*num+1));
    }

    private boolean checkIfSafe(int i, int j, int num) {

        return (checkRowAndCol(i,j,num) &&
                unUsedInBox(i-i%B,j-j%B,num));
    }

    private boolean checkRowAndCol(int row, int col, int num) {

        for(int i = 0; i < 9; i++) {
            if(board[i][col] != 0 && board[i][col] == num) return false; //check row
            if(board[row][i] != 0 && board[row][i] == num) return false; //check column
        }
        return true;
    }

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
            if (checkIfSafe(i,j,num)) {
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

    private void removeKDigits() {
        int count = K;
        while (count != 0) {
            int cellId = randomGenerator(N*N)-1;
            int i = (cellId/N);
            int j = cellId%N;

            if (board[i][j] != 0) {
                count--;
                this.board[i][j] = 0;
            }
        }
    }
}
