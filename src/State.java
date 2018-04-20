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
    private ArrayList<Constructable> activeBuildings;
    private ArrayList<BuildTask> buildQueue = new ArrayList<>();
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
                    State possibleNextState = (State) this.clone();
                    if (construct.canAndShouldBeBuilt(this, goal)) {
                        possibleNextState = buildConstruct(construct);
                    }
                    possibleNextState = possibleNextState.gatherMinerals();
                    possibleNextStates.add(possibleNextState);
                }


            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        //}
        return possibleNextStates;
    }

    public State gatherMinerals() throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.parent = this;
        if (mineralSlots > 0) {
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

    public State buildConstruct(Constructable construct) throws CloneNotSupportedException {
        State nextState = (State) this.clone();
        nextState.parent = this;
        if (construct.isUnit()) {
            nextState.activeBuildings.add(construct);
            nextState.constructs.put(construct, constructs.getOrDefault(construct, 0) - 1);
        }
        //TODO start timer, only put into map once timer has finished
        //nextState.constructs.put(c, constructs.getOrDefault(c, 0) + 1);
        return nextState;
    }

    private void tickQueue() {
        for (int i=0; i<buildQueue.size(); i++) {
            BuildTask bt = buildQueue.get(i);
            int ticksLeft = bt.tick();
            if (ticksLeft == 0) {
                constructs.put(bt.getConstructable(), constructs.getOrDefault(bt.getConstructable(), 0) + 1); //todo not sure if this should be construct or child
                buildQueue.remove(i);
            }
        }
    }

    private void useProbe() {
        this.units.put(Constructable.PROBE, this.units.get(Constructable.PROBE) - 1);
    }

    private void buildUnit(Constructable c) {
        buildQueue.add(new BuildTask(c));
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

    public ArrayList<Constructable> getActiveBuildings() {
        return activeBuildings;
    }
}
