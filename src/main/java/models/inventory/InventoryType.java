package models.inventory;

import models.Constants;

public enum InventoryType {
    BASIC(12), BIG(24), DELUXE(Constants.INFINITY);

    int capacity;
    InventoryType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}