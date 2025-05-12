package models.NPC;

import models.ItemManager;
import models.Location;
import models.services.DialogueLoader;

import java.util.ArrayList;
import java.util.List;

public class HarveyNPC extends NPC {


    public HarveyNPC() {
        super(
                "harvey",
                "Doctor",
                "Harvey is a kind-hearted, meticulous doctor who values routine and health above all else.\nHe enjoys reading and often spends his time researching medical topics.\nHarvey has a deep love for coffee and wine and prefers to stay out of the spotlight, but his caring nature shines when you get to know him better.",
                new Location(25,82),
                new ArrayList<>(List.of(ItemManager.getItemByName("Coffee"), ItemManager.getItemByName("Carrot Pickles"),
                                ItemManager.getItemByName("Wine"))),
                "src/main/resources/data/NPC/harveyDialogues.json"

        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap(filePath);
    }
}
