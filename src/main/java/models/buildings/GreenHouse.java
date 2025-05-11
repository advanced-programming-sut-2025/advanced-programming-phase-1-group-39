package models.buildings;

import models.Location;

public class GreenHouse extends Building {
    boolean isBuild = false;

    public GreenHouse(Location startLocation) {
        super("greenhouse", startLocation, 8, 8);
    }

    public void build() {
        isBuild = true;
    }
    public boolean isBuild() {
        return isBuild;
    }
}
