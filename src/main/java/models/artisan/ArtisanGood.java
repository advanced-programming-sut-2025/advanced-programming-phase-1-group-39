package models.artisan;

import models.Item;

public class ArtisanGood extends Item {
    int energy;

    public ArtisanGood(String name) {
        super(name);
    }

//    public ArtisanGood(String name, int baseSellPrice, int energy) {
//        super(name, baseSellPrice);
//        this.energy = energy;
//    }
}
