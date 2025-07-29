package com.StardewValley.models.saveClasses;

import com.StardewValley.models.NPC.Quest;

public class QuestData {
    private int level;

    private String taskItemName;
    private int taskItemAmount;

    public QuestData(Quest quest) {
        level = quest.getLevel();
        taskItemName = quest.getTask().getItem().getName();
        taskItemAmount = quest.getTask().getAmount();
    }
}
