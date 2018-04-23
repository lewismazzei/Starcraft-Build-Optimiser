import java.util.ArrayList;
import java.util.HashMap;

public class StarcraftSimulator {

    public static void main(String[] args) {
        State root = new State();

        ArrayList<State> currentStates = new ArrayList<>();
        ArrayList<State> possibleNextStates = new ArrayList<>();

        ArrayList<Goal> goals = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            goals.add(new Goal(Goal.unitsRequired(i)));
        }

        currentStates.add(root);

        for (Goal goal : goals) {

        }
     }
}