package models.NPC;

import models.ItemManager;
import models.ItemStack;
import models.Location;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class AbigailNPC extends NPC {

    public AbigailNPC() {
        super(
                "abigail",
                "Explorer ",
                "Abigail is a free-spirited, adventurous soul with a love for exploration and risk-taking.\nShe's very passionate about coffee and pumpkins and enjoys anything that challenges the status quo.\nShe’s bold, independent, and always ready for new experiences, which makes her an exciting NPC to befriend.",
                new Location(95, 22),
                new ArrayList<>(List.of(ItemManager.getItemByName("Stone"), ItemManager.getItemByName("Iron Ore"),
                        ItemManager.getItemByName("Coffee")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/abigailDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Gold Bar"), 1)
                , new ItemStack(ItemManager.getItemByName("Pumpkin"), 1)
                , new ItemStack(ItemManager.getItemByName("Wheat"), 50)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/abigailMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }
}
