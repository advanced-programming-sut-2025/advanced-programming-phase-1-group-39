package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Pierre extends NPC {

    public Pierre() {
        super(
                "pierre",
                "General Store Owner",
                "Passionate about small business and proud of his store.\nHe has a flair for dramatics when it comes to JojaMart, and always tries to make his goods sound like the best.",
                new Location(33,26),
                new ArrayList<>(List.of(ItemManager.getItemByName("Oil"), ItemManager.getItemByName("Vinegar")))
        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}
