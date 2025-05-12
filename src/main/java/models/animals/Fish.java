package models.animals;

import models.Enums.Season;

public class Fish {
    private FishType type;
    private int price;
    private Season season;
    private AnimalProductQuality quality;

    public Fish(FishType type, int price, Season season) {
        this.type = type;
        this.price = price;
        this.season = season;
        this.quality = AnimalProductQuality.NORMAL; // initial Todo:
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