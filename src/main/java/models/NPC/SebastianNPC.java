package models.NPC;

import models.*;
import models.services.DialogueLoader;
import models.services.MissionsLoader;

import java.util.ArrayList;
import java.util.List;

public class SebastianNPC extends NPC {


    public SebastianNPC() {
        super(
                "sebastian",
                "programmer",
                "Sebastian is a brooding and introverted character who loves technology and enjoys spending time alone.\nHis hobbies include tinkering with gadgets and playing video games.\nHe has a soft spot for pizza and pumpkins, and although he seems distant at first, he appreciates meaningful connections over time.",
                new Location(30, 15),
                new ArrayList<>(List.of((ItemManager.getItemByName("Pumpkin Pie")), (ItemManager.getItemByName("Wool")),
                        (ItemManager.getItemByName("Pizza"))))

        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/sebastianDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Iron Bar"), 50)
                , new ItemStack(ItemManager.getItemByName("pumpkin pie"), 1)
                , new ItemStack(ItemManager.getItemByName("Stone"), 150)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/sebastianMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }

    @Override
    public void getRewardMission1(int friendShipLevel, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(1).getTask().getItem().getName(),
                getQuest(1).getTask().getAmount());
        if (friendShipLevel == 2) {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Diamond"), 4);
        } else {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Diamond"), 2);
        }
    }

    @Override
    public void getRewardMission2(int friendShipLevel, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(2).getTask().getItem().getName(),
                getQuest(2).getTask().getAmount());
        if (friendShipLevel == 2) {
            currentPlayer.changeMoney(10000);
        } else {
            currentPlayer.changeMoney(5000);
        }
    }

    @Override
    public void getRewardMission3(int friendShipLevel, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(3).getTask().getItem().getName(),
                getQuest(3).getTask().getAmount());
        if (friendShipLevel == 2) {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Quartz"), 100);
        } else {
            currentPlayer.getInventory().addItem(ItemManager.getItemByName("Quartz"), 50);
        }
    }


}
