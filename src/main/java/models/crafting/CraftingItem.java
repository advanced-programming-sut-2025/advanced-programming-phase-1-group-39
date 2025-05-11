package models.crafting;

import models.Item;

import java.util.Map;

public class CraftingItem extends Item {
    private int sellPrice;

    public CraftingItem(String name, int sellPrice) {
        super(name);
        this.sellPrice = sellPrice;
    }

    public String getName() {
        return name;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public String toString() {
        return "CraftingItem{" +
                "sellPrice=" + sellPrice +
                ", name='" + name + '\'' +
                '}';
    }
}
