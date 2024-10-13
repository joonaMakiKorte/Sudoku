package com.example.sudokuguifx;

public class Solver {

    public void solveSudoku(int[][] board) {
        if(board == null || board.length == 0)
            return;
        solve(board);
    }

    public boolean solve(int[][] board){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == 0){
                    for(int c = 1; c <= 9; c++){//trial. Try 1 through 9
                        if(isValid(board, i, j, c)){
                            board[i][j] = c; //Put c for this cell

                            if(solve(board))
                                return true; //If it's the solution return true
                            else
                                board[i][j] = 0; //Otherwise go back
                        }
                    }

                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int c){
        for(int i = 0; i < 9; i++) {
            if(board[i][col] != 0 && board[i][col] == c) return false; //check row
            if(board[row][i] != 0 && board[row][i] == c) return false; //check column
            if(board[3 * (row / 3) + i / 3][ 3 * (col / 3) + i % 3] != 0 &&
                    board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == c) return false; //check 3*3 block
        }
        return true;
    }
}
