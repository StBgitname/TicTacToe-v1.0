package ai;

import java.util.*;

public class Transformations {

    /**
     * Führt eine Rotation des Tic Tac Toe Spiels um 90 Grad durch.
     *
     * @param state Der aktuelle Zustand des Spielfelds als String.
     * @return Der Zustand nach der Rotation.
     */
    public static String rotate(String state) {
        char[] rotated = new char[9];
        rotated[0] = state.charAt(6);
        rotated[1] = state.charAt(3);
        rotated[2] = state.charAt(0);
        rotated[3] = state.charAt(7);
        rotated[4] = state.charAt(4);
        rotated[5] = state.charAt(1);
        rotated[6] = state.charAt(8);
        rotated[7] = state.charAt(5);
        rotated[8] = state.charAt(2);
        return new String(rotated);
    }

    /**
     * Spiegelt das Tic Tac Toe-Brett horizontal.
     *
     * @param state Der aktuelle Zustand des Spielfelds als String.
     * @return Der Zustand nach der horizontalen Spiegelung.
     */
    public static String mirrorHorizontal(String state) {
        char[] mirrored = new char[9];
        mirrored[0] = state.charAt(6);
        mirrored[1] = state.charAt(7);
        mirrored[2] = state.charAt(8);
        mirrored[3] = state.charAt(3);
        mirrored[4] = state.charAt(4);
        mirrored[5] = state.charAt(5);
        mirrored[6] = state.charAt(0);
        mirrored[7] = state.charAt(1);
        mirrored[8] = state.charAt(2);
        return new String(mirrored);
    }

    /**
     * Spiegelt das Tic Tac Toe-Brett vertikal.
     *
     * @param state Der aktuelle Zustand des Spielfelds als String.
     * @return Der Zustand nach der vertikalen Spiegelung.
     */
    public static String mirrorVertical(String state) {
        char[] mirrored = new char[9];
        mirrored[0] = state.charAt(2);
        mirrored[1] = state.charAt(1);
        mirrored[2] = state.charAt(0);
        mirrored[3] = state.charAt(5);
        mirrored[4] = state.charAt(4);
        mirrored[5] = state.charAt(3);
        mirrored[6] = state.charAt(8);
        mirrored[7] = state.charAt(7);
        mirrored[8] = state.charAt(6);
        return new String(mirrored);
    }

    /**
     * Generiert alle möglichen Transformationen (Rotationen und Spiegelungen) des aktuellen Spielfeldzustands.
     *
     * @param state Der ursprüngliche Zustand des Spielbretts.
     * @return Eine Menge von eindeutigen Zuständen nach verschiedenen Transformationen.
     */
    public static Set<String> getAllTransformations(String state) {
        // ein HashSet speichert keine Duplikate! Damit sind doppelte Zustände schon ausgeschlossen.
        Set<String> transformations = new HashSet<>();

        String currentState = state;

        // Rotation 0°, 90°, 180°, 270°
        for (int i = 0; i < 4; i++) {
            transformations.add(currentState);
            transformations.add(mirrorHorizontal(currentState));
            transformations.add(mirrorVertical(currentState));
            // Zustand um 90° rotieren, um zur nächsten Drehung zu kommen.
            currentState = rotate(currentState);
        }
//        System.out.println("Transformations: " + transformations);
        return transformations;
    }

    /**
     * Überprüft, ob der gegebene Zustand in der Q-Tabelle (unter Berücksichtigung von Transformationen) existiert.
     *
     * @param state   Der aktuelle Zustand des Spielfeldes.
     * @param qTable  Die Q-Tabelle mit gespeicherten Zuständen und Q-Werten.
     * @return True, wenn der Zustand oder einer seiner Transformationen in der Q-Tabelle ist, sonst False.
     */
    public static boolean isStateInQTable(String state, Map<String, double[]> qTable) {
        Set<String> transformations = getAllTransformations(state);

        // Prüfe, ob eine der Transformationen in der Q-Tabelle existiert
        for (String transformedState : transformations) {
            if (qTable.containsKey(transformedState)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gibt den kanonischen Zustand des Spielfeldes zurück (d. h. den Zustand, der alle Transformationen repräsentiert).
     *
     * @param state Der aktuelle Zustand des Spielfeldes.
     * @return Der kanonische Zustand.
     */
    public static String getCanonicalState(String state) {
        Set<String> transformations = getAllTransformations(state);

        // Den lexikographisch kleinsten Zustand zurückgeben
        return transformations.stream().min(String::compareTo).orElse(state);
    }
}
