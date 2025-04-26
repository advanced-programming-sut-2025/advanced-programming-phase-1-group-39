package models.cropsAndFarming;

import models.Enums.Season;

public class FarmingProduct extends Crop {
    boolean canBecomeGiant;
    public FarmingProduct(String name, int baseSellPrice, boolean isEdible, int baseEnergy,
                          int baseHealth, Season season, boolean canBecomeGiant) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.canBeEaten = isEdible;
        this.baseEnergy = baseEnergy;
        this.baseHealth = baseHealth;
        this.season = season;
        this.canBecomeGiant = canBecomeGiant;
    }
}
