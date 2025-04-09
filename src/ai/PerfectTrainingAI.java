package ai;

import java.util.*;

public class PerfectTrainingAI implements TrainingAI {

    private char aiPlayer;
    private char opponentPlayer;
    private Random random; // Added random for generating random moves

    // Constructor to set the symbols for the players
    public PerfectTrainingAI(char aiPlayer, char opponentPlayer) {
        this.aiPlayer = aiPlayer;
        this.opponentPlayer = opponentPlayer;
        this.random = new Random();
    }

    // Returns the optimal move based on the current state of the board
    @Override
    public int getMove(String state) {
        return findBestMove(state);
    }

    // Finds the best move for the AI
    private int findBestMove(String state) {

        // Check if it's the first move
        if (state.chars().filter(ch -> ch == ' ').count() == 9) {
            // Choose a random move for the first move
            return random.nextInt(9); // Random first move
        }

        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;

        // Check all possible moves
        for (int i = 0; i < 9; i++) {
            if (state.charAt(i) == ' ') {
                // Simulate the move
                String newState = state.substring(0, i) + aiPlayer + state.substring(i + 1);
                // Evaluate the move using the Minimax algorithm
                int moveVal = minimax(newState, 0, false);
                // Update the best move if the current move is better
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        return bestMove;
    }

    // Minimax algorithm to evaluate moves
    private int minimax(String state, int depth, boolean isMax) {
        int score = evaluate(state);

        // If the current state is a win or loss, return the corresponding value
        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (isFull(state)) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            // Maximize the AI player's score
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == ' ') {
                    String newState = state.substring(0, i) + aiPlayer + state.substring(i + 1);
                    best = Math.max(best, minimax(newState, depth + 1, false));
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            // Minimize the opponent player's score
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == ' ') {
                    String newState = state.substring(0, i) + opponentPlayer + state.substring(i + 1);
                    best = Math.min(best, minimax(newState, depth + 1, true));
                }
            }
            return best;
        }
    }

    // Evaluates the current state of the board
    private int evaluate(String state) {
        // Check rows, columns, and diagonals for a win
        for (int row = 0; row < 3; row++) {
            if (state.charAt(row * 3) == state.charAt(row * 3 + 1) && state.charAt(row * 3 + 1) == state.charAt(row * 3 + 2)) {
                if (state.charAt(row * 3) == aiPlayer) return 10;
                if (state.charAt(row * 3) == opponentPlayer) return -10;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (state.charAt(col) == state.charAt(col + 3) && state.charAt(col + 3) == state.charAt(col + 6)) {
                if (state.charAt(col) == aiPlayer) return 10;
                if (state.charAt(col) == opponentPlayer) return -10;
            }
        }

        if (state.charAt(0) == state.charAt(4) && state.charAt(4) == state.charAt(8)) {
            if (state.charAt(0) == aiPlayer) return 10;
            if (state.charAt(0) == opponentPlayer) return -10;
        }

        if (state.charAt(2) == state.charAt(4) && state.charAt(4) == state.charAt(6)) {
            if (state.charAt(2) == aiPlayer) return 10;
            if (state.charAt(2) == opponentPlayer) return -10;
        }

        return 0;
    }

    // Checks if the board is full
    private boolean isFull(String state) {
        for (int i = 0; i < 9; i++) {
            if (state.charAt(i) == ' ') return false;
        }
        return true;
    }
}