package models.artisan;

import models.Item;

public class ArtisanGood extends Item {
    private int baseSellPrice;
    private int energy;

    public ArtisanGood(String name, int baseSellPrice, int energy) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.energy = energy;
    }
}
