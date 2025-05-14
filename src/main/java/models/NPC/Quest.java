package models.NPC;

import models.ItemStack;

import java.util.ArrayList;

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
