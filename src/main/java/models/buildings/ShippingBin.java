package models.buildings;

import models.Location;

public class ShippingBin extends Building {
    Location startLocation;

    public ShippingBin(Location startLocation) {
        super("Shipping Bin", startLocation, 1, 1);
    }
}
