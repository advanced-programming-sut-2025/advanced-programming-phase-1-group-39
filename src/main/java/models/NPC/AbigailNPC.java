package models.NPC;

import models.*;
import models.services.DialogueLoader;
import models.services.MissionsLoader;
import models.tools.ToolType;
import models.tools.WateringCan;

import java.util.ArrayList;
import java.util.List;

public class AbigailNPC extends NPC {

    public AbigailNPC() {
        super(
                "abigail",
                "Explorer ",
                "Abigail is a free-spirited, adventurous soul with a love for exploration and risk-taking.\nShe's very passionate about coffee and pumpkins and enjoys anything that challenges the status quo.\nShe’s bold, independent, and always ready for new experiences, which makes her an exciting NPC to befriend.",
                new Location(95, 22),
                new ArrayList<>(List.of(ItemManager.getItemByName("Stone"), ItemManager.getItemByName("Iron"),
                        ItemManager.getItemByName("Coffee")))
        );
    }

    {
        super.dialogues = DialogueLoader.loadJsonToMap("src/main/resources/data/NPC/abigailDialogues.json");
        super.tasks = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Gold Bar"), 1)
                , new ItemStack(ItemManager.getItemByName("Pumpkin"), 1)
                , new ItemStack(ItemManager.getItemByName("Wheat"), 50)));
        super.missions = MissionsLoader.loadMissionsFromJson("src/main/resources/data/Missions/abigailMissions.json");
        super.quests = generateNPCQuests(tasks, missions);
    }

    @Override
    public void getRewardMission1(int friendShipLevel, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(1).getTask().getItem().getName(),
                getQuest(1).getTask().getAmount());
        if (friendShipLevel == 2) {
            currentPlayer.getFriendship("abigail").setFriendshipLevel(3);
        } else if (friendShipLevel == 1) {
            currentPlayer.getFriendship("abigail").setFriendshipLevel(2);
        }
    }

    @Override
    public void getRewardMission2(int friendShipLevel, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(2).getTask().getItem().getName(),
                getQuest(2).getTask().getAmount());
        if (friendShipLevel == 2) {
            currentPlayer.addMoney(1000);
        } else {
            currentPlayer.addMoney(500);
        }
    }

    @Override
    public void getRewardMission3(int friendShipLevel, Game game) {
        Player currentPlayer = game.getPlayerInTurn();
        currentPlayer.getInventory().pickItem(getQuest(3).getTask().getItem().getName(),
                getQuest(3).getTask().getAmount());

        WateringCan wateringCan = (WateringCan) currentPlayer.getInventory().getItemByName("watering can").getItem();
        wateringCan.upgradeTo(ToolType.IRIDIUM);
    }
}
