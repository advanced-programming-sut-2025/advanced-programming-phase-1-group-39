package models.Shops;

import models.Item;

public class ShopItem extends Item {
    private int price;
    private int dailyLimit;
    private int availableQuantity;

    public ShopItem(String name, int price, int dailyLimit) {
        super(name);
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.availableQuantity = dailyLimit;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void decreaseQuantity(int amount) {
        availableQuantity = Math.max(availableQuantity - amount, 0);
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public void resetDailyLimit(int quantity) {
        availableQuantity = dailyLimit;
    }
}
