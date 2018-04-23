import com.sun.source.tree.IfTree;

import java.util.Map;
import java.util.Optional;

public enum Constructable {
    NEXUS(100, 400, 0, new Constructable[0], Optional.empty(), Optional.of(1)),
    PYLON(25, 100, 0, new Constructable[0], Optional.empty(), Optional.of(1)),
    ASSIMILATOR(30, 75, 0, new Constructable[0], Optional.empty(), Optional.of(1)),
    GATEWAY(65, 150, 0, new Constructable[]{PYLON}, Optional.empty(), Optional.of(1)),
    CYBERNETICS_CORE(50, 150, 0, new Constructable[]{PYLON, GATEWAY}, Optional.empty(), Optional.of(1)),
    ROBOTICS_FACILITY(65, 200, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty(), Optional.of(1)),
    STARGATE(60, 150, 150, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty(), Optional.of(1)),
    TWILIGHT_COUNCIL(50, 150, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty(), Optional.of(1)),
    TEMPLAR_ARCHIVES(50, 150, 200, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, Optional.empty(), Optional.of(1)),
    DARK_SHRINE(100, 100, 250, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, Optional.empty(), Optional.of(1)),
    ROBOTICS_BAY(65, 200, 200, new Constructable[]{PYLON, ROBOTICS_FACILITY}, Optional.empty(), Optional.of(1)),
    FLEET_BEACON(60, 300, 200, new Constructable[]{PYLON, STARGATE}, Optional.empty(), Optional.of(1)),

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

    private final int buildTime;
    private final int mineralCost;
    private final int gasCost;
    private final Constructable[] dependencies;
    private final Optional<Constructable> builtFrom;
    private final Optional<Integer> cap;

    Constructable(int buildTime, int mineralCost, int gasCost, Constructable[] dependencies, Optional<Constructable> builtFrom, Optional<Integer> cap) {
        this.buildTime = buildTime;
        this.mineralCost = mineralCost;
        this.gasCost = gasCost;
        this.dependencies = dependencies;
        this.builtFrom = builtFrom;
        this.cap = cap;
    }

    public boolean dependenciesExist(State state) {
        int matching = 0;
        for (Constructable c : dependencies) {
            for (Map.Entry<Constructable, Integer> b : state.getBuildings().entrySet()) {
                if (b.getKey() == c) {
                    matching++;
                }
            }
        }
        return matching == dependencies.length;
    }

    public boolean resourcesAvailable(State state) {
        return state.getMinerals() > mineralCost && state.getGas() > gasCost;
    }

    public boolean isUnit() {
        return this.builtFrom.isPresent();
    }

    public Constructable[] getDependencies() {
        return dependencies;
    }

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

    public boolean canAndShouldBeBuilt(State state, Goal goal) {
        if (dependenciesExist(state) && resourcesAvailable(state)) {
            if (isUnit()) {
                if (!state.getActiveBuildings().contains(this.builtFrom.get()) && state.getUnits().get(this) < goal.getUnitsRequired().get(this)) {
                    state.buildConstruct(this);
                }
            } else {
                if (this.getCap().isPresent() && state.getBuildings().get(this) < this.getCap().get()) {
                    state.buildConstruct(this);
                }
            }
        }
    }
}
