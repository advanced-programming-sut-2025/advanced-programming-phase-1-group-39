package models.NPC;

import models.ItemManager;
import models.ItemStack;
import models.Location;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class LeahNPC extends NPC {
    public LeahNPC() {
        super(
                "leah",
                "Artist",
                "Leia is an artist with a deep appreciation for nature and beauty.\nShe enjoys quiet moments surrounded by the beauty of the world and values peace.\nHer preferences lean toward fine food like salads and wine, and she loves the simple joy of creating beautiful things, whether it's art or nurturing the environment.",
                new Location(40, 82),
                new ArrayList<>(List.of(ItemManager.getItemByName("Salad"), ItemManager.getItemByName("Grape"),
                        ItemManager.getItemByName("Wine")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/leahDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Wood"), 10)
                    , new ItemStack(ItemManager.getItemByName("Salmon"), 1)
                    , new ItemStack(ItemManager.getItemByName("Wood"), 200)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/harveyMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }
}
