package models.NPC;

import models.ItemManager;
import models.Location;
import models.services.DialogueLoader;

import java.util.ArrayList;
import java.util.List;

public class AbigailNPC extends NPC {

    public AbigailNPC() {
        super(
                "Abigail",
                "Explorer ",
                "Abigail is a free-spirited, adventurous soul with a love for exploration and risk-taking.\nShe's very passionate about coffee and pumpkins and enjoys anything that challenges the status quo.\nShe’s bold, independent, and always ready for new experiences, which makes her an exciting NPC to befriend.",
                new Location(95, 22),
                new ArrayList<>(List.of(ItemManager.getItemByName("Stone"), ItemManager.getItemByName("Iron Ore"),
                                        ItemManager.getItemByName("Coffee"))),
                "src/main/resources/data/NPC/abigailDialogues.json"

        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap(filePath);
    }
}
