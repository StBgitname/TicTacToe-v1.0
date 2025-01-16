package ai;

import utility.QTableHandler;

import java.util.*;


//  Implementiert eine einfache KI für Tic Tac Toe mit Reinforcement Learning.
public class AdvancedTrainingAI implements TrainingAI {

    private HashMap<String, double[]> qTable; // Q-Tabelle zur Speicherung von Boardzuständen und Q-Werten
    private Random random;

    private double explorationRate = 0.1; // Wahrscheinlichkeit für Exploration


    // Konstruktor
    public AdvancedTrainingAI() {
        qTable = new HashMap<>();
        random = new Random();
    }

    /**
     * Berechnet den nächsten Zug basierend auf Q-Werten, lernt aber nichts dazu
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

    @Override
    public void loadQTable(String fileName) {
        qTable = QTableHandler.loadQTable(fileName);
    }

}
