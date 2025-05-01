package models.cropsAndFarming;

import models.Enums.Season;
import models.map.Tile;

public class FarmingProduct extends Crop {
    Tile tile;
    boolean canBecomeGiant;
    public FarmingProduct(String name, Tile tile, int baseSellPrice, boolean isEdible, int baseEnergy,
                          int baseHealth, Season[] seasons, boolean canBecomeGiant) {
        super(name);
        this.tile = tile;
        this.baseSellPrice = baseSellPrice;
        this.canBeEaten = isEdible;
        this.baseEnergy = baseEnergy;
        this.baseHealth = baseHealth;
        this.seasons = seasons;
        this.canBecomeGiant = canBecomeGiant;
    }
}
