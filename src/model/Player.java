package model;


//  Repräsentiert einen Spieler im Tic Tac Toe Spiel.

public class Player {

    private String name; // Name des Spielers
    private char symbol; // Symbol des Spielers ('X' oder 'O')

    /**
     * Konstruktor für einen Spieler.
     *
     * @param name   Der Name des Spielers.
     * @param symbol Das Symbol des Spielers.
     */
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }
}
