package models.cooking;

import java.util.Map;

public class Food {
    String name;
    Map<String, Integer> ingredients;
    int energy;
    FoodBuff buff;
    int sellPrice;

    public Food(String name, Map<String, Integer> ingredients, int energy, FoodBuff buff, int sellPrice) {
        this.name = name;
        this.ingredients = ingredients;
        this.energy = energy;
        this.buff = buff;
        this.sellPrice = sellPrice;
    }
}
