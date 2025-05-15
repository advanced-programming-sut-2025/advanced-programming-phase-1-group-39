package models.inventory;

import models.Constants;

public enum InventoryType {
    BASIC(12), BIG(24), DELUXE(1000000);

    int capacity;
    InventoryType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public static InventoryType getType(String name) {
        if (name.equalsIgnoreCase("Basic Pack")) {
            return BASIC;
        }
        if (name.equalsIgnoreCase("Large Pack")) {
            return BIG;
        }
        if (name.equalsIgnoreCase("Deluxe Pack")) {
            return DELUXE;
        }

        return null;
    }
}
