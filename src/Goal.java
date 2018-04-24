import java.util.*;

public class Goal {
    //ATTRIBUTE (map to store how many of each unit is required)
    private HashMap<Constructable, Integer> unitsRequired = new HashMap<>();

    //CONSTRUCTOR
    public Goal(HashMap<Constructable, Integer> unitsRequired) {
        this.unitsRequired = unitsRequired;
    }

    //GOALS
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

    //GETTER
    public HashMap<Constructable, Integer> getUnitsRequired() {
        return unitsRequired;
    }
}
