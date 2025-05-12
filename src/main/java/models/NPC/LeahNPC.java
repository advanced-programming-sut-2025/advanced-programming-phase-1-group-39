package models.NPC;

import models.ItemManager;
import models.Location;
import models.services.DialogueLoader;

import java.util.ArrayList;
import java.util.List;

public class LeahNPC extends NPC {
    public LeahNPC() {
        super(
                "Leah",
                "Artist",
                "Leia is an artist with a deep appreciation for nature and beauty.\nShe enjoys quiet moments surrounded by the beauty of the world and values peace.\nHer preferences lean toward fine food like salads and wine, and she loves the simple joy of creating beautiful things, whether it's art or nurturing the environment.",
                new Location(40,82),
                new ArrayList<>(List.of(ItemManager.getItemByName("Salad"), ItemManager.getItemByName("Grape"),
                                ItemManager.getItemByName("Wine"))),
                "src/main/resources/data/NPC/leahDialogues.json"
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap(filePath);
    }
}
