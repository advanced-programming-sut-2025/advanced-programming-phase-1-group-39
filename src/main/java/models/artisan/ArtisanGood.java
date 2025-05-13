package models.artisan;

import models.Item;

public class ArtisanGood extends Item {
    private final String description;
    private final int energy;
    private final int sellPrice;

    public ArtisanGood(String name, String description, int energy, int sellPrice) {
        super(name);
        this.description = description;
        this.energy = energy;
        this.sellPrice = sellPrice;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergy() { return energy; }
    public int getSellPrice() { return sellPrice; }
}
