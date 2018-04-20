import java.util.Map;
import java.util.Optional;

public enum Constructable {
    NEXUS(100, 400, 0, new Constructable[0], Optional.empty()),
    PYLON(25, 100, 0, new Constructable[0], Optional.empty()),
    ASSIMILATOR(30, 75, 0, new Constructable[0], Optional.empty()),
    GATEWAY(65, 150, 0, new Constructable[]{PYLON}, Optional.empty()),
    CYBERNETICS_CORE(50, 150, 0, new Constructable[]{PYLON, GATEWAY}, Optional.empty()),
    ROBOTICS_FACILITY(65, 200, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty()),
    STARGATE(60, 150, 150, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty()),
    TWILIGHT_COUNCIL(50, 150, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, Optional.empty()),
    TEMPLAR_ARCHIVES(50, 150, 200, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, Optional.empty()),
    DARK_SHRINE(100, 100, 250, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, Optional.empty()),
    ROBOTICS_BAY(65, 200, 200, new Constructable[]{PYLON, ROBOTICS_FACILITY}, Optional.empty()),
    FLEET_BEACON(60, 300, 200, new Constructable[]{PYLON, STARGATE}, Optional.empty()),

    PROBE(17, 50, 0, new Constructable[]{NEXUS}, Optional.of(NEXUS)),
    ZEALOT(38, 100, 0, new Constructable[]{GATEWAY}, Optional.of(GATEWAY)),
    STALKER(42, 125, 50, new Constructable[]{GATEWAY, CYBERNETICS_CORE}, Optional.of(GATEWAY)),
    SENTRY(37, 50, 100, new Constructable[]{GATEWAY}, Optional.of(GATEWAY)),
    OBSERVER(40, 25, 75, new Constructable[]{ROBOTICS_FACILITY}, Optional.of(ROBOTICS_FACILITY)),
    IMMORTAL(55, 250, 100, new Constructable[]{ROBOTICS_FACILITY}, Optional.of(ROBOTICS_FACILITY)),
    PHOENIX(35, 150, 100, new Constructable[]{STARGATE}, Optional.of(STARGATE)),
    VOID_RAY(60, 250, 150, new Constructable[]{STARGATE}, Optional.of(STARGATE)),
    COLOSSUS(75, 300, 200, new Constructable[]{ROBOTICS_FACILITY, ROBOTICS_BAY}, Optional.of(ROBOTICS_FACILITY)),
    HIGH_TEMPLAR(55, 50, 150, new Constructable[]{GATEWAY, TEMPLAR_ARCHIVES}, Optional.of(GATEWAY)),
    DARK_TEMPLAR(55, 125, 125, new Constructable[]{GATEWAY, DARK_SHRINE}, Optional.of(GATEWAY)),
    CARRIER(120, 350, 250, new Constructable[]{STARGATE, FLEET_BEACON}, Optional.of(STARGATE));

    private final int buildTime;
    private final int mineralCost;
    private final int gasCost;
    private final Constructable[] dependencies;
    private final Optional<Constructable> builtFrom;

    Constructable(int buildTime, int mineralCost, int gasCost, Constructable[] dependencies, Optional<Constructable> builtFrom) {
        this.buildTime = buildTime;
        this.mineralCost = mineralCost;
        this.gasCost = gasCost;
        this.dependencies = dependencies;
        this.builtFrom = builtFrom;
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
}
