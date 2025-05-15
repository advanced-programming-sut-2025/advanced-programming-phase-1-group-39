package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Willy extends NPC {

    public Willy() {
        super(
                "willy",
                "Fisherman & Tackle Shop Owner",
                "A salty old fisherman with countless tales from the sea.\nHe’s simple and humble, and has a deep respect for nature and the ocean.",
                new Location(105,80),
                new ArrayList<>(List.of(ItemManager.getItemByName("Wood"), ItemManager.getItemByName("Iron")))
        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}

