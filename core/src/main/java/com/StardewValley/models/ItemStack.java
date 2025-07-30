package com.StardewValley.models;


import com.StardewValley.models.saveClasses.ToolData;
import com.StardewValley.models.tools.Tool;

public class ItemStack {
    transient Item item;

    String name;
    int amount;
    ToolData toolData = null;

    public ItemStack(Item item, int amount) {
        this.item = item;
        if (item instanceof Tool)
            toolData = new ToolData((Tool)item);
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

    public ToolData getToolData() {
        return toolData;
    }
}