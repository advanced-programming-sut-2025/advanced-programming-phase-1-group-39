package models.cooking;

import models.Item;

import java.util.Map;

public class Food extends Item {
    int energy;
    int sellPrice;
    FoodBuff buff;

    public Food(String name, int energy, int sellPrice,  FoodBuff buff) {
        super(name);
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.buff = buff;
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public FoodBuff getBuff() {
        return buff;
    }
}