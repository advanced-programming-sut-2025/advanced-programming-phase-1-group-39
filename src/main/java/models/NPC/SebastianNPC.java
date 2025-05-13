package models.NPC;

import models.Item;
import models.ItemManager;
import models.ItemStack;
import models.Location;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SebastianNPC extends NPC {


    public SebastianNPC() {
        super(
                "sebastian",
                "programmer",
                "Sebastian is a brooding and introverted character who loves technology and enjoys spending time alone.\nHis hobbies include tinkering with gadgets and playing video games.\nHe has a soft spot for pizza and pumpkins, and although he seems distant at first, he appreciates meaningful connections over time.",
                new Location(30, 15),
                new ArrayList<>(List.of((ItemManager.getItemByName("pumpkin pie")), (ItemManager.getItemByName("Wool")),
                        (ItemManager.getItemByName("pizza"))))

        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/sebastianDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Iron Bar"), 50)
                , new ItemStack(ItemManager.getItemByName("pumpkin pie"), 1)
                , new ItemStack(ItemManager.getItemByName("Stone"), 150)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/sebastianMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }
}
