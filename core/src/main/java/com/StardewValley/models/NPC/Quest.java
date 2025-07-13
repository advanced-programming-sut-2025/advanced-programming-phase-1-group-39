package com.StardewValley.models.NPC;


import com.StardewValley.models.ItemStack;

public class Quest {

    int level;
    ItemStack task;

    public Quest(int level, ItemStack task) {
        this.level = level;
        this.task = task;
    }

    public int getLevel() {
        return level;
    }

    public ItemStack getTask() {
        return task;
    }
}

