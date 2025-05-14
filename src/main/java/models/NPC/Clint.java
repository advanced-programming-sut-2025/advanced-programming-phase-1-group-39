package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Clint extends NPC {
    public Clint() {
        super(
                "clint",
                "Blacksmith",
                "A quiet man who spends his days hammering away at the forge.\nHe’s a bit awkward around people, especially someone he has a crush on, but he’s loyal and dedicated.",
                new Location(105,53),
                new ArrayList<>(List.of(ItemManager.getItemByName("Iron"), ItemManager.getItemByName("Iridium")))
        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}



