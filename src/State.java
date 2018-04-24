import java.util.*;

public class State {
    private static final int MAX_TIME = 1911;

    private int time;
    private int minerals;
    private int gas;
    //private int accumulatedMinerals;
    //private int accumulatedGas;
    private int mineralSlots;
    private int gasSlots;
    //private HashMap<Constructable, Integer> units;
    //private HashMap<Constructable, Integer> buildings;
    private Set<Constructable> required = new HashSet<>();
    private HashMap<Constructable, Integer> constructs;
    private HashMap<ProbeTask, Integer> probes;
    private ArrayList<Constructable> activeBuildings = new ArrayList<>();
    private ArrayList<BuildTask> buildQueue = new ArrayList<>();
    private State child;
    private LinkedHashMap<Integer, BuildTask> significantTasks = new LinkedHashMap<>(); //todo insert build into hashmap time, task

    public State(Goal goal) {
        this.time = 0;
        this.minerals = 50;
        this.gas = 0;
        this.mineralSlots = 24;
        this.gasSlots = 6;
        this.constructs = initialConstructs();
        this.probes = initialProbes();

        this.child = new State(this, goal);
    }

    public State(State state, Goal goal) {
        this.time = state.getTime() + 1;
        this.minerals = state.minerals;
        this.gas = state.gas;
        this.mineralSlots = state.mineralSlots;
        this.gasSlots = state.gasSlots;
        this.constructs = state.constructs;
        this.probes = state.probes;
        this.activeBuildings = state.activeBuildings;
        this.buildQueue = state.buildQueue;
        this.significantTasks = state.significantTasks;
        this.gatherMinerals();
        this.gatherGas();

        this.tickQueue();

        if (!this.goalReached(goal) && this.time < MAX_TIME) {
            this.child = nextState(goal);
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

    public ArrayList<BuildTask> getBuildQueue() {
        return buildQueue;
    }

    public State nextState(Goal goal) {
        List<Constructable> constructs = goal.getBuildingsRequired();

        for (Map.Entry<Constructable, Integer> unit : goal.getUnitsRequired().entrySet()) {
            constructs.add(unit.getKey());
        }

        Random random = new Random();

            int index = random.nextInt(constructs.size() - 1);

            Constructable randomConstruct = constructs.get(index);
            if (randomConstruct.canAndShouldBeBuilt(this, goal)) {
                buildConstruct(randomConstruct);
            }

        State state = new State(this, goal);

        return state;
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
        significantTasks.put(time, bt);
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
        for (Map.Entry<Constructable, Integer> goalSet : goal.getUnitsRequired().entrySet()) {
            if (goalSet.getValue() > constructs.getOrDefault(goalSet.getKey(), 0)) {
                return false;
            }
        }
        return true;
    }

    public int getMinerals() {
        return minerals;
    }

    public int getGas() {
        return gas;
    }

    public HashMap<Constructable, Integer> getConstructs() {
        return constructs;
    }

    public ArrayList<Constructable> getActiveBuildings() {
        return activeBuildings;
    }

    public State getChild() {
        return child;
    }

    public LinkedHashMap<Integer, BuildTask> getSignificantTasks() {
        return significantTasks;
    }
}
