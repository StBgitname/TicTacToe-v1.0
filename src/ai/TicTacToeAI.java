package ai;

import utility.QTableHandler;

import java.util.*;


//  Implementiert eine einfache KI für Tic Tac Toe mit Reinforcement Learning.
public class TicTacToeAI {

    private HashMap<String, double[]> qTable; // Q-Tabelle zur Speicherung von Boardzuständen und Q-Werten
    private Random random;
    private double learningRate = 0.1; // Lernrate
    private double discountFactor = 0.8; // Diskontierungsfaktor
    private double explorationRate = 0.1; // Wahrscheinlichkeit für Exploration


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
            List<Integer> validMoves = new Vector<>();

            // alle gültigen Züge sammeln
            for (int i = 0; i < qValues.length; i++) {
                if (state.charAt(i) == ' ') {
                    validMoves.add(i);
                }
            }

            int bestMove = validMoves.getFirst();

            // gültigen Zug mit dem höchsten Q-Wert ermitteln
            for (int i = 0; i < validMoves.size(); i++) {

                if (qValues[validMoves.get(i)] > qValues[bestMove]) {
                    bestMove = validMoves.get(i);
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

    /**
     * Aktualisiert die Belohnungen basierend auf der Spielhistorie.
     *
     * @param finalReward Die finale Belohnung.
     */
    public void propagateRewards(double finalReward, List<String> stateHistory, List<Integer> moveHistory) {
        double reward = finalReward;

        // History rückwärts durchgehen
        for (int i = stateHistory.size() - 1; i >= 0; i--) {
            String state = stateHistory.get(i);
            int move = moveHistory.get(i);

            // Q-Werte aktualisieren
            updateQValue(state, move, reward);

            // Diskontiere die Belohnung
            reward *= discountFactor;
        }
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
