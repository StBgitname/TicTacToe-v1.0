package view;

import controller.GameController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Board;

/**
 * Stellt die grafische Benutzeroberfläche für das Tic Tac Toe Spiel bereit und verbindet sie mit dem GameController.
 */
public class GameViewGUI extends Application {
    private GameController gameController; // Verbindung zu GameController
    private Label statusLabel; // Statusanzeige
    private Button[][] gridButtons; // Spielfeld-Buttons
    private Button trainButton;  // Button zum AI-Training
    private Button restartButton;  // Spiel neu starten
    private TextField trainingGamesField; // Textfeld für die Anzahl der Trainingsspiele
    private TextField learningRateField;
    private TextField discountFactorField;
    private TextField explorationRateField;
    private RadioButton randomOpponentButton; // Radiobutton für zufälligen Gegner
    private RadioButton perfectOpponentButton; // Radiobutton für perfekten Gegner

    @Override
    public void start(Stage primaryStage) {

        // Hauptlayout
        VBox root = new VBox();     // Vertical Box
        root.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        root.setSpacing(10);        // vertikaler Abstand der Elemente

        // Spielfeld (3x3 Grid)
        GridPane grid = new GridPane();
        gridButtons = new Button[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(100, 100); // Größe des Buttons
//                button.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
                int row = i;
                int col = j;
                button.setOnAction(e -> gameController.handlePlayerMove(row, col)); // Ereignis-Handler mit Zeile und Spalte
                grid.add(button, j, i);
                gridButtons[i][j] = button;
            }
        }

        // Statusanzeige
        statusLabel = new Label("Willkommen zu Tic Tac Toe!");

        // restart Button
        restartButton = new Button("Neues Spiel");
        restartButton.setOnAction(event -> gameController.startNewGame());

        // Trennlinie
        Separator separator1 = new Separator();

        // Textfeld für die learningRate
        learningRateField = new TextField("0.2");
        learningRateField.setPrefWidth(60);
        // input filter für das Textfeld (nur Werte von 0 bis 1)
        learningRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^(0(\\.\\d*)?|1(\\.0*)?)$")) {
                learningRateField.setText(oldValue);
            }
        });
        // Label für learningRate
        Label learningRateLabel = new Label("Learning Rate (0-1)");
        // HBox für learningRateLabel und learningRateField
        HBox learningRateBox = new HBox(10, learningRateLabel, learningRateField);
        learningRateBox.setAlignment(Pos.CENTER_LEFT);

        // Textfeld für den Diskontierungsfaktor
        discountFactorField = new TextField("0.9");
        discountFactorField.setPrefWidth(60);

        // input filter für das Textfeld (nur Werte von 0 bis 1)
        discountFactorField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^(0(\\.\\d*)?|1(\\.0*)?)$")) {
                discountFactorField.setText(oldValue);
            }
        });
        // Label für discountFactor
        Label discountFactorLabel = new Label("Discount Factor (0-1)");
        // HBox für discountFactorLabel und discountFactorField
        HBox discountFactorBox = new HBox(10, discountFactorLabel, discountFactorField);
        discountFactorBox.setAlignment(Pos.CENTER_LEFT);

        // Textfeld für die Exploration Rate
        explorationRateField = new TextField("0.1");
        explorationRateField.setPrefWidth(60);
        // input filter für das Textfeld (nur Werte von 0 bis 1)
        explorationRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^(0(\\.\\d*)?|1(\\.0*)?)$")) {
                explorationRateField.setText(oldValue);
            }
        });
        // Label für explorationRate
        Label explorationRateLabel = new Label("Exploration Rate (0-1)");
        // HBox für explorationRateLabel und explorationRateField
        HBox explorationRateBox = new HBox(10, explorationRateLabel, explorationRateField);
        explorationRateBox.setAlignment(Pos.CENTER_LEFT);

        // Trennlinie
        Separator separator2 = new Separator();

        // Trainingsbutton
        trainButton = new Button("AI-Training");
        trainButton.setOnAction(event -> {
            String input = trainingGamesField.getText();
            int numGames = input.isEmpty() ? 100 : Integer.parseInt(input);  // Standardwert: 100 Spiele
            gameController.trainAI(numGames);
        });

        // Textfeld für die Anzahl der Trainingsspiele
        trainingGamesField = new TextField();
        trainingGamesField.setPromptText("Anzahl der Spiele");
        // input filter für das Textfeld (nur positive ganze Zahlen)
        trainingGamesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.equals("0")) {
                trainingGamesField.setText(oldValue);
            }
        });

        // HBox for trainButton and trainingGamesField
        HBox trainingBox = new HBox(10, trainButton, trainingGamesField);

        // Radiobuttons für die Auswahl des Gegners
        randomOpponentButton = new RadioButton("zufälliger Gegner");
        perfectOpponentButton = new RadioButton("perfekter Gegner");

        // ToggleGroup für die Radiobuttons
        ToggleGroup opponentGroup = new ToggleGroup();
        randomOpponentButton.setToggleGroup(opponentGroup);
        perfectOpponentButton.setToggleGroup(opponentGroup);

        // Standardmäßig den zufälligen Gegner auswählen
        randomOpponentButton.setSelected(true);

        // Trennlinie
        Separator separator3 = new Separator();

        // Button zum Löschen des Lernfortschritts
        Button clearLearningButton = new Button("Lernfortschritt löschen");
        clearLearningButton.setOnAction(event -> gameController.clearLearningProgress());

        // Layout zusammenfügen
        root.getChildren().addAll(
                grid,
                statusLabel,
                restartButton,
                separator1,
                learningRateBox,
                discountFactorBox,
                explorationRateBox,
                separator2,
                trainingBox,
                randomOpponentButton,
                perfectOpponentButton,
                separator3,
                clearLearningButton);

        // Szene und Bühne
        Scene scene = new Scene(root, 320, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();

        // Initialisiere den Controller
        gameController = new GameController(this); // Dependency Injection der aktuellen GUI

        // Initiales Spielfeld anzeigen
        renderBoard(gameController.getBoard());
    }

    /**
     * Aktualisiert die GUI basierend auf dem aktuellen Zustand des Boards.
     *
     * @param board Das aktuelle Spielfeld.
     */
    public void renderBoard(Board board) {
        char[][] grid = board.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                // Spielerfarbe setzen
                if (grid[i][j] == 'X') {
                    gridButtons[i][j].setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: red;");
                }
                if (grid[i][j] == 'O') {
                    gridButtons[i][j].setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: blue;");
                }

                // Button-Beschriftung
//                gridButtons[i][j].setText(grid[i][j] == ' ' ? "" : String.valueOf(grid[i][j]));
                gridButtons[i][j].setText(String.valueOf(grid[i][j]));
                gridButtons[i][j].setDisable(grid[i][j] != ' ');   // Deaktivieren, wenn das Feld nicht leer ist
            }
        }
    }

    /**
     * Zeigt eine Nachricht auf der GUI an.
     *
     * @param message Die Nachricht, die angezeigt werden soll.
     */
    public void displayMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Deaktiviert alle Buttons im Spielfeld.
     */
    public void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gridButtons[i][j].setDisable(true); // Deaktiviert den Button
            }
        }
    }

    public TextField getLearningRateField() {
        return learningRateField;
    }

    public TextField getDiscountFactorField() {
        return discountFactorField;
    }

    public TextField getExplorationRateField() {
        return explorationRateField;
    }

    public RadioButton getRandomOpponentButton() {
        return randomOpponentButton;
    }

    public RadioButton getPerfectOpponentButton() {
        return perfectOpponentButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}