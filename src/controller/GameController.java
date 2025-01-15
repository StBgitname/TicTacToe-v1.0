package controller;

import model.Board;
import model.Player;
import view.GameViewGUI;
import ai.TicTacToeAI;

import java.util.ArrayList;
import java.util.List;

/**
 * Steuert den Spielablauf von Tic Tac Toe.
 * Koordiniert die Interaktionen zwischen Modell, Ansicht und KI.
 */
public class GameController {
    private Board board; // Das Spielfeld
    private Player humanPlayer; // Spieler: Mensch
    private Player aiPlayer; // Spieler: KI
    private GameViewGUI view; // Verbindung zur GUI
    private TicTacToeAI ai; // KI-Logik
    private List<String> stateHistory;  // Liste der Spielfeldzustände eines Spiels
    private List<Integer> moveHistory; // Liste der KI-Züge eines Spiels
    private Player currentPlayer; // Aktueller Spieler ('X' oder 'O')

    /**
     * Konstruktor, der die GUI-Instanz entgegennimmt.
     *
     * @param view Die GUI-Instanz, um Updates anzuzeigen.
     */
    public GameController(GameViewGUI view) {
        this.board = new Board();
        this.view = view;
        this.humanPlayer = new Player("Human", 'X');
        this.aiPlayer = new Player("AI", 'O');
        this.currentPlayer = humanPlayer; // Der Mensch beginnt immer.
        this.ai = new TicTacToeAI();
        this.stateHistory = new ArrayList<>();
        this.moveHistory = new ArrayList<>();

        // Laden der Q-Tabelle
        ai.loadQTable("qtable.csv");
    }

    /**
     * Behandelt den Spielzug eines Spielers.
     *
     * @param row Die Reihe des Spielfelds, die der Spieler angeklickt hat.
     * @param col Die Spalte des Spielfelds, die der Spieler angeklickt hat.
     */
    public void handlePlayerMove(int row, int col) {
        if (currentPlayer == humanPlayer) {
            // Menschlicher Spieler macht einen Zug
            if (board.makeMove(row, col, currentPlayer.getSymbol())) {
                processMove();
            } else {
                // Fehlermeldung bei ungültigem Zug
                // (mit GUI nicht möglich, da dort schon die Buttons deaktiviert werden)
                view.displayMessage("Ungültiger Zug! Bitte wähle ein leeres Feld.");
            }
        }
    }

    /**
     * Führt den Zug der KI aus.
     * Wird vom `processMove()` aufgerufen, wenn die KI am Zug ist.
     */
    private void performAIMove() {
        // Zustand des Spielfelds abrufen
        String state = board.getState();
        boolean legalMove;
        int move;

        do {
            // KI berechnet ihren nächsten Zug
            move = ai.getMove(state);
            int row = move / 3;
            int col = move % 3;

            // KI führt den Zug aus
            legalMove = board.makeMove(row, col, currentPlayer.getSymbol());
        } while (!legalMove);   // wiederholen, bis der Zug gültig ist


        // Speichern des Zustands und des Zuges
        stateHistory.add(state);
        moveHistory.add(move);

        // Spielfeld in der GUI aktualisieren
        processMove();
    }

    /**
     * Verarbeitet den aktuellen Spielzug (allgemein für Spieler und KI).
     */
    private void processMove() {
        // Spielfeld in der GUI aktualisieren
        view.renderBoard(board);

        // Prüfen, ob der aktuelle Spieler gewonnen hat
        if (board.checkWin(currentPlayer.getSymbol())) {
            view.displayMessage("Spieler " + currentPlayer.getName() + " hat gewonnen!");
            ai.propagateRewards(currentPlayer == humanPlayer ? -1.0 : 1.0, stateHistory, moveHistory); // Belohnung für die KI
            endGame();
            return;
        }

        // Prüfen, ob das Spielfeld voll ist (Unentschieden)
        if (board.isFull()) {
            view.displayMessage("Unentschieden! Niemand gewinnt.");
            ai.propagateRewards(0.2, stateHistory, moveHistory); // leichte Belohnung
            endGame();
            return;
        }

        // Spieler wechseln
        switchPlayer();

        // Nachricht anzeigen und bei KI automatisch weitermachen
        if (currentPlayer == aiPlayer) {
            view.displayMessage("Die KI überlegt...");
            performAIMove();
        } else {
            view.displayMessage("Spieler " + currentPlayer.getSymbol() + " ist am Zug.");
        }
    }

    /**
     * Wechselt den aktuellen Spieler.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == humanPlayer) ? aiPlayer : humanPlayer;
    }

    /**
     * Aktualisiert die Belohnungen basierend auf der Spielhistorie.
     *
     * @param finalReward Die finale Belohnung.
     *//*
    private void propagateRewards(double finalReward) {
        double reward = finalReward;

        // History rückwärts durchgehen
        for (int i = stateHistory.size() - 1; i >= 0; i--) {
            String state = stateHistory.get(i);
            int move = moveHistory.get(i);

            // Q-Werte aktualisieren
            ai.updateQValue(state, move, reward);

            // Diskontiere die Belohnung
            reward *= ai.getDiscountFactor();
        }
    }*/

    /**
     * Beendet das Spiel und speichert die Q-Tabelle.
     */
    private void endGame() {
//        view.displayMessage("Das Spiel ist beendet. Danke fürs Spielen!");

        // Alle Spielfelder in der GUI deaktivieren
        view.disableAllButtons();

        // Q-Tabelle speichern (falls verwendet)
        ai.saveQTable("qtable.csv");
    }

    /**
     * Gibt das aktuelle Spielfeld (Board) zurück.
     *
     * @return Das Spielfeld.
     */
    public Board getBoard() {
        return board;
    }
}