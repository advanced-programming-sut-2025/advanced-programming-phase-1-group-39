package models.cropsAndFarming;

import models.Item;

public class ForagingMaterial extends Item {
    int baseSellPrice;
    public ForagingMaterial(String name, int baseSellPrice) {
        super(name);
        this.baseSellPrice = baseSellPrice;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public ForagingMaterial clone() {
        return new ForagingMaterial(this.getName(), this.baseSellPrice);
    }

    @Override
    public String toString() {
        return "ForagingMaterial{" +
                "baseSellPrice=" + baseSellPrice +
                ", name='" + name + '\'' +
                '}';
    }
}
