import java.util.*;

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
    private State child;

    public State() {
        this.time = 0;
        this.minerals = 50;
        this.gas = 0;
        this.mineralSlots = 24;
        this.gasSlots = 6;
        this.constructs = initialConstructs();
        this.probes = initialProbes();
    }

    public State(State state) {
        this.time = state.getTime() + 1;
        this.gatherMinerals();
        this.gatherGas();

        if (!this.goalReached(goal)) {
            this.child = new State(this);
        }
    }

    public int getTime() {
        return time;
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

        List<Constructable> constructs = Arrays.asList(Constructable.values());

        Random random = new Random();

        int index = random.nextInt(constructs.size());

        Constructable randomConstruct = constructs.get(index);

        if (randomConstruct.canAndShouldBeBuilt(this, goal)) {
            buildConstruct(randomConstruct);
        } else {
            //DO YOUR DEPENDANCY THING?
        }
        gatherMinerals();

        return possibleNextStates;
    }

    public void gatherMinerals() {
        if (mineralSlots > 0) {
            int mining = probes.getOrDefault(ProbeTask.MINERAL_MINING, 0);
            if (mining <= 16) {
                minerals += mining * 0.68;
            } else {
                minerals += (16 * 0.68) + (0.33 * (mining - 16));
            }
        }
    }

    public void gatherGas() {
        if (gasSlots > 0) {
            int gathering = probes.getOrDefault(ProbeTask.GAS_COLLECTION, 0);
            if (gathering <= 6) {
                gas += gathering * 0.63;
            } else {
                gas += (16 * 0.68) + (0.33 * (gathering - 4));
            }
        }
    }

    public void buildConstruct(Constructable construct) {
        BuildTask bt = new BuildTask(construct);
        if (bt.getConstructable().isUnit()) {
            activeBuildings.add(bt.getConstructable());
        }
        buildQueue.add(bt);
    }

    private void tickQueue() {
        for (int i = 0; i < buildQueue.size(); i++) {
            BuildTask bt = buildQueue.get(i);
            int ticksLeft = bt.tick();
            if (ticksLeft == 0) {
                if (bt.getConstructable().isUnit()) {
                    activeBuildings.remove(bt.getConstructable());
                }
                constructs.put(bt.getConstructable(), constructs.getOrDefault(bt.getConstructable(), 0) + 1); //todo not sure if this should be construct or child
                buildQueue.remove(i);
            }
        }
    }

    private boolean goalReached(Goal goal) {
        for (Map.Entry<Constructable, Integer> unit: this.getUnits().entrySet()) {
            if (unit.getValue() < goal.getUnitsRequired().get(unit.getKey())) {
                return false;
            }
        }
        return true;
    }

    //private void useProbe() {
    //    this.units.put(Constructable.PROBE, this.units.get(Constructable.PROBE) - 1);
    //}

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
