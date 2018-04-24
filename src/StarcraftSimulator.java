import java.util.*;

public class StarcraftSimulator {

    public static void main(String[] args) {


        ArrayList<State> currentStates = new ArrayList<>();
        ArrayList<State> possibleNextStates = new ArrayList<>();

        ArrayList<Goal> goals = new ArrayList<>();

        Goal goal = new Goal(Goal.unitsRequired(Integer.parseInt(args[0])));

        HashMap<Integer, LinkedHashMap<Integer, BuildTask>> games = new HashMap<>();

        for (int i=0; i<2; i++) {
            State game = new State(goal);

            games.put(getGameLength(game), game.getSignificantTasks());
            game = null;
        }

        int shortest = Collections.min(games.keySet());

        HashMap<Integer, BuildTask> optimal = games.get(shortest);

        System.out.println(shortest);

        //todo print output
     }

     public static State getLast(State s) {
         while (s.getChild() != null) {
             s = s.getChild();
         }

         return s;
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