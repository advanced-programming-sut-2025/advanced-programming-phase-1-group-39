package com.StardewValley.models.cropsAndFarming;

import models.Enums.Season;
import models.cropsAndFarming.Crop;

public class FarmingProduct extends Crop {
    boolean canBecomeGiant;
    public FarmingProduct(String name, int baseSellPrice, boolean isEdible, int baseEnergy,
                          int baseHealth, Season[] seasons, boolean canBecomeGiant) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.canBeEaten = isEdible;
        this.baseEnergy = baseEnergy;
        this.baseHealth = baseHealth;
        this.seasons = seasons;
        this.canBecomeGiant = canBecomeGiant;
    }
}