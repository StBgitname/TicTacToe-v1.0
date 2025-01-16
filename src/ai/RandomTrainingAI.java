package ai;

import java.util.Random;

public class RandomTrainingAI implements TrainingAI{

    private Random random;

    // Konstruktor
    public RandomTrainingAI() {
        random = new Random();
    }

    // Trainings-AI liefert zufälligen Zug
    public int getMove(String state) {

        int move;

        do{
            move = random.nextInt(9);
        } while(state.charAt(move) != ' ');     // ist der Zug gültig?

        return move;
    }

    @Override
    public void loadQTable(String s) {

    }
}
