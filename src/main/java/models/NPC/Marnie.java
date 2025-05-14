package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Marnie extends NPC {

    public Marnie() {
        super(
                "clint",
                "   ",
                "     ",
                new Location(1,1),
                new ArrayList<>(List.of(ItemManager.getItemByName("aaaa"),
                        ItemManager.getItemByName("aaaa")))

        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}

