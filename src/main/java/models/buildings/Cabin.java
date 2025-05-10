package models.buildings;

import models.Location;

public class Cabin extends Building {

    public Cabin(Location startLocation) {
        super("Cabin", startLocation, 6, 6);
    }
}
