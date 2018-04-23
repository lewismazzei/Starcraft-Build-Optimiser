import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StarcraftSimulator {

    public static void main(String[] args) {


        ArrayList<State> currentStates = new ArrayList<>();
        ArrayList<State> possibleNextStates = new ArrayList<>();

        ArrayList<Goal> goals = new ArrayList<>();

        Goal goal = new Goal(Goal.unitsRequired(Integer.parseInt(args[0])));

        HashMap<Integer, State> games = new HashMap<>();

        for (int i=0; i<100; i++) {
            State game = new State(goal);
            games.put(getGameLength(game), game);
        }

        int shortest = Collections.min(games.keySet());

        State optimal = games.get(shortest);

        //todo print output
     }

     public static int getGameLength(State s) {
        int time = 0;
        while (s.getChild() != null) {
            time++;
            s = s.getChild();
        }

        return time;
     }
}