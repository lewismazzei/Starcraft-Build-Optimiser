import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StarcraftSimulator {

    public static void main(String[] args) {
        Constructable construct = Constructable.DARK_SHRINE;
        Arrays.stream(construct.getDependencies()).forEach(System.out::println);

        Game root = new Game();

        ArrayList<Game> currlayer = new ArrayList<>();
        ArrayList<Game> nextlayer = new ArrayList<>();

        currlayer.add(root);

        for (int goal = 1; goal < 6; goal++) {
            boolean goalReached = false;
            while (!goalReached) {








            }
        }


    }
}
