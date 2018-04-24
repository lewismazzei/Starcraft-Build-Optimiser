import java.util.*;

public class StarcraftSimulator {
    public static void main(String[] args) {
        //get goal number from user
        Goal goal = new Goal(Goal.unitsRequired(Integer.parseInt(args[0])));
        //hashmap to store games (Represented by an integer time to reach goal and a hashmap with timestamps for each action taken)
        HashMap<Integer, LinkedHashMap<Integer, BuildTask>> games = new HashMap<>();
        //create a number of randomly generated games
        for (int i = 0; i < 10000; i++) {
            //randomly generate game
            State game = new State(goal);
            //put game's completion time along with its timestamps into map
            games.put(getGameLength(game), game.getTimeStamps());
        }
        //find the shortest game
        int shortest = Collections.min(games.keySet());
        //create a map of timestamps and build tasks for shortest game
        HashMap<Integer, BuildTask> optimal = games.get(shortest);
        //print out build tasks and timestamps to stdout
        for (Map.Entry<Integer, BuildTask> action : optimal.entrySet()) {
            System.out.println(action.getValue().getConstructable() + " @ " + action.getKey() + " ticks");
        }
     }

     //get length of game
     public static int getGameLength(State s) {
        //base time
        int time = 0;
        // until we reach the end of the linked list
        while (s.getChild() != null) {
            //increment time
            time++;
            //move to next node
            s = s.getChild();
        }
        //return the time
        return time;
     }
}