package models.cooking;

import java.util.Map;

public class Food {
    String name;
    int energy;
    int sellPrice;
    FoodBuff buff;

    public Food(String name, int energy, int sellPrice,  FoodBuff buff) {
        this.name = name;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.buff = buff;
    }
}
