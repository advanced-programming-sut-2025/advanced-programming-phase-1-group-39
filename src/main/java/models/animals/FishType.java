package models.animals;

import models.Enums.Season;

public enum FishType {
    SALMON(75, Season.FALL, 15),
    SARDINE(40, Season.FALL, 10),
    SHAD(60, Season.FALL, 12),
    BLUE_DISCUS(120, Season.FALL, 18),

    MIDNIGHT_CARP(150, Season.WINTER, 20),
    SQUID(80, Season.WINTER, 14),
    TUNA(100, Season.WINTER, 16),
    PERCH(55, Season.WINTER, 13),

    FLOUNDER(100, Season.SPRING, 15),
    LIONFISH(100, Season.SPRING, 15),
    HERRING(30, Season.SPRING, 9),
    GHOSTFISH(45, Season.SPRING, 12),

    TILAPIA(75, Season.SUMMER, 14),
    DORADO(100, Season.SUMMER, 16),
    SUNFISH(30, Season.SUMMER, 8),
    RAINBOW_TROUT(65, Season.SUMMER, 13),

    // Legendary
    LEGEND(5000, Season.SPRING, 50),
    GLACIERFISH(1000, Season.WINTER, 40),
    ANGLER(900, Season.FALL, 35),
    CRIMSONFISH(1500, Season.SUMMER, 45);

    public final int basePrice;
    public final Season season;
    public final int energy;

    FishType(int basePrice, Season season, int energy) {
        this.basePrice = basePrice;
        this.season = season;
        this.energy = energy;
    }

    public Fish create() {
        return new Fish(energy, this, basePrice, season);
    }
}
