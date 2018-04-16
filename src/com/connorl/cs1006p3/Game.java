package com.connorl.cs1006p3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private int elapsed = 0;
    private List<Constructable> buildings = new ArrayList<>();
    private List<Constructable> units = new ArrayList<>();

    public List<Constructable> getBuildings() {
        return buildings;
    }

    public List<Constructable> getUnits() {
        return units;
    }
}
