import org.omg.CORBA.UNKNOWN;

import java.util.*;

public class Goal {
    private HashMap<Constructable, Integer> unitsRequired = new HashMap<>();
    private HashSet<Constructable> buildingsRequired = new HashSet<>();
    private int mineralsRequired;
    private int gasRequired;

    public Goal(HashMap<Constructable, Integer> unitsRequired) {
        this.unitsRequired = unitsRequired;
        this.buildingsRequired = buildingsRequired();
        this.mineralsRequired = calcMineralsRequired(unitsRequired);
        this.gasRequired = calcGasRequired(unitsRequired);
    }

    //private int calcMineralsRequired() {
    //    int mineralCount = 0;
    //    for (Map.Entry<Constructable, Integer> entry : unitsRequired.entrySet()) {
    //        mineralCount += entry.getKey().getMineralCost() * entry.getValue();
    //    }
    //    return mineralCount;
    //}
    //
    //private int calcGasRequired() {
    //    int mineralCount = 0;
    //    for (Map.Entry<Constructable, Integer> entry : unitsRequired.entrySet()) {
    //        mineralCount += entry.getKey().getGasCost() * entry.getValue();
    //    }
    //    return mineralCount;
    //}

    public static HashMap<Constructable, Integer> unitsRequired(int goal) {
        HashMap<Constructable, Integer> unitsRequired = new HashMap<>();

        switch (goal) {
            case 1:
                unitsRequired.put(Constructable.ZEALOT, 1);
                unitsRequired.put(Constructable.STALKER, 4);
                unitsRequired.put(Constructable.IMMORTAL, 2);
                unitsRequired.put(Constructable.COLOSSUS, 2);
                break;
            case 2:
                unitsRequired.put(Constructable.ZEALOT, 6);
                unitsRequired.put(Constructable.STALKER, 2);
                unitsRequired.put(Constructable.SENTRY, 3);
                unitsRequired.put(Constructable.VOID_RAY, 4);
            case 3:
                unitsRequired.put(Constructable.ZEALOT, 2);
                unitsRequired.put(Constructable.STALKER,2);
                unitsRequired.put(Constructable.SENTRY, 1);
                unitsRequired.put(Constructable.COLOSSUS, 2);
                unitsRequired.put(Constructable.PHOENIX, 5);
            case 4:
                unitsRequired.put(Constructable.ZEALOT, 10);
                unitsRequired.put(Constructable.STALKER, 7);
                unitsRequired.put(Constructable.SENTRY, 2);
                unitsRequired.put(Constructable.HIGH_TEMPLAR, 3);
            case 5:
                unitsRequired.put(Constructable.ZEALOT, 8);
                unitsRequired.put(Constructable.STALKER, 10);
                unitsRequired.put(Constructable.SENTRY, 2);
                unitsRequired.put(Constructable.IMMORTAL, 1);
                unitsRequired.put(Constructable.OBSERVER, 1);
                unitsRequired.put(Constructable.CARRIER, 3);
                unitsRequired.put(Constructable.DARK_TEMPLAR, 2);
            default:
                throw new IllegalArgumentException();
        }
        return unitsRequired;
    }

    private HashSet<Constructable> buildingsRequired() {
        HashSet<Constructable> buildingsRequired = new HashSet<>();
        for (Constructable construct : unitsRequired.keySet()) {
            buildingsRequired.addAll(Arrays.asList(construct.getDependencies()));
            for (Constructable c : construct.getDependencies()) {
                buildingsRequired.addAll(Arrays.asList(c.getDependencies()));
            }
        }
        return buildingsRequired;
    }

    public HashMap<Constructable, Integer> getUnitsRequired() {
        return unitsRequired;
    }

    public int getMineralsRequired() {
        return mineralsRequired;
    }

    public int getGasRequired() {
        return gasRequired;
    }
}
