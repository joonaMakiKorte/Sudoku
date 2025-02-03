package com.example.sudokuguifx;

public class Solver {

    public static void solveSudoku(int[][] board, Utils.SolverCallback callback) throws InterruptedException {
        solve(board, callback);
    }

    private static boolean solve(int[][] board, Utils.SolverCallback callback) throws InterruptedException {
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == 0){
                    for(int c = 1; c <= 9; c++){//trial. Try 1 through 9
                        if(Utils.checkIfSafe(board, i, j, c)) {
                            board[i][j] = c; //Put c for this cell

                            if (callback != null) {
                                callback.onCellUpdated(i,j,c);
                            }

                            if(solve(board,callback)) {
                                return true; //If it's the solution return true
                            }

                            board[i][j] = 0; //Otherwise go back

                            // Notify the callback
                            if (callback != null) {
                                callback.onCellUpdated(i,j,0);
                            }
                        }
                    }

                    return false;
                }
            }
        }
        return true;
    }

}
