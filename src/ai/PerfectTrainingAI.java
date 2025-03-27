package ai;

import java.util.*;

public class PerfectTrainingAI implements TrainingAI {

    private char aiPlayer;
    private char humanPlayer;

    // Konstruktor, der die Symbole der Spieler setzt
    public PerfectTrainingAI(char aiPlayer, char humanPlayer) {
        this.aiPlayer = aiPlayer;
        this.humanPlayer = humanPlayer;
    }

    // Liefert den optimalen Zug basierend auf dem aktuellen Zustand des Spielfelds
    @Override
    public int getMove(String state) {
        return findBestMove(state);
    }

    @Override
    public void loadQTable(String s) {
        // Keine Q-Tabelle erforderlich
    }

    // Findet den besten Zug für die KI
    private int findBestMove(String state) {
        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;

        // Überprüft alle möglichen Züge
        for (int i = 0; i < 9; i++) {
            if (state.charAt(i) == ' ') {
                // Simuliert den Zug
                String newState = state.substring(0, i) + aiPlayer + state.substring(i + 1);
                // Bewertet den Zug mit dem Minimax-Algorithmus
                int moveVal = minimax(newState, 0, false);
                // Aktualisiert den besten Zug, wenn der aktuelle Zug besser ist
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }
        return bestMove;
    }

    // Minimax-Algorithmus zur Bewertung von Zügen
    private int minimax(String state, int depth, boolean isMax) {
        int score = evaluate(state);

        // Wenn der aktuelle Zustand ein Gewinn oder Verlust ist, gibt den entsprechenden Wert zurück
        if (score == 10) return score - depth;
        if (score == -10) return score + depth;
        if (isFull(state)) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            // Maximiert den Gewinn der KI
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == ' ') {
                    String newState = state.substring(0, i) + aiPlayer + state.substring(i + 1);
                    best = Math.max(best, minimax(newState, depth + 1, false));
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            // Minimiert den Gewinn des menschlichen Spielers
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == ' ') {
                    String newState = state.substring(0, i) + humanPlayer + state.substring(i + 1);
                    best = Math.min(best, minimax(newState, depth + 1, true));
                }
            }
            return best;
        }
    }

    // Bewertet den aktuellen Zustand des Spielfelds
    private int evaluate(String state) {
        // Überprüft Reihen, Spalten und Diagonalen auf Gewinn
        for (int row = 0; row < 3; row++) {
            if (state.charAt(row * 3) == state.charAt(row * 3 + 1) && state.charAt(row * 3 + 1) == state.charAt(row * 3 + 2)) {
                if (state.charAt(row * 3) == aiPlayer) return 10;
                if (state.charAt(row * 3) == humanPlayer) return -10;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (state.charAt(col) == state.charAt(col + 3) && state.charAt(col + 3) == state.charAt(col + 6)) {
                if (state.charAt(col) == aiPlayer) return 10;
                if (state.charAt(col) == humanPlayer) return -10;
            }
        }

        if (state.charAt(0) == state.charAt(4) && state.charAt(4) == state.charAt(8)) {
            if (state.charAt(0) == aiPlayer) return 10;
            if (state.charAt(0) == humanPlayer) return -10;
        }

        if (state.charAt(2) == state.charAt(4) && state.charAt(4) == state.charAt(6)) {
            if (state.charAt(2) == aiPlayer) return 10;
            if (state.charAt(2) == humanPlayer) return -10;
        }

        return 0;
    }

    // Überprüft, ob das Spielfeld voll ist
    private boolean isFull(String state) {
        for (int i = 0; i < 9; i++) {
            if (state.charAt(i) == ' ') return false;
        }
        return true;
    }
}