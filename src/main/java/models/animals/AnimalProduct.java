package models.animals;

import models.Item;

public class AnimalProduct extends Item {
    int baseSellPrice;
    public AnimalProduct(String name, int baseSellPrice) {
        super(name);
        this.baseSellPrice = baseSellPrice;
    }

    public String getName() {
        return name;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    @Override
    public String toString() {
        return "AnimalProduct{" +
                "baseSellPrice=" + baseSellPrice +
                ", name='" + name + '\'' +
                '}';
    }
}
