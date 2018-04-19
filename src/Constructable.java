import java.util.Map;

public enum Constructable {
    NEXUS(100, 400, 0, new Constructable[0], null),
    PYLON(25, 100, 0, new Constructable[0], null),
    ASSIMILATOR(30, 75, 0, new Constructable[0], null),
    GATEWAY(65, 150, 0, new Constructable[]{PYLON}, null),
    CYBERNETICS_CORE(50, 150, 0, new Constructable[]{PYLON, GATEWAY}, null),
    ROBOTICS_FACILITY(65, 200, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, null),
    STARGATE(60, 150, 150, new Constructable[]{PYLON, CYBERNETICS_CORE}, null),
    TWILIGHT_COUNCIL(50, 150, 100, new Constructable[]{PYLON, CYBERNETICS_CORE}, null),
    TEMPLAR_ARCHIVES(50, 150, 200, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, null),
    DARK_SHRINE(100, 100, 250, new Constructable[]{PYLON, TWILIGHT_COUNCIL}, null),
    ROBOTICS_BAY(65, 200, 200, new Constructable[]{PYLON, ROBOTICS_FACILITY}, null),
    FLEET_BEACON(60, 300, 200, new Constructable[]{PYLON, STARGATE}, null),

    PROBE(17, 50, 0, new Constructable[]{NEXUS}, NEXUS),
    ZEALOT(38, 100, 0, new Constructable[]{GATEWAY}, GATEWAY),
    STALKER(42, 125, 50, new Constructable[]{GATEWAY, CYBERNETICS_CORE}, GATEWAY),
    SENTRY(37, 50, 100, new Constructable[]{GATEWAY}, GATEWAY),
    OBSERVER(40, 25, 75, new Constructable[]{ROBOTICS_FACILITY}, ROBOTICS_FACILITY),
    IMMORTAL(55, 250, 100, new Constructable[]{ROBOTICS_FACILITY}, ROBOTICS_FACILITY,
    PHOENIX(35, 150, 100, new Constructable[]{STARGATE}, STARGATE),
    VOID_RAY(60, 250, 150, new Constructable[]{STARGATE}, STARGATE),
    COLOSSUS(75, 300, 200, new Constructable[]{ROBOTICS_FACILITY, ROBOTICS_BAY}, ROBOTICS_FACILITY),
    HIGH_TEMPLAR(55, 50, 150, new Constructable[]{GATEWAY, TEMPLAR_ARCHIVES}, GATEWAY),
    DARK_TEMPLAR(55, 125, 125, new Constructable[]{GATEWAY, DARK_SHRINE}, GATEWAY),
    CARRIER(120, 350, 250, new Constructable[]{STARGATE, FLEET_BEACON}, STARGATE);

    private final int buildTime;
    private final int mineralCost;
    private final int gasCost;
    private final Constructable[] dependencies;

    Constructable(int buildTime, int mineralCost, int gasCost, Constructable[] dependencies, Constructable builtFrom) {
        this.buildTime = buildTime;
        this.mineralCost = mineralCost;
        this.gasCost = gasCost;
        this.dependencies = dependencies;
    }

    public Constructable[] getDependencies() {
        return dependencies;
    }

    public boolean dependenciesExist(Game game) {
        int matching = 0;
        for (Constructable c : dependencies) {
            for (Map.Entry<Constructable, Integer> b : game.getBuildings().entrySet()) {
                if (b.getKey() == c) {
                    matching++;
                }
            }
        }
        return matching == dependencies.length;
    }

    public boolean resourcesAvailable(Game game) {
        return game.getMinerals() > mineralCost && game.getGas() > gasCost;
    }

}
