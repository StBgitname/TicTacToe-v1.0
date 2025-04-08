package ai;

import utility.QTableHandler;
import java.util.*;


//  Implementiert eine einfache KI für Tic Tac Toe mit Reinforcement Learning.
public class TicTacToeAI {

    private HashMap<String, double[]> qTable; // Q-Tabelle zur Speicherung von Boardzuständen und Q-Werten
    private Random random;
    private double learningRate; // Lernrate
    private double discountFactor; // Diskontierungsfaktor
    private double explorationRate; // Wahrscheinlichkeit für Exploration


    // Konstruktor, der die KI initialisiert.
    public TicTacToeAI(double learningRate, double discountFactor, double explorationRate) {
        qTable = new HashMap<>();
        random = new Random();
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.explorationRate = explorationRate;
    }

    /**
     * Berechnet den nächsten Zug basierend auf Q-Werten und Exploration.
     *
     * @param state Der aktuelle Zustand des Spielfelds als String.
     * @return Die Position (0-8) für den nächsten Zug.
     */
    public int getMove(String state) {

        // Kanonischen Zustand berechnen
        String canonicalState = Transformations.getCanonicalState(state);
        int bestMove;
        Transformations transformations = new Transformations();

        if (!qTable.containsKey(canonicalState)) {     // existiert noch kein Eintrag zum entsprechenden Board-State?
            qTable.put(canonicalState, new double[9]); // Initialisierung aller Züge
        }

        if (random.nextDouble() < explorationRate) {    // random.nextDouble() -> Zufallswert zwischen 0 und 1
            return random.nextInt(9); // Zufälliger Zug
        } else {
            double[] qValues = qTable.get(canonicalState);
            List<Integer> validMoves = new Vector<>();

            // alle gültigen Züge sammeln
            for (int i = 0; i < qValues.length; i++) {
                if (state.charAt(i) == ' ') {
                    validMoves.add(i);
                }
            }

            bestMove = validMoves.getFirst();

            // gültigen Zug mit dem höchsten Q-Wert ermitteln
            for (Integer validMove : validMoves) {

                if (qValues[validMove] > qValues[bestMove]) {
                    bestMove = validMove;
                }
            }
        }

        // Transformiere den canonicalState iterativ zurück zum ursprünglichen state
        return transformations.transformToOriginalState(canonicalState, state, bestMove);
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

    // Setter für die Parameter
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setDiscountFactor(double discountFactor) {
        this.discountFactor = discountFactor;
    }

    public void setExplorationRate(double explorationRate) {
        this.explorationRate = explorationRate;
    }
}
