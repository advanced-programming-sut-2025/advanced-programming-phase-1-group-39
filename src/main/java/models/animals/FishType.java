package models.animals;

import models.Enums.Season;

public enum FishType {
    SALMON(75, Season.FALL),
    SARDINE(40, Season.FALL),
    SHAD(60, Season.FALL),
    BLUE_DISCUS(120, Season.FALL),

    MIDNIGHT_CARP(150, Season.WINTER),
    SQUID(80, Season.WINTER),
    TUNA(100, Season.WINTER),
    PERCH(55, Season.WINTER),

    FLOUNDER(100, Season.SPRING),
    LIONFISH(100, Season.SPRING),
    HERRING(30, Season.SPRING),
    GHOSTFISH(45, Season.SPRING),

    TILAPIA(75, Season.SUMMER),
    DORADO(100, Season.SUMMER),
    SUNFISH(30, Season.SUMMER),
    RAINBOW_TROUT(65, Season.SUMMER),

    // Legendary
    LEGEND(5000, Season.SPRING),
    GLACIERFISH(1000, Season.WINTER),
    ANGLER(900, Season.FALL),
    CRIMSONFISH(1500, Season.SUMMER);

    public final int basePrice;
    public final Season season;

    FishType(int basePrice, Season season) {
        this.basePrice = basePrice;
        this.season = season;
    }

    public Fish create() {
        return new Fish(this.name(), this, basePrice, season);
    }
}
