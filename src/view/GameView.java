package view;

import model.Board;

/**
 * Verwaltet die Anzeige des Tic Tac Toe Spiels.
 * Zeigt das Spielfeld und Nachrichten für den Benutzer an.
 */
public class GameView {

    /**
     * Zeigt das aktuelle Spielfeld an.
     *
     * @param board Das Spielfeld-Objekt, das dargestellt wird.
     */
    public void displayBoard(Board board) {
        char[][] grid = board.getBoard();
        for (int i = 0; i < 3; i++) {
            System.out.println(grid[i][0] + "|" + grid[i][1] + "|" + grid[i][2]);
//            if (i < 2) System.out.println("-----"); // Trennlinie zwischen den Reihen
        }
        System.out.println("\n");
    }

    /**
     * Zeigt eine Nachricht für den Benutzer an.
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
