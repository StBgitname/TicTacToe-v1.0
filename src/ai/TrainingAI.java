package ai;

public interface TrainingAI {

    int getMove(String state);

    void loadQTable(String s);
}
