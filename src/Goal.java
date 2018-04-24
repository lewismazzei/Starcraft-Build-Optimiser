import java.util.*;

public class Goal {
    private HashMap<Constructable, Integer> unitsRequired = new HashMap<>();
    //private ArrayList<Constructable> buildingsRequired = new ArrayList<>();
    //private int mineralsRequired;
    //private int gasRequired;

    public Goal(HashMap<Constructable, Integer> unitsRequired) {
        this.unitsRequired = unitsRequired;
        //this.mineralsRequired = calcMineralsRequired(unitsRequired);
        //this.gasRequired = calcGasRequired(unitsRequired);
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
                break;
            case 3:
                unitsRequired.put(Constructable.ZEALOT, 2);
                unitsRequired.put(Constructable.STALKER,2);
                unitsRequired.put(Constructable.SENTRY, 1);
                unitsRequired.put(Constructable.COLOSSUS, 2);
                unitsRequired.put(Constructable.PHOENIX, 5);
                break;
            case 4:
                unitsRequired.put(Constructable.ZEALOT, 10);
                unitsRequired.put(Constructable.STALKER, 7);
                unitsRequired.put(Constructable.SENTRY, 2);
                unitsRequired.put(Constructable.HIGH_TEMPLAR, 3);
                break;
            case 5:
                unitsRequired.put(Constructable.ZEALOT, 8);
                unitsRequired.put(Constructable.STALKER, 10);
                unitsRequired.put(Constructable.SENTRY, 2);
                unitsRequired.put(Constructable.IMMORTAL, 1);
                unitsRequired.put(Constructable.OBSERVER, 1);
                unitsRequired.put(Constructable.CARRIER, 3);
                unitsRequired.put(Constructable.DARK_TEMPLAR, 2);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return unitsRequired;
    }

    public ArrayList<Constructable> getBuildingsRequired() {
        //HashSet<Constructable> dependancies = new HashSet<>();
        //for (int i = 0; i < constructs.size(); i++) {
        //    if (constructs.size() != 0) {
        //        Constructable construct = Arrays.asList(constructs).get(i);
        //        dependancies.add(construct));
        //
        //    }
        //}
        ArrayList<Constructable> buildingsRequired = new ArrayList<>();

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

    //public int getMineralsRequired() {
    //    return mineralsRequired;
    //}
    //
    //public int getGasRequired() {
    //    return gasRequired;
    //}
}
