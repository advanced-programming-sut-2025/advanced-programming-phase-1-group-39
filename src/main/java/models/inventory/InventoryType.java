package models.inventory;

import models.Constants;

public enum InventoryType {
    BASIC(12), BIG(24), DELUXE(1000000); // TODO: may be change

    int capacity;
    InventoryType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}