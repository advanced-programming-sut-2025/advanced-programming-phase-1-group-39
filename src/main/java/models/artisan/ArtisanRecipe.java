package models.artisan;

import java.util.HashMap;

public class ArtisanRecipe {
    private String name;
    private HashMap<String, Integer> ingredients;
    private String description;
    private int processingTime;
    private int sellPrice;
    private int energy;

    private ArtisanGood good;

    public ArtisanRecipe(String name, String description, HashMap<String, Integer> ingredients, int processingTime, int sellPrice, int energy) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.processingTime = processingTime;
        this.sellPrice = sellPrice;
        this.energy = energy;
        this.good = new ArtisanGood(name, description, sellPrice, energy);
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Integer> getIngredients() {
        return ingredients;
    }

    public ArtisanGood getGood() {
        return good;
    }

    public String getDescription() {
        return description;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getEnergy() {
        return energy;
    }
}
