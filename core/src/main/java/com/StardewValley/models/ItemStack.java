package com.StardewValley.models;


public class ItemStack {
    transient Item item;

    String name;
    int amount;

    public ItemStack(Item item, int amount) {
        this.item = item;

        this.name = (item != null) ? item.getName() : null;
        this.amount = amount;
    }

    public ItemStack() {
    }

    public void addStack(int amount) {
        this.amount += amount;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}