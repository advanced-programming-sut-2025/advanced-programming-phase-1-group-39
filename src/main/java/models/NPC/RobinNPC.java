package models.NPC;

import models.ItemManager;
import models.ItemStack;
import models.Location;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class RobinNPC extends NPC {
    public RobinNPC() {
        super(
                "robin",
                "Carpenter",
                "Robin is a hardworking and pragmatic carpenter with a deep connection to the land and her craft.\nShe enjoys simple, home-cooked meals like spaghetti and finds fulfillment in her work around the farm.\nThough she’s always focused on her job, she has a warm and supportive personality, eager to help those in need.",
                new Location(25,43),
                new ArrayList<>(List.of(ItemManager.getItemByName("spaghetti"), ItemManager.getItemByName("Wood"),
                                ItemManager.getItemByName("Iron Bar")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/robinDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Wood"), 80)
                    , new ItemStack(ItemManager.getItemByName("Iron Bar"), 10)
                    , new ItemStack(ItemManager.getItemByName("Wood"), 10000)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/robinMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }
}
