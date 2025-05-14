package models.NPC;

import models.*;
import models.cooking.FoodRecipe;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class LeahNPC extends NPC {
    public LeahNPC() {
        super(
                "leah",
                "Artist",
                "Leia is an artist with a deep appreciation for nature and beauty.\nShe enjoys quiet moments surrounded by the beauty of the world and values peace.\nHer preferences lean toward fine food like salads and wine, and she loves the simple joy of creating beautiful things, whether it's art or nurturing the environment.",
                new Location(40, 82),
                new ArrayList<>(List.of(ItemManager.getItemByName("Salad"), ItemManager.getItemByName("Grape"),
                        ItemManager.getItemByName("Apple Wine")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/leahDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Wood"), 10)
                    , new ItemStack(ItemManager.getItemByName("Salmon"), 1)
                    , new ItemStack(ItemManager.getItemByName("Wood"), 200)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/harveyMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }

    @Override
    public void getRewardMission1(int friendShip, Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.getInventory().pickItem(getQuest(1).getTask().getItem().getName(),
                getQuest(1).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.addMoney(1000);
        } else {
            currentPlayer.addMoney(500);
        }
    }

    @Override
    public void getRewardMission2(int friendShip, Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.getInventory().pickItem(getQuest(3).getTask().getItem().getName(),
                getQuest(3).getTask().getAmount());
        currentPlayer.learnFoodRecipe(FoodRecipe.SALMON_DINNER);
    }

    @Override
    public void getRewardMission3(int friendShip, Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.getInventory().pickItem(getQuest(3).getTask().getItem().getName(),
                getQuest(3).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Deluxe Scarecrow"), 6);
        } else {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Deluxe Scarecrow"), 3);
        }

    }

}
