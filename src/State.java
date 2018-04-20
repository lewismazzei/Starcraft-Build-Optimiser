import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private int time;
    private int currentMinerals;
    private int currentGas;
    private int accumulatedMinerals;
    private int accumulatedGas;
    private int mineralPatches;
    private int gasGeysers;
    //private HashMap<Constructable, Integer> units;
    //private HashMap<Constructable, Integer> buildings;
    private HashMap<Constructable, Integer> constructs;
    private State parent;


    public State() {
        this.time = 0;
        this.currentMinerals = 50;
        this.currentGas = 0;
        this.mineralPatches = 24;
        this.gasGeysers = 6;
        this.constructs = initialConstructs();
    }

    public State(int time, int currentMinerals, int currentGas, HashMap<Constructable, Integer> constructs) {
        this.time = time;
        this.currentMinerals = currentMinerals;
        this.currentGas = currentGas;
        this.constructs = constructs;
    }

    //private HashMap<Constructable, Integer> initialUnits() {
    //    HashMap<Constructable, Integer> units = new HashMap<>();
    //    units.put(Constructable.PROBE, 6);
    //    return units;
    //}
    //
    //private HashMap<Constructable, Integer> initialBuildings() {
    //    HashMap<Constructable, Integer> buildings = new HashMap<>();
    //    buildings.put(Constructable.NEXUS, 1);
    //    return buildings;
    //}

    private HashMap<Constructable, Integer> initialConstructs() {
        HashMap<Constructable, Integer> constructs = new HashMap<>();
        constructs.put(Constructable.PROBE, 6);
        constructs.put(Constructable.NEXUS, 1);
        return constructs;
    }

    public ArrayList<State> getPossibleActions(int goal) {
        ArrayList<State> nextActions = new ArrayList<>();

        //if (units.get(Constructable.PROBE) == 0) {
        //    getPossibleActions.addAll(buildUnits());
        //} else {
            try {
                //IF next construct in stack can be built
                //nextActions.addAll(buildConstruct(NEXT CONSTRUCT TO BE BUILT));
                //ELSE
                nextActions.add(gatherMinerals());
                nextActions.add(gatherGas());


            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        //}
        return nextActions;
    }

    private State gatherMinerals() throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.parent = this;
        if (mineralPatches > 0) {
            nextState.useProbe();
            if (mineralPatches > 8) {
                nextState.currentMinerals += 0.68;
            } else {
                nextState.currentMinerals += 0.33;
            }
            nextState.mineralPatches -= 1;
        } else {
            return null;
        }
        return nextState;
    }

    private State gatherGas() throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.parent = this;
        nextState.useProbe();
        nextState.currentGas += 0.63;

        return nextState;
    }

    private void buildConstruct(Constructable c) throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.constructs.put(c, constructs.getOrDefault(c, 0) + 1);
        if (c.isUnit())

    }

    private void useProbe() {
        this.units.put(Constructable.PROBE, this.units.get(Constructable.PROBE) - 1);
    }

    private void buildUnit(Constructable c) {
        units.put(c, units.getOrDefault(c, 0) + 1);

    }

    public int getCurrentMinerals() {
        return currentMinerals;
    }

    public int getCurrentGas() {
        return currentGas;
    }

    public HashMap<Constructable, Integer> getBuildings() {
        return buildings;
    }

    public HashMap<Constructable, Integer> getUnits() {
        return units;
    }

    public HashMap<Constructable, Integer> getConstructs() {
        return constructs;
    }
}
