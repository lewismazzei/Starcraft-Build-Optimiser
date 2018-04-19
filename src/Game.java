import org.omg.PortableInterceptor.NON_EXISTENT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private int time;
    private int minerals;
    private int gas;
    private int mineralPatches;
    private int gasGeysers;
    private HashMap<Constructable, Integer> units;
    private HashMap<Constructable, Integer> buildings;
    private Game parent;

    private final int GOAL_1 = 1;
    private final int GOAL_2 = 2;
    private final int GOAL_3 = 3;
    private final int GOAL_4 = 4;
    private final int GOAL_5 = 5;

    public Game() {
        this.time = 0;
        this.minerals = 50;
        this.gas = 0;
        this.mineralPatches = 24;
        this.gasGeysers = 6;
        this.units = initialUnits();
        this.buildings = initialBuildings();
    }

    public Game(int time, int minerals, int gas, HashMap<Constructable, Integer> buildings, HashMap<Constructable, Integer> units) {
        this.time = time;
        this.minerals = minerals;
        this.gas = gas;
        this.units = units;
        this.buildings = buildings;
    }

    private HashMap<Constructable, Integer> initialUnits() {
        HashMap<Constructable, Integer> units = new HashMap<>();
        units.put(Constructable.PROBE, 6);
        return units;
    }

    private HashMap<Constructable, Integer> initialBuildings() {
        HashMap<Constructable, Integer> buildings = new HashMap<>();
        buildings.put(Constructable.NEXUS, 1);
        return buildings;
    }

    public ArrayList<Game> nextLayer(int goal) {
        ArrayList<Game> nextLayer = new ArrayList<>();
        ArrayList<Constructable> redundant = new ArrayList<>();
        switch (goal) {
            case GOAL_1:
                redundant.add(Constructable.STARGATE);
                redundant.add(Constructable.TWILIGHT_COUNCIL);
                break;
            case GOAL_2:
                redundant.add(Constructable.ROBOTICS_FACILITY);
                redundant.add(Constructable.TWILIGHT_COUNCIL);
                redundant.add(Constructable.FLEET_BEACON);
            case GOAL_3:
                redundant.add(Constructable.ROBOTICS_FACILITY);
                redundant.add(Constructable.TWILIGHT_COUNCIL);
                redundant.add(Constructable.FLEET_BEACON);
            case GOAL_4:
                redundant.add(Constructable.STARGATE);
                redundant.add(Constructable.ROBOTICS_FACILITY);
            case GOAL_5:
                redundant.add(Constructable.ROBOTICS_BAY);
                redundant.add(Constructable.TEMPLAR_ARCHIVES);
        }
        //if (units.get(Constructable.PROBE) == 0) {
        //    nextLayer.addAll(buildUnits());
        //} else {
            nextLayer.add(gatherMinerals(this));
            nextLayer.add(gatherGas(this));
            nextLayer.addAll(buildUnits());
            nextLayer.addAll(constructBuildings());
        //}
        return nextLayer;
    }

    private Game gatherMinerals(Game game) {
        Game nextGame = new Game();
        try {
            nextGame = (Game) game.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        nextGame.parent = game;
        if (mineralPatches > 0) {
            nextGame.useProbe();
            if (mineralPatches > 8) {
                nextGame.minerals += 0.68;
            } else {
                nextGame.minerals += 0.33;
            }
            nextGame.mineralPatches -= 1;
        } else {
            return null;
        }
        return nextGame;
    }

    private Game gatherGas(Game game) {
        switch (game.buildings.getOrDefault(Constructable.ASSIMILATOR, 0)) {
            case 0:
                this.buildConstructable(Constructable.ASSIMILATOR);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                System.out.println("Impossible number of assimilators");
                System.exit(0);
        }
        return nextGame;
    }

    private void buildConstructable(Constructable c) {
        Game nextGame = new Game();

    }

    private void useProbe() {
        this.units.put(Constructable.PROBE, this.units.get(Constructable.PROBE) - 1);
    }

    private void buildUnit(Constructable c) {
        units.put(c, units.getOrDefault(c, 0) + 1);

    }

    public int getMinerals() {
        return minerals;
    }

    public int getGas() {
        return gas;
    }

    public HashMap<Constructable, Integer> getBuildings() {
        return buildings;
    }

    public HashMap<Constructable, Integer> getUnits() {
        return units;
    }
}
