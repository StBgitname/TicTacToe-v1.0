package controller;

import ai.*;
import model.Board;
import model.Player;
import view.GameViewGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.ToDoubleBiFunction;

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
        this.ai = new TicTacToeAI();
        //TODO
        // Auswahl der Trainings-AI über GUI

        this.trainer = new RandomTrainingAI();
        //this.trainer = new AdvancedTrainingAI();
        //this.trainer = new PerfectTrainingAI('X', 'O');

        this.stateHistory = new ArrayList<>();
        this.moveHistory = new ArrayList<>();

        // Laden der Q-Tabelle
        ai.loadQTable("qtable.csv");
        System.out.println("Trainer: " + trainer.getClass().getName());
        if (trainer.getClass().getName().equals("ai.AdvancedTrainingAI")){
            trainer.loadQTable("qtable.csv");
        }

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
            //move = trainer.getMove(state);   //Test, um gegen TrainingsAI zu spielen
            int row = move / 3;
            int col = move % 3;

            // KI führt den Zug aus
            legalMove = board.makeMove(row, col, currentPlayer.getSymbol());
        } while (!legalMove);   // wiederholen, bis der Zug gültig ist


        // Speichern des Zustands und des Zuges
        stateHistory.add(state);
        moveHistory.add(move);
        //System.out.println("AI move: " + move);

        // Spielfeld in der GUI aktualisieren
        processMove();
    }

    /**
     * Verarbeitet den aktuellen Spielzug (allgemein für Spieler und KI).
     */
    private void processMove() {
        // Spielfeld in der GUI aktualisieren
        view.renderBoard(board);

        /*try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

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
     * Beendet das Spiel und speichert die Q-Tabelle.
     */
    private void endGame() {
//        view.displayMessage("Das Spiel ist beendet. Danke fürs Spielen!");
        end = true;

        // Alle Spielfelder in der GUI deaktivieren
        view.disableAllButtons();

        // Q-Tabelle speichern (falls verwendet)
        ai.saveQTable("qtable.csv");

        //System.out.println("moveHistory: " + moveHistory);
        //System.out.println("stateHistory: " + stateHistory);
    }

    public void trainAI(int numGames) {

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
}