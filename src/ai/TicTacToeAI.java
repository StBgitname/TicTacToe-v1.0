package ai;

import utility.QTableHandler;

import java.util.HashMap;
import java.util.Random;


//  Implementiert eine einfache KI für Tic Tac Toe mit Reinforcement Learning.
public class TicTacToeAI {

    private HashMap<String, double[]> qTable; // Q-Tabelle zur Speicherung von Boardzuständen und Q-Werten
    private Random random;
    private double learningRate = 0.1; // Lernrate
    private double discountFactor = 0.8; // Diskontierungsfaktor
    private double explorationRate = 0.2; // Wahrscheinlichkeit für Exploration


    // Konstruktor, der die KI initialisiert.
    public TicTacToeAI() {
        qTable = new HashMap<>();
        random = new Random();
    }

    /**
     * Berechnet den nächsten Zug basierend auf Q-Werten und Exploration.
     *
     * @param state Der aktuelle Zustand des Spielfelds als String.
     * @return Die Position (0-8) für den nächsten Zug.
     */
    public int getMove(String state) {
        if (!qTable.containsKey(state)) {     // existiert noch kein Eintrag zum entsprechenden Board-State?
            qTable.put(state, new double[9]); // Initialisierung aller Züge
        }

        if (random.nextDouble() < explorationRate) {    // random.nextDouble() -> Zufallswert zwischen 0 und 1
            return random.nextInt(9); // Zufälliger Zug
        } else {
            double[] qValues = qTable.get(state);
            int bestMove = 0;
            // Zug mit dem höchsten Q-Wert ermitteln
            for (int i = 1; i < qValues.length; i++) {
                if (qValues[i] > qValues[bestMove]) {
                    bestMove = i;
                }
            }
            return bestMove;
        }
    }

    /**
     * Aktualisiert den Q-Wert nach einem Spielzug.
     *
     * @param state  Der Zustand des Spielfelds vor dem Zug.
     * @param move   Der gewählte Zug.
     * @param reward Die Belohnung für den Zug.
     */
    public void updateQValue(String state, int move, double reward) {
        if (!qTable.containsKey(state)) {
            qTable.put(state, new double[9]);
        }
        double[] qValues = qTable.get(state);
        // Q-Learning-Formel
        qValues[move] += learningRate * (reward - qValues[move]);
    }




    public void loadQTable(String fileName) {
        qTable = QTableHandler.loadQTable(fileName);
    }

    public void saveQTable(String fileName) {
        QTableHandler.saveQTable(qTable, fileName);
    }

    public double getDiscountFactor() {
        return discountFactor;
    }
}
