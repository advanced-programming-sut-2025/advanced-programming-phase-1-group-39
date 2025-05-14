package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Marnie extends NPC {

    public Marnie() {
        super(
                "marine",
                "Rancher & Livestock Supplier",
                "Marnie treats her animals like family.\nShe’s warm and friendly, always willing to give farming advice or chat over a cup of tea.",
                new Location(31,80),
                new ArrayList<>(List.of(ItemManager.getItemByName("Chicken"), ItemManager.getItemByName("Cow")))

        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}

