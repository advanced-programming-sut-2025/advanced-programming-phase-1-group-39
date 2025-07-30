package com.StardewValley.models.buildings;

import com.StardewValley.models.Location;
import com.StardewValley.models.Refrigerator;

public class Cabin extends Building {
    Refrigerator refrigerator = new Refrigerator();

    public Cabin(Location startLocation) {
        super("cabin", startLocation, 6, 6);
    }

    public Cabin() {}

    public Refrigerator getRefrigerator() {
        return refrigerator;
    }
}
