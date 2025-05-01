package models.cropsAndFarming;

import models.Enums.Season;

import java.util.Arrays;

public class ForagingCrop extends Crop {
    private ForagingSource source;

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

    public ForagingSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "ForagingCrop{" +
                "source=" + source +
                ", name='" + name + '\'' +
                ", baseSellPrice=" + baseSellPrice +
                ", canBeEaten=" + canBeEaten +
                ", baseEnergy=" + baseEnergy +
                ", baseHealth=" + baseHealth +
                ", seasons=" + Arrays.toString(seasons) +
                '}';
    }

    @Override
    protected Object clone(){
        return new ForagingCrop(this.name, this.baseSellPrice, this.baseEnergy, this.source, this.seasons);
    }
}
