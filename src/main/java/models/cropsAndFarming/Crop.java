package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;

public abstract class Crop extends Item {
    String name;
    int baseSellPrice;
    boolean canBeEaten;
    int baseEnergy;
    int baseHealth;
    Season[] seasons;


    public Crop(String name) {
        super(name);
    }
}