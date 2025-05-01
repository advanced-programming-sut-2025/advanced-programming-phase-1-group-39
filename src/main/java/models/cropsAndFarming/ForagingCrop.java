package models.cropsAndFarming;

import models.Enums.Season;

public class ForagingCrop extends Crop {
    ForagingSource source;

    public ForagingCrop(String name) {
        super(name);
    }

    public ForagingCrop(String name, int baseSellPrice, int energy, ForagingSource source, Season[] seasons) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.canBeEaten = true;
        this.baseEnergy = energy;
        this.source = source;
        this.seasons = seasons;
    }
}
