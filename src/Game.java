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
    private Game[] children;

    public Game() {
        this.time = 0;
        this.minerals = 50;
        this.gas = 0;
        this.mineralPatches = 8;
        this.gasGeysers = 2;
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

    public ArrayList<Game> nextLayer(int goal) throws CloneNotSupportedException {
        ArrayList<Game> nextLayer = new ArrayList<>();
        ArrayList<Constructable> redundant = new ArrayList<>();
        switch (goal) {
            case 1:
                redundant.add(Constructable.STARGATE);
                redundant.add(Constructable.TWILIGHT_COUNCIL);
                break;
            //TODO cases 2-5
        }
        if (units.get(Constructable.PROBE) == 0) {
            nextLayer.addAll(buildUnits());
        } else {
            nextLayer.add(gatherMinerals(this));
            nextLayer.addAll(gatherGas());
            nextLayer.addAll(buildUnits());
            nextLayer.addAll(constructBuildings());
        }
        return nextLayer;
    }

    private Game gatherMinerals(Game game) throws CloneNotSupportedException {
        Game nextGame = (Game) game.clone();
        if (mineralPatches > 0) {
            useProbe();
            nextGame.minerals += 1;
            nextGame.parent = game;
            //TODO add nextGame to children
        }

    }

    private void useProbe() {
        units.put(Constructable.PROBE, units.get(Constructable.PROBE) - 1);
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
