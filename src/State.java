import java.util.ArrayList;
import java.util.HashMap;

public class State {
    private int time;
    private int minerals;
    private int gas;
    //private int accumulatedMinerals;
    //private int accumulatedGas;
    private int mineralSlots;
    private int gasSlots;
    //private HashMap<Constructable, Integer> units;
    //private HashMap<Constructable, Integer> buildings;
    private HashMap<Constructable, Integer> constructs;
    private HashMap<ProbeTask, Integer> probes;
    private State parent;

    public State() {
        this.time = 0;
        this.minerals = 50;
        this.gas = 0;
        this.mineralSlots = 24;
        this.gasSlots = 6;
        this.constructs = initialConstructs();
        this.probes = initialProbes();
    }

    //public State(int time, int minerals, int gas, HashMap<Constructable, Integer> constructs) {
    //    this.time = time;
    //    this.minerals = minerals;
    //    this.gas = gas;
    //    this.constructs = constructs;
    //}

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

    private HashMap<ProbeTask, Integer> initialProbes() {
        HashMap<ProbeTask, Integer> probes = new HashMap<>();
        probes.put(ProbeTask.MINERAL_MINING, 6);
        return probes;
    }

    public ArrayList<State> possibleNextStates(Goal goal) {
        ArrayList<State> possibleNextStates = new ArrayList<>();

            try {
                //IF next construct in stack can be built
                //possibleNextStates.addAll(buildConstruct(NEXT CONSTRUCT TO BE BUILT));
                //ELSE
                for (Constructable construct : Constructable.values()) {
                    if (construct.dependenciesExist(this) && construct.resourcesAvailable(this)) {
                        buildConstruct(construct);
                    }
                }


            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        //}
        return possibleNextStates;
    }

    public State gatherMinerals(State State) throws CloneNotSupportedException {
        State nextState = (State) State.clone();
        nextState.parent = State;
        if (mineralSlots > 0) { //todo mine until we have the correct number of resources to accomplish the complete goal.
            int mining = probes.getOrDefault(ProbeTask.MINERAL_MINING, 0);
            if (mining <= 16) {
                nextState.minerals += mining * 0.68;
            } else {
                nextState.minerals += (16 * 0.68) + (0.33 * (mining - 16));
            }
        } else {
            return null;
        }
        return nextState;
    }

    public State gatherGas() throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.parent = this;
        nextState.useProbe();
        nextState.gas += 0.63;

        return nextState;
    }

    public void buildConstruct(Constructable c) throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.constructs.put(c, constructs.getOrDefault(c, 0) + 1);
        if (c.isUnit()) {

        }
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

    public HashMap<Constructable, Integer> getConstructs() {
        return constructs;
    }
}
