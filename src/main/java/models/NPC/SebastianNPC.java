package models.NPC;

import models.Item;
import models.ItemManager;
import models.Location;
import models.services.DialogueLoader;

import java.util.ArrayList;
import java.util.List;

public class SebastianNPC extends NPC {



    public SebastianNPC() {
        super(
                "Sebastian",
                "programmer",
                "Sebastian is a brooding and introverted character who loves technology and enjoys spending time alone.\nHis hobbies include tinkering with gadgets and playing video games.\nHe has a soft spot for pizza and pumpkins, and although he seems distant at first, he appreciates meaningful connections over time.",
                new Location(30, 15),
                new ArrayList<>(List.of(ItemManager.getItemByName("pumpkin pie"), ItemManager.getItemByName("Wool"),
                                        ItemManager.getItemByName("pizza"))),
                "src/main/resources/data/NPC/sebastianDialogues.json"
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap(filePath);
    }






}
