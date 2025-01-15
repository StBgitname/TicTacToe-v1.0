package utility;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class QTableHandler {

    // Methode zum Speichern der Q-Tabelle in eine CSV-Datei
    public static void saveQTable(Map<String, double[]> qTable, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, double[]> entry : qTable.entrySet()) {
                String key = entry.getKey();
                double[] values = entry.getValue();

                // Konvertiere die Werte in eine durch Kommas getrennte Zeichenkette
                StringBuilder line = new StringBuilder(key);
                for (double value : values) {
                    line.append(",").append(value);
                }

                // Schreibe die Zeile in die Datei
                writer.write(line.toString());
                writer.newLine();
            }
            System.out.println("Q-Tabelle erfolgreich in Datei gespeichert: " + fileName);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Q-Tabelle: " + e.getMessage());
        }
    }

    // Methode zum Laden der Q-Tabelle aus einer CSV-Datei
    public static HashMap<String, double[]> loadQTable(String fileName) {
        HashMap<String, double[]> qTable = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 1) {
                    String key = parts[0];
                    double[] values = new double[parts.length - 1];

                    for (int i = 1; i < parts.length; i++) {
                        values[i - 1] = Double.parseDouble(parts[i]);
                    }

                    qTable.put(key, values);
                }
            }
            System.out.println("Q-Tabelle erfolgreich aus Datei geladen: " + fileName);
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Q-Tabelle: " + e.getMessage());
        }

        return qTable;
    }
}
