import java.util.*;

public class State {
    //CONSTANTS
    private static final int MAX_TIME = 1900;
    private static final int NUM_OF_MINERAL_PATCHES = 8;
    private static final double OPTIMAL_MINING_RATE = 0.68;
    private static final double REDUCED_MINING_RATE = 0.33;
    public static final double GAS_GATHERING_RATE = 0.63;

    //ATTRIBUTES
    private int time;
    private double minerals;
    private double gas;
    private int mineralSlots;
    private int gasSlots;
    private HashMap<Constructable, Integer> constructs;
    private HashMap<ProbeTask, Integer> probes;
    private ArrayList<Constructable> availableBuldings = new ArrayList<>();
    private ArrayList<BuildTask> buildQueue = new ArrayList<>();
    private State child;
    private LinkedHashMap<Integer, BuildTask> timeStamps = new LinkedHashMap<>();
    private ArrayList<String> events = new ArrayList<>();

    //INITIAL CONSTRUCTOR
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

    //ITERATIVE CONSTRCUTOR
    public State(State state, Goal goal) {
        //increment time
        this.time = state.getTime() + 1;
        //update attributes
        this.minerals = state.minerals;
        this.gas = state.gas;
        this.mineralSlots = state.mineralSlots;
        this.gasSlots = state.gasSlots;
        this.constructs = state.constructs;
        this.probes = state.probes;
        this.availableBuldings = state.availableBuldings;
        this.buildQueue = state.buildQueue;
        this.timeStamps = state.timeStamps;
        this.events = state.events;
        //update
        this.mineMinerals();
        this.gatherGas();
        this.tickQueue();
        if (!this.goalReached(goal) && this.time < MAX_TIME) {
            this.child = nextState(goal);
        } else {
            this.child = null;
        }
    }

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

    //randomly pick a next state for the game
    public State nextState(Goal goal) {
        //list that stores all the constructs that can currently be afforded and that adhere to certain heurisitics
        ArrayList<Constructable> constructsThatCanAndShouldBeBuilt = new ArrayList<>();
        //loop through every construct
        for (Constructable construct : Constructable.values()) {
            //if that construct can currently be afforded and adheres to certain heurisitics
            if (construct.canAndShouldBeBuilt(this, goal)) {
                //add it to list
                constructsThatCanAndShouldBeBuilt.add(construct);
            }
        }
        //if there is a construct in the list
        if (constructsThatCanAndShouldBeBuilt.size() > 0) {
            int index;
            //if there is just one set the index to 0
            if (constructsThatCanAndShouldBeBuilt.size() == 1) {
                index = 0;
            //if there is multiple
            } else {
                //pick a random index
                Random random = new Random();
                index = random.nextInt(constructsThatCanAndShouldBeBuilt.size() - 1);
            }
            //use index to select a certain construct
            Constructable randomConstructThatCanAndShouldBeBuilt = constructsThatCanAndShouldBeBuilt.get(index);
            //build the construct
            buildConstruct(randomConstructThatCanAndShouldBeBuilt);
        }
        //return a new state with the new building constructed
        return new State(this, goal);
    }

    //increment mineral count
    public void mineMinerals() {
        //get the number of probes mining minerals
        int probesMining = probes.getOrDefault(ProbeTask.MINERAL_MINING, 0);
        //if there aren't two probes on all mineral patches then rate will be optimal (i.e. you would assing a probe to a patch with a first or second slot)
        if (probesMining <= NUM_OF_MINERAL_PATCHES * 2) {
            minerals += probesMining * OPTIMAL_MINING_RATE;
        //if all mineral patches have at least two probes on them then the rate for the next probes will be slower
        } else {
            minerals += (16 * OPTIMAL_MINING_RATE) + (REDUCED_MINING_RATE * (probesMining - 16));
        }
    }

    //increment gas count
    public void gatherGas() {
        //get the number of probes gathering gas
        int probesGathering = probes.getOrDefault(ProbeTask.GAS_GATHERING, 0);
        gas += probesGathering * GAS_GATHERING_RATE;

    }

    public void buildConstruct(Constructable construct) {
        //create new build task
        BuildTask bt = new BuildTask(construct);
        //add it to the build queue
        buildQueue.add(bt);
        //if (bt.getConstructable().isUnit()) {
        //    availableBuldings.(bt.getConstructable());
        //}
        //subtract minerals and gas from bank
        minerals -= construct.getMineralCost();
        gas -= construct.getGasCost();
        //add timestamp to list
        timeStamps.put(time, bt);
    }

    //update build queue for next tick
    private void tickQueue() {
        //for every task in the build queue
        for (int i = 0; i < buildQueue.size(); i++) {
            BuildTask bt = buildQueue.get(i);
            //if a construct has reached it's buildtime...
            if (bt.tick() == 0) {
                //...and the construct is a unit
                if (bt.getConstructable().isUnit()) {
                    //add the building it is built from to the inactive buildings list
                    availableBuldings.remove(bt.getConstructable().getBuiltFrom().get());
                    //if unit is a probe
                    if (bt.getConstructable() == Constructable.PROBE) {
                        //assign a random task to the probe
                        boolean miner = new Random().nextBoolean();
                        //if the randomly selected task is not possible to carry out...
                        if (miner && mineralSlots < 1 || !miner && gasSlots < 1) {
                            //...toggle the task
                            miner = !miner;
                        }
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
                //regardless of whether the construct is a unit or building, update the constructs map to reflect the new construct
                constructs.put(bt.getConstructable(), constructs.getOrDefault(bt.getConstructable(), 0) + 1);

                events.add("Finished building: " + bt.getConstructable() + " @ " + time + " ticks");

                //and remove it's build task from the queue
                buildQueue.remove(i);
            }
        }
    }

    //check to see whether goal has been reached
    private boolean goalReached(Goal goal) {
        //for each unit required in goal
        for (Map.Entry<Constructable, Integer> goalSet : goal.getUnitsRequired().entrySet()) {
            //if there are still units required...
            if (goalSet.getValue() > constructs.getOrDefault(goalSet.getKey(), 0)) {
                //...goal has not been reached
                return false;
            }
        }
        //...if no more units are required then goal has been reached
        return true;
    }

    //GETTERS
    public double getMinerals() {
        return minerals;
    }

    public double getGas() {
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

    public LinkedHashMap<Integer, BuildTask> getTimeStamps() {
        return timeStamps;
    }

    public int getMineralSlots() {
        return mineralSlots;
    }

    public int getGasSlots() {
        return gasSlots;
    }

    public int getTime() {
        return time;
    }
}
