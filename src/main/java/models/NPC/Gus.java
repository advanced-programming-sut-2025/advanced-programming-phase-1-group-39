package models.NPC;

import models.Game;
import models.ItemManager;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class Gus extends NPC {

    public Gus() {
        super(
                "gus",
                "Chef & Bartender",
                "Gus loves food and people in equal measure.\nHis saloon is the heart of town, and he’s always ready to serve comfort—on a plate or with a kind word.",
                new Location(73,50),
                new ArrayList<>(List.of(ItemManager.getItemByName("Beer"), ItemManager.getItemByName("Salad")))

        );
    }

    @Override
    public void getRewardMission1(int friendship, Game game) {}

    @Override
    public void getRewardMission2(int friendship, Game game) {}

    @Override
    public void getRewardMission3(int friendship, Game game) {}
}

