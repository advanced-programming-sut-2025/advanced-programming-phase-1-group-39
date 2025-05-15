package models.buildings;

import models.Location;

public class ShippingBin extends Building {
    Location startLocation;

    public ShippingBin(String name, Location startLocation, int width, int height) {
        super(name, startLocation, width, height);
    }
}
