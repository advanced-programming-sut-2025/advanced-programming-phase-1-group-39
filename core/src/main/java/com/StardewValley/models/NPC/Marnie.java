package com.StardewValley.models.NPC;


import com.StardewValley.models.Game;
import com.StardewValley.models.ItemManager;
import com.StardewValley.models.Location;

import java.util.ArrayList;
import java.util.List;

public class Marnie extends NPC {

    public Marnie() {
        super(
                "marine",
                "Rancher & Livestock Supplier",
                "Marnie treats her animals like family.\nShe’s warm and friendly, always willing to give farming advice or chat over a cup of tea.",
                new Location(31,80),
                new ArrayList<>(List.of(ItemManager.getItemByName("Duck Feather"), ItemManager.getItemByName("Large Milk")))

        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}

