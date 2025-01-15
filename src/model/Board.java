package model;

/**
 * Repräsentiert das Spielfeld für Tic Tac Toe.
 * Verwaltet den Zustand des Spiels, überprüft Züge und den Spielstatus.
 */
public class Board {
    private char[][] board;

    /**
     * Initialisiert ein leeres Spielfeld.
     */
    public Board() {
        board = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' '; // Jede Zelle wird als leer (' ') gesetzt.
            }
        }
    }

    /**
     * Führt einen Zug auf dem Spielfeld aus.
     *
     * @param row    Die Zeile des Zuges (0-basierter Index).
     * @param col    Die Spalte des Zuges (0-basierter Index).
     * @param playerSymbol Das Symbol des Spielers ('X' oder 'O').
     * @return true, wenn der Zug gültig ist; false, wenn das Feld bereits belegt ist.
     */
    public boolean makeMove(int row, int col, char playerSymbol) {

        if (row >= 0 && row <= 2 && col >= 0 && col <= 2) {     // gültige Eingabewerte?
            if (board[row][col] == ' ') {       // Ist das Feld noch frei?
                board[row][col] = playerSymbol;
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft, ob der angegebene Spieler gewonnen hat.
     *
     * @param player Das Symbol des Spielers ('X' oder 'O').
     * @return true, wenn der Spieler gewonnen hat; sonst false.
     */
    public boolean checkWin(char player) {

        for (int i = 0; i < 3; i++) {
            // Prüfung der Reihen und Spalten
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        // Prüfung der Diagonalen
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    /**
     * Überprüft, ob das Spielfeld vollständig belegt ist.
     *
     * @return true, wenn keine leeren Felder mehr vorhanden sind; sonst false.
     */
    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    /**
     * Gibt das Spielfeld zurück.
     *
     * @return Das aktuelle Spielfeld als 2D-Array.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Erstellt eine String-Repräsentation des Spielfelds.
     * Nützlich für die Zustandsverwaltung in der KI.
     *
     * @return Ein String, der den aktuellen Zustand des Spielfelds repräsentiert.
     */
    public String getState() {
        StringBuilder state = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state.append(board[i][j]);
            }
        }
        return state.toString();
    }
}
