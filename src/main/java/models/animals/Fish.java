package models.animals;

import models.Enums.Season;

public class Fish {
    private int energy;
    private FishType type;
    private int price;
    private Season season;
    private AnimalProductQuality quality;

    public Fish(int energy, FishType type, int price, Season season) {
        this.energy = energy;
        this.type = type;
        this.price = price;
        this.season = season;
        this.quality = AnimalProductQuality.NORMAL; // initial Todo:
    }

    public int getEnergy() {
        return energy;
    }

    public FishType getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public Season getSeason() {
        return season;
    }

    public AnimalProductQuality getQuality() {
        return quality;
    }

    public void setQuality(AnimalProductQuality quality) {
        this.quality = quality;
    }

    public int getFinalPrice() {
        return (int) (price * quality.getRate());
    }
}