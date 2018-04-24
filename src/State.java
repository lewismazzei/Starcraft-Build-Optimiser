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
    private ArrayList<Constructable> availableBuldings = new ArrayList<>();
    private ArrayList<BuildTask> buildQueue = new ArrayList<>();
    private State child;
    private LinkedHashMap<Integer, BuildTask> significantActions = new LinkedHashMap<>(); //todo insert build into hashmap time, task
    private ArrayList<String> events = new ArrayList<>();

    public State(Goal goal) {
        this.time = 0;
        this.minerals = 50;
        this.gas = 0;
        this.mineralSlots = 24;
        this.gasSlots = 6;
        this.constructs = initialConstructs();
        this.probes = initialProbes();
        this.availableBuldings.add(Constructable.NEXUS);

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
        this.availableBuldings = state.availableBuldings;
        this.buildQueue = state.buildQueue;
        this.significantActions = state.significantActions;
        this.events = state.events;
        this.mineMinerals();
        this.gatherGas();

        this.tickQueue();

        if (!this.goalReached(goal) && this.time < MAX_TIME) {
            this.child = nextState(goal);
        } else {
            this.child = null;
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

        ArrayList<Constructable> constructsThatCanAndShouldBeBuilt = new ArrayList<>();

        for (Constructable construct : Constructable.values()) {
            if (construct.canAndShouldBeBuilt(this, goal)) {
                constructsThatCanAndShouldBeBuilt.add(construct);
            }
        }

        if (constructsThatCanAndShouldBeBuilt.size() > 0) {
            Random random = new Random();

            int index;

            if (constructsThatCanAndShouldBeBuilt.size() == 1) {
                index = 0;
            } else {
                index = random.nextInt(constructsThatCanAndShouldBeBuilt.size() - 1);
            }


            Constructable randomConstructThatCanAndShouldBeBuilt = constructsThatCanAndShouldBeBuilt.get(index);

            buildConstruct(randomConstructThatCanAndShouldBeBuilt);
        } else {
            events.add("Waited");
        }

        return new State(this, goal);
    }

    public void mineMinerals() {
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
            int gathering = probes.getOrDefault(ProbeTask.GAS_GATHERING, 0);
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
            availableBuldings.remove(bt.getConstructable());
        }
        buildQueue.add(bt);
        //significantActions.put(time, bt);
        events.add("Started building: " + bt.getConstructable());
    }

    private void tickQueue() {
        //for every task in the build queue
        for (int i = 0; i < buildQueue.size(); i++) {
            BuildTask bt = buildQueue.get(i);
            //tick
            int ticksLeft = bt.tick();
            //if a construct has reached it's buildtime...
            if (ticksLeft == 0) {
                //...and the construct is a unit
                if (bt.getConstructable().isUnit()) {
                    //add the building it is built from to the inactive buildings list
                    availableBuldings.add(bt.getConstructable().getBuiltFrom().get());
                    //if unit is a probe
                    if (bt.getConstructable() == Constructable.PROBE) {
                        //assign a random task to the probe
                        Random random = new Random();

                        boolean miner = random.nextBoolean();
                        //if the randomly selected task is not possible to carry out...
                        if (miner && mineralSlots < 1 || !miner && gasSlots < 1) {
                            //...toggle the task
                            miner = !miner;
                            //if it is assigned to mine minerals
                            if (miner) {
                                //then update the probes map to reflect this
                                probes.put(ProbeTask.MINERAL_MINING, probes.get(ProbeTask.MINERAL_MINING) + 1);
                                //and decrement numner of mineralSlots
                                mineralSlots -= 1;
                            //if it is assigned to gather gas
                            } else {
                                //then update the probes map to reflect this
                                probes.put(ProbeTask.GAS_GATHERING, probes.getOrDefault(ProbeTask.GAS_GATHERING, 0) + 1);
                                //and decrement number of gas slots
                                gasSlots -= 1;
                            }

                        }
                    }
                }
                //regardless of whether the construct is a unit or building, update the constructs map to reflect the new construct
                constructs.put(bt.getConstructable(), constructs.getOrDefault(bt.getConstructable(), 0) + 1);

                events.add("Finished building: " + bt.getConstructable());

                //and remove it's build task from the queue
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

    public ArrayList<Constructable> getAvailableBuldings() {
        return availableBuldings;
    }

    public State getChild() {
        return child;
    }

    public LinkedHashMap<Integer, BuildTask> getSignificantActions() {
        return significantActions;
    }

    public int getMineralSlots() {
        return mineralSlots;
    }

    public int getGasSlots() {
        return gasSlots;
    }

    public ArrayList<String> getEvents() {
        return events;
    }
}
