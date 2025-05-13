package models;

import models.inventory.Inventory;

public class Refrigerator {

    private Inventory inventory = new Inventory();

    public void putItem(Item item, int amount) {
        inventory.addItem(item, amount);
    }

    public ItemStack pickItem(String itemName, int amount) {
        ItemStack item = inventory.pickItem(itemName, amount);
        if (item != null) {
            return item;
        }
        return null;
    }

    public void addItem(Item item, int amount) {
        inventory.addItem(item, amount);
    }

    public boolean contains(String itemName) {
        return inventory.hasItem(itemName);
    }

    public Inventory getInventory() {
        return inventory;
    }
}

