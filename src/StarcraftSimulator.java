import java.util.*;

public class StarcraftSimulator {

    public static void main(String[] args) {

        Goal goal = new Goal(Goal.unitsRequired(Integer.parseInt(args[0])));

        HashMap<Integer, LinkedHashMap<Integer, BuildTask>> games = new HashMap<>();

        for (int i=0; i<100000; i++) {
            State game = new State(goal);

            games.put(getGameLength(game), game.getSignificantActions());

            //for (Map.Entry<Integer, BuildTask> entry : game.getSignificantActions().entrySet()) {
            //    System.out.println(entry.getValue().getConstructable());
            //}

            //for (String event : game.getEvents()) {
            //    System.out.println(event);
            //}

        }

        int shortest = Collections.min(games.keySet());

        HashMap<Integer, BuildTask> optimal = games.get(shortest);

        for (Map.Entry<Integer, BuildTask> action : optimal.entrySet()) {
            System.out.println(action.getValue().getConstructable() + " @ " + action.getKey() + " ticks");
        }
     }

     //public static State getLast(State s) {
     //    while (s.getChild() != null) {
     //        s = s.getChild();
     //    }
     //
     //    return s;
     //}

     public static int getGameLength(State s) {
        int time = 0;
        while (s.getChild() != null) {
            time++;
            s = s.getChild();
        }

        return time;
     }
}