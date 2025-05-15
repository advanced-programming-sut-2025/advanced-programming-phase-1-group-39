package models.NPC;

import models.*;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class HarveyNPC extends NPC {


    public HarveyNPC() {
        super(
                "harvey",
                "Doctor",
                "Harvey is a kind-hearted, meticulous doctor who values routine and health above all else.\nHe enjoys reading and often spends his time researching medical topics.\nHarvey has a deep love for coffee and wine and prefers to stay out of the spotlight, but his caring nature shines when you get to know him better.",
                new Location(25,82),
                new ArrayList<>(List.of(ItemManager.getItemByName("Coffee"), ItemManager.getItemByName("Carrot Pickle"),
                        ItemManager.getItemByName("Apple Wine")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/harveyDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Corn"), 12)
                , new ItemStack(ItemManager.getItemByName("Salmon"), 1)
                , new ItemStack(ItemManager.getItemByName("Orange Wine"), 1)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/harveyMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }

    @Override
    public void getRewardMission1(int friendShip, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(1).getTask().getItem().getName(),
                getQuest(1).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.changeMoney(1500);
        } else {
            currentPlayer.changeMoney(750);
        }
    }

    @Override
    public void getRewardMission2(int friendShip, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(2).getTask().getItem().getName(),
                getQuest(2).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.getFriendship("harvey").setFriendshipLevel(3);
        } else if (friendShip == 1) {
            currentPlayer.getFriendship("harvey").setFriendshipLevel(2);
        }
    }

    @Override
    public void getRewardMission3(int friendShip, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(3).getTask().getItem().getName(),
                getQuest(3).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Salad"), 10);
        } else {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Salad"), 5);
        }
    }
}
