package controller;

import ai.*;
import model.Board;
import model.Player;
import utility.QTableHandler;
import view.GameViewGUI;

import java.util.ArrayList;
import java.util.HashMap;
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
    private TrainingAI trainer;  // Trainings-KI
    private List<String> stateHistory;  // Liste der Spielfeldzustände eines Spiels
    private List<Integer> moveHistory; // Liste der KI-Züge eines Spiels
    private Player currentPlayer; // Aktueller Spieler ('X' oder 'O')
    private boolean end = false;
    private int[] wins = new int[3];

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
        this.ai = new TicTacToeAI(
                Double.parseDouble(view.getLearningRateField().getText()),
                Double.parseDouble(view.getDiscountFactorField().getText()),
                Double.parseDouble(view.getExplorationRateField().getText())
        );
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
            //move = trainer.getMove(state);   //Test, um als Mensch gegen TrainingsAI zu spielen
            int row = move / 3;
            int col = move % 3;

            // KI führt den Zug aus
            legalMove = board.makeMove(row, col, currentPlayer.getSymbol());
        } while (!legalMove);   // wiederholen, bis der Zug gültig ist

        // Kanonischen Zustand und entsprechenden Zug speichern
        String canonicalState = Transformations.getCanonicalState(state);
        int canonicalMove = Transformations.transformToOriginalState(state, canonicalState, move);
        stateHistory.add(canonicalState);
        moveHistory.add(canonicalMove);

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
            wins[currentPlayer.getSymbol() == 'X' ? 0 : 1]++;
            ai.propagateRewards(currentPlayer == humanPlayer ? -1.0 : 1.0, stateHistory, moveHistory); // Belohnung für die KI
            endGame();
            return;
        }

        // Prüfen, ob das Spielfeld voll ist (Unentschieden)
        if (board.isFull()) {
            view.displayMessage("Unentschieden! Niemand gewinnt.");
            wins[2]++;
            ai.propagateRewards(0.5, stateHistory, moveHistory); // leichte Belohnung
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
     * Beendet das Spiel und speichert die Q-Tabelle.
     */
    private void endGame() {
//        view.displayMessage("Das Spiel ist beendet. Danke fürs Spielen!");
        end = true;

        // Alle Spielfelder in der GUI deaktivieren
        view.disableAllButtons();

        // Q-Tabelle speichern (falls verwendet)
        ai.saveQTable("qtable.csv");

//        System.out.println("moveHistory: " + moveHistory);
//        System.out.println("stateHistory: " + stateHistory);
    }

    public void trainAI(int numGames) {

        // Auswahl der Trainings-AI basierend auf der Radiobutton-Auswahl
        if (view.getRandomOpponentButton().isSelected()) {
            this.trainer = new RandomTrainingAI();
        } else {
            this.trainer = new PerfectTrainingAI('X', 'O');
        }

        int move;

        for (int i = 0; i < numGames; i++) {

            startNewGame();
            do {
                move = trainer.getMove(board.getState());
                //System.out.println("Trainer Move: " + move);
                int row = move / 3;
                int col = move % 3;

                handlePlayerMove(row, col);
                // performAIMove() wird automatisch in processMove() aufgerufen

            } while (!end);
        }

        view.displayMessage("Trainer: " + wins[0] + ", AI: " + wins[1] + ", Unentschieden: " + wins[2]);
        wins[0] = 0;
        wins[2] = 0;
        wins[1] = 0;
    }

    public void startNewGame() {

        // Aktuelle Werte aus den Textfeldern abrufen
        ai.setLearningRate(Double.parseDouble(view.getLearningRateField().getText()));
        ai.setDiscountFactor(Double.parseDouble(view.getDiscountFactorField().getText()));
        ai.setExplorationRate(Double.parseDouble(view.getExplorationRateField().getText()));

        // alle Werte auf Anfang
        board.resetBoard();
        currentPlayer = humanPlayer;
        stateHistory.clear();
        moveHistory.clear();
        end = false;
        view.renderBoard(board);
    }

    /**
     * Gibt das aktuelle Spielfeld (Board) zurück.
     *
     * @return Das Spielfeld.
     */
    public Board getBoard() {
        return board;
    }

    public void clearLearningProgress() {
        try {
            // schreibe leere Q-Tabelle in die CSV-Datei
            QTableHandler.saveQTable(new HashMap<>(), "qtable.csv");
            view.displayMessage("Lernfortschritt erfolgreich gelöscht.");
        } catch (Exception e) {
            view.displayMessage("Fehler beim Löschen des Lernfortschritts: " + e.getMessage());
        }
    }
}