package models.NPC;

import models.ItemStack;

import java.util.ArrayList;

public class Quest {

    int level;
    String mission;
    ItemStack task;

    public Quest(int level, String mission, ItemStack task) {
        this.level = level;
        this.mission = mission;
        this.task = task;
    }
}
