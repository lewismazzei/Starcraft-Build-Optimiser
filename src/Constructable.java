import java.util.Map;
import java.util.Optional;

public enum Constructable {
    //BUILDINGS
    NEXUS(100, 400, 0, new Constructable[0], Optional.empty(), Optional.of(1)),
    PYLON(25, 100, 0, new Constructable[0], Optional.empty(), Optional.of(1)),
    ASSIMILATOR(30, 75, 0, new Constructable[0], Optional.empty(), Optional.of(2)),
    GATEWAY(65, 150, 0, new Constructable[]{PYLON}, Optional.empty(), Optional.of(2)),
    CYBERNETICS_CORE(50, 150, 0, new Constructable[]{PYLON, GATEWAY}, Optional.empty(), Optional.of(1)),
    ROBOTICS_FACILITY(65, 200, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty(), Optional.of(3)),
    STARGATE(60, 150, 150, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty(), Optional.of(1)),
    TWILIGHT_COUNCIL(50, 150, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty(), Optional.of(1)),
    TEMPLAR_ARCHIVES(50, 150, 200, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, Optional.empty(), Optional.of(1)),
    DARK_SHRINE(100, 100, 250, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, Optional.empty(), Optional.of(1)),
    ROBOTICS_BAY(65, 200, 200, new Constructable[]{PYLON, ROBOTICS_FACILITY}, Optional.empty(), Optional.of(1)),
    FLEET_BEACON(60, 300, 200, new Constructable[]{PYLON, STARGATE}, Optional.empty(), Optional.of(1)),
    //UNITS
    PROBE(17, 50, 0, new Constructable[]{NEXUS}, Optional.of(NEXUS), Optional.empty()),
    ZEALOT(38, 100, 0, new Constructable[]{GATEWAY}, Optional.of(GATEWAY), Optional.empty()),
    STALKER(42, 125, 50, new Constructable[]{GATEWAY, CYBERNETICS_CORE}, Optional.of(GATEWAY), Optional.empty()),
    SENTRY(37, 50, 100, new Constructable[]{GATEWAY}, Optional.of(GATEWAY), Optional.empty()),
    OBSERVER(40, 25, 75, new Constructable[]{ROBOTICS_FACILITY}, Optional.of(ROBOTICS_FACILITY), Optional.empty()),
    IMMORTAL(55, 250, 100, new Constructable[]{ROBOTICS_FACILITY}, Optional.of(ROBOTICS_FACILITY), Optional.empty()),
    PHOENIX(35, 150, 100, new Constructable[]{STARGATE}, Optional.of(STARGATE), Optional.empty()),
    VOID_RAY(60, 250, 150, new Constructable[]{STARGATE}, Optional.of(STARGATE), Optional.empty()),
    COLOSSUS(75, 300, 200, new Constructable[]{ROBOTICS_FACILITY, ROBOTICS_BAY}, Optional.of(ROBOTICS_FACILITY), Optional.empty()),
    HIGH_TEMPLAR(55, 50, 150, new Constructable[]{GATEWAY, TEMPLAR_ARCHIVES}, Optional.of(GATEWAY), Optional.empty()),
    DARK_TEMPLAR(55, 125, 125, new Constructable[]{GATEWAY, DARK_SHRINE}, Optional.of(GATEWAY), Optional.empty()),
    CARRIER(120, 350, 250, new Constructable[]{STARGATE, FLEET_BEACON}, Optional.of(STARGATE), Optional.empty());
    //ATTRIBUTES
    private final int buildTime;
    private final int mineralCost;
    private final int gasCost;
    private final Constructable[] dependencies;
    private final Optional<Constructable> builtFrom;
    private final Optional<Integer> cap;
    //CONSTRUCTOR
    Constructable(int buildTime, int mineralCost, int gasCost, Constructable[] dependencies, Optional<Constructable> builtFrom, Optional<Integer> cap) {
        this.buildTime = buildTime;
        this.mineralCost = mineralCost;
        this.gasCost = gasCost;
        this.dependencies = dependencies;
        this.builtFrom = builtFrom;
        this.cap = cap;
    }
    //check to see whether dependencies exist
    public boolean dependenciesExist(State state) {
        //no dependencies originally
        int matching = 0;
        //check for the dependencies for the constructable
        for (Constructable dependency : dependencies) {
            //check the constructs currently present in the game
            for (Map.Entry<Constructable, Integer> construct : state.getConstructs().entrySet()) {
                //check if it's one of the dependencies that we need
                if (construct.getKey() == dependency) {
                    //increment the number of matching dependencies
                    matching++;
                }
            }
        }
        //return true if the number dependencies matches the number of matching dependencies
        return matching == dependencies.length;
    }

    //check to see whether resources are available
    public boolean resourcesAvailable(State state) {
        return state.getMinerals() >= mineralCost && state.getGas() >= gasCost;
    }

    //check to see whether construct is unit
    public boolean isUnit() {
        return this.builtFrom.isPresent();
    }

    public boolean canAndShouldBeBuilt(State state, Goal goal) {
        //if the dependencies and resources exist...
        if (dependenciesExist(state) && resourcesAvailable(state)) {
            //...and the construct is a unit...
            if (isUnit()) {
                //...and the building required to build it is inactive...
                if (state.getAvailableBuldings().contains(this.builtFrom.get())) {
                    //(special case if the unit is a probe as there is a cap for numbers of probes not a required amount)
                    if (this == Constructable.PROBE) {
                        //if there is any kind of slot available for a probe (if there only gas slots left then there needs to be the necessary assimilators...
                        if (state.getMineralSlots() > 0 ||
                                state.getGasSlots() >= 1 && state.getGasSlots() <= 3 && state.getConstructs().getOrDefault(Constructable.ASSIMILATOR, 0) >= 2 ||
                                state.getGasSlots() >= 4 && state.getGasSlots() <= 6 && state.getConstructs().getOrDefault(Constructable.ASSIMILATOR, 0) >= 1) {
                            //then it can and should be built
                            return true;
                        }
                    }
                //(for any other unit)
                } else {
                    long beingBuilt = state.getBuildQueue().stream().filter(bt -> bt.getConstructable() == this).count();
                    //...and the number of that units already built plus the ones currently being built is less than the required amount...
                    if (state.getConstructs().getOrDefault(this, 0) + beingBuilt < goal.getUnitsRequired().getOrDefault(this, 0)) {
                        //...then it can and should be built
                        return true;
                    }
                }
            //if the construct is a building...
            } else {
                long beingBuilt = state.getBuildQueue().stream().filter(bt -> bt.getConstructable() == this).count();
                //...and and the number of that buildings already built plus the ones currently being built is less than the cap
                if (state.getConstructs().getOrDefault(this, 0) + beingBuilt < this.getCap().get()) {
                    //...then it can and should be built
                    return true;
                }
            }
        }
        //if true is never never returned then for some reason the construct can't or shouldn't be be built
        return false;
    }

    //GETTERS
    public int getMineralCost() {
        return mineralCost;
    }

    public int getGasCost() {
        return gasCost;
    }

    public int getBuildTime() {
        return buildTime;
    }

    public Optional<Constructable> getBuiltFrom() {
        return builtFrom;
    }

    public Optional<Integer> getCap() {
        return cap;
    }
}
