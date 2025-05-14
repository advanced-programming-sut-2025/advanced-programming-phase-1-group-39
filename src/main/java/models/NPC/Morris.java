package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Morris extends NPC {

    public Morris() {
        super(
                "morris",
                "Corporate Store Manager",
                "Always trying to sell, always smiling.\nHe’s all about profit and progress, but behind the corporate charm is someone you may not fully trust.",
                new Location(105,16),
                new ArrayList<>(List.of(ItemManager.getItemByName("Rice"), ItemManager.getItemByName("Sugar")))
        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}
