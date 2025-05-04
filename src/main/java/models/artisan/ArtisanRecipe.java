package models.artisan;

import java.util.HashMap;

public class ArtisanRecipe {
    private String name;
    private HashMap<String, Integer> ingredients;
    private int sellPrice;
    private int energy;

    private ArtisanGood good;

    public ArtisanRecipe(String name, HashMap<String, Integer> ingredients, int sellPrice, int energy) {
        this.name = name;
        this.ingredients = ingredients;
        this.good = new ArtisanGood(name, sellPrice, energy);
    }
}
