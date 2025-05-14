package models.NPC;

import models.ItemManager;
import models.ItemStack;
import models.Location;
import models.cropsAndFarming.CropManager;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class HarveyNPC extends NPC {


    public HarveyNPC() {
        super(
                "harvey",
                "Doctor",
                "Harvey is a kind-hearted, meticulous doctor who values routine and health above all else.\nHe enjoys reading and often spends his time researching medical topics.\nHarvey has a deep love for coffee and wine and prefers to stay out of the spotlight, but his caring nature shines when you get to know him better.",
                new Location(25,82),
                new ArrayList<>(List.of(ItemManager.getItemByName("Coffee"), ItemManager.getItemByName("Carrot Pickle"),
                                ItemManager.getItemByName("Apple Wine")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/harveyDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Corn"), 12)
                     , new ItemStack(ItemManager.getItemByName("Salmon"), 1)
                     , new ItemStack(ItemManager.getItemByName("Wine"), 1)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/harveyMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }
}
