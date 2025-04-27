package models.animals;

import models.Enums.Season;

public class Fish {
    int energy;
    FishType type;
    int price;
    Season season;

    public Fish(int energy, FishType type, int price, Season season) {
        this.energy = energy;
        this.type = type;
        this.price = price;
        this.season = season;
    }
}