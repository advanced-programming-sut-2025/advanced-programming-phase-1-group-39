package models.buildings;

import models.Location;
import models.Refrigerator;

public class Cabin extends Building {
    Refrigerator refrigerator = new Refrigerator();

    public Cabin(Location startLocation) {
        super("cabin", startLocation, 6, 6);
    }

    public Refrigerator getRefrigerator() {
        return refrigerator;
    }
}
