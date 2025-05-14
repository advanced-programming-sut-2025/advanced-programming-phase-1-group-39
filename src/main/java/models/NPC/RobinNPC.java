package models.NPC;

import models.*;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class RobinNPC extends NPC {
    public RobinNPC() {
        super(
                "robin",
                "Carpenter",
                "Robin is a hardworking and pragmatic carpenter with a deep connection to the land and her craft.\nShe enjoys simple, home-cooked meals like spaghetti and finds fulfillment in her work around the farm.\nThough she’s always focused on her job, she has a warm and supportive personality, eager to help those in need.",
                new Location(32,50),
                new ArrayList<>(List.of(ItemManager.getItemByName("spaghetti"), ItemManager.getItemByName("Wood"),
                                ItemManager.getItemByName("Iron Bar")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/robinDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Wood"), 80)
                    , new ItemStack(ItemManager.getItemByName("Iron Bar"), 10)
                    , new ItemStack(ItemManager.getItemByName("Wood"), 10000)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/robinMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }

    @Override
    public void getRewardMission1(int friendShip, Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.getInventory().pickItem(getQuest(1).getTask().getItem().getName(),
                getQuest(1).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.addMoney(2000);
        } else {
            currentPlayer.addMoney(1000);
        }
    }

    @Override
    public void getRewardMission2(int friendShip, Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.getInventory().pickItem(getQuest(2).getTask().getItem().getName(),
                getQuest(2).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Bee House"), 6);
        } else {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Bee House"), 3);
        }
    }

    @Override
    public void getRewardMission3(int friendShip, Game game) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.getInventory().pickItem(getQuest(3).getTask().getItem().getName(),
                getQuest(3).getTask().getAmount());
        if (friendShip == 2) {
            currentPlayer.addMoney(50000);
        } else {
            currentPlayer.addMoney(25000);
        }
    }
}
