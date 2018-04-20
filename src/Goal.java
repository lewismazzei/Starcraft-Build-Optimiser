import java.util.HashMap;
import java.util.Map;

public class Goal {
    private HashMap<Constructable, Integer> unitsRequired = new HashMap<>();
    private int mineralsRequired;
    private int gasRequired;

    public Goal(HashMap<Constructable, Integer> unitsRequired) {
        this.unitsRequired = unitsRequired;
        this.mineralsRequired = calcMineralsRequired(unitsRequired);
        this.gasRequired = calcGasRequired(unitsRequired);
    }

    private int calcMineralsRequired(HashMap<Constructable, Integer> units) {
        int mineralCount = 0;
        for (Map.Entry<Constructable, Integer> entry : units.entrySet()) {
            mineralCount += entry.getKey().getMineralCost() * entry.getValue();
        }
        return mineralCount;
    }

    private int calcGasRequired(HashMap<Constructable, Integer> units) {
        int mineralCount = 0;
        for (Map.Entry<Constructable, Integer> entry : units.entrySet()) {
            mineralCount += entry.getKey().getGasCost() * entry.getValue();
        }
        return mineralCount;
    }

    public int getMineralsRequired() {
        return mineralsRequired;
    }

    public int getGasRequired() {
        return gasRequired;
    }
}
