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

    public void purchase(int amount) {
        if (dailyLimit > 500000) { // Todo: kheili randome haji shayad bug bokhore
            return;
        }
        dailyLimit -= amount;
        if (dailyLimit < 0) {
            dailyLimit = 0;
        }
    }
}
