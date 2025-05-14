package controllers;

import models.*;
import models.Enums.Season;
import models.Enums.WeatherStatus;
import models.NPC.PlayerNPCInteraction;
import models.NPC.Quest;
import models.artisan.ArtisanMachine;
import models.tools.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class NPCGameController {

    App app = App.getApp();
    Game game = app.getCurrentGame();
    Player currentPlayer = game.getCurrentPlayer();
    ArrayList<String> NPCNames = new ArrayList<>(List.of("sebastian", "abigail", "leah", "robin", "harvey"));

    public Result meetNPC(Matcher matcher) {
        String npcName = matcher.group("NPCName");
        String condition;
        String output;

        if (!NPCNames.contains(npcName)) {
            return new Result(false, "NPC " + npcName + "is not among the npcs!");
        } else if (npcName.equals("sebastian") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("sebastian").getLocation())) {
            return new Result(false, "to interact with sebastian, you must be next to him.");
        } else if (npcName.equals("abigail") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("abigail").getLocation())) {
            return new Result(false, "to interact with abigail, you must be next to her.");
        } else if (npcName.equals("harvey") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("harvey").getLocation())) {
            return new Result(false, "to interact with harvey, you must be next to him.");
        } else if (npcName.equals("leah") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("leah").getLocation())) {
            return new Result(false, "to interact with leah, you must be next to her.");
        } else if (npcName.equals("robin") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("robin").getLocation())) {
            return new Result(false, "to interact with robin, you must be next to him.");
        } else if (npcName.equals("sebastian")) {
            condition = getConditions(game.getTime(), currentPlayer.getFriendship("sebastian"), game.getTodayWeather());
            output = getDialogueByConditions(condition, "sebastian", game);
            if (currentPlayer.getFriendship("sebastian").isFirstTalking()) {
                setFriendshipScore(currentPlayer.getFriendship("sebastian"), 20);
                currentPlayer.getFriendship("sebastian").setFirstTalking(false);
            }
            return new Result(true, output);
        } else if (npcName.equals("abigail")) {
            condition = getConditions(game.getTime(), currentPlayer.getFriendship("abigail"), game.getTodayWeather());
            output = getDialogueByConditions(condition, "abigail", game);
            if (currentPlayer.getFriendship("abigail").isFirstTalking()) {
                setFriendshipScore(currentPlayer.getFriendship("abigail"), 20);
                currentPlayer.getFriendship("abigail").setFirstTalking(false);
            }
            return new Result(true, output);
        } else if (npcName.equals("harvey")) {
            condition = getConditions(game.getTime(), currentPlayer.getFriendship("harvey"), game.getTodayWeather());
            output = getDialogueByConditions(condition, "harvey", game);
            if (currentPlayer.getFriendship("harvey").isFirstTalking()) {
                setFriendshipScore(currentPlayer.getFriendship("harvey"), 20);
                currentPlayer.getFriendship("harvey").setFirstTalking(false);
            }
            return new Result(true, output);
        } else if (npcName.equals("leah")) {
            condition = getConditions(game.getTime(), currentPlayer.getFriendship("leah"), game.getTodayWeather());
            output = getDialogueByConditions(condition, "leah", game);
            if (currentPlayer.getFriendship("leah").isFirstTalking()) {
                setFriendshipScore(currentPlayer.getFriendship("leah"), 20);
                currentPlayer.getFriendship("leah").setFirstTalking(false);
            }
            return new Result(true, output);
        } else {
            condition = getConditions(game.getTime(), currentPlayer.getFriendship("robin"), game.getTodayWeather());
            output = getDialogueByConditions(condition, "robin", game);
            if (currentPlayer.getFriendship("robin").isFirstTalking()) {
                setFriendshipScore(currentPlayer.getFriendship("robin"), 20);
                currentPlayer.getFriendship("robin").setFirstTalking(false);
            }
            return new Result(true, output);
        }
    }

    public Result giveGift(Matcher matcher) {
        String npcName = matcher.group("NPCName");
        String itemName = matcher.group("item");

        if (!NPCNames.contains(npcName)) {
            return new Result(false, "NPC " + npcName + "is not among the npcs!");
        } else if (npcName.equals("sebastian") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("sebastian").getLocation())) {
            return new Result(false, "to interact with sebastian, you must be next to him.");
        } else if (npcName.equals("abigail") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("abigail").getLocation())) {
            return new Result(false, "to interact with abigail, you must be next to her.");
        } else if (npcName.equals("harvey") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("harvey").getLocation())) {
            return new Result(false, "to interact with harvey, you must be next to him.");
        } else if (npcName.equals("leah") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("leah").getLocation())) {
            return new Result(false, "to interact with leah, you must be next to her.");
        } else if (npcName.equals("robin") && !isNpcNearPlayer(currentPlayer.getLocation(),
                game.getNPC("robin").getLocation())) {
            return new Result(false, "to interact with robin, you must be next to him.");
        } else {
            int mode = canGiveGift(itemName, game, npcName);
            if (mode == 0) {
                return new Result(false, "The " + itemName + " does not exist in your inventory.");
            } else if (mode == 1) {
                return new Result(false, "You cannot give tools as gifts to NPCs.");
            } else if (mode == 2) {
                return new Result(false, "You cannot give non-sellable items as gifts to NPCs.");
            } else if (mode == 3) {
                if (currentPlayer.getFriendship(npcName).isFirstGift()) {
                    setFriendshipScore(currentPlayer.getFriendship(npcName), 200);
                    currentPlayer.getFriendship(npcName).setFirstGift(false);
                }
                currentPlayer.getInventory().pickItem(itemName, 1);
                return new Result(true, "Wow, you really made my day. Thanks for being amazing.");
            } else {
                if (currentPlayer.getFriendship(npcName).isFirstGift()) {
                    setFriendshipScore(currentPlayer.getFriendship(npcName), 50);
                    currentPlayer.getFriendship(npcName).setFirstGift(false);
                }
                currentPlayer.getInventory().pickItem(itemName, 1);
                return new Result(true, "Oh, thank you! That’s sweet of you.");
            }
        }
    }

    public Result showFriendship() {
        StringBuilder output = new StringBuilder();
        for (String npcName : NPCNames) {
            output.append(printFriendShip(npcName));
        }
        return new Result(true, output.toString());
    }

    public Result showQuestsList() {
        StringBuilder output = new StringBuilder();
        output = activeMissions();
        return new Result(true, output.toString());
    }

    public Result finishQuests(Matcher matcher) {
        String index = matcher.group("index");

        if (!isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("sebastian").getLocation()) &&
            !isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("abigail").getLocation()) &&
            !isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("harvey").getLocation()) &&
            !isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("leah").getLocation()) &&
            !isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("robin").getLocation())) {

            return new Result(false, "To complete this quest, you need to be near the target NPC.");
        } else if (Integer.parseInt(index) > 3 || Integer.parseInt(index) < 0) {
            return new Result(false, "Please enter a quest number between 1 and 3.");
        } else if (isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("sebastian").getLocation())) {
            if (getMission(Integer.parseInt(index), "sebastian") == null) {
                return new Result(false, "This quest has already been completed.");
            } else {
                if (!canDoRequest(Integer.parseInt(index), "sebastian")) {
                    return new Result(false, "You're missing something! Gather the required items and return to complete your quest.");
                } else {
                    getReward(Integer.parseInt(index), "sebastian", currentPlayer.getFriendship("sebastian").getFriendshipLevel());
                    deleteQuest(Integer.parseInt(index), "sebastian");
                    return new Result(true, "Well done, adventurer! The quest is complete and your prize awaits.");
                }
            }
        } else if (isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("abigail").getLocation())) {
            if (getMission(Integer.parseInt(index), "abigail") == null) {
                return new Result(false, "This quest has already been completed.");
            } else {
                if (!canDoRequest(Integer.parseInt(index), "abigail")) {
                    return new Result(false, "You're missing something! Gather the required items and return to complete your quest.");
                } else {
                    getReward(Integer.parseInt(index), "abigail", currentPlayer.getFriendship("abigail").getFriendshipLevel());
                    deleteQuest(Integer.parseInt(index), "abigail");
                    return new Result(true, "Well done, adventurer! The quest is complete and your prize awaits.");
                }
            }
        } else if (isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("harvey").getLocation())) {
            if (getMission(Integer.parseInt(index), "harvey") == null) {
                return new Result(false, "This quest has already been completed.");
            } else {
                if (!canDoRequest(Integer.parseInt(index), "harvey")) {
                    return new Result(false, "You're missing something! Gather the required items and return to complete your quest.");
                } else {
                    getReward(Integer.parseInt(index), "harvey", currentPlayer.getFriendship("harvey").getFriendshipLevel());
                    deleteQuest(Integer.parseInt(index), "harvey");
                    return new Result(true, "Well done, adventurer! The quest is complete and your prize awaits.");
                }
            }
        } else if (isNpcNearPlayer(currentPlayer.getLocation(), game.getNPC("leah").getLocation())) {
            if (getMission(Integer.parseInt(index), "leah") == null) {
                return new Result(false, "This quest has already been completed.");
            } else {
                if (!canDoRequest(Integer.parseInt(index), "leah")) {
                    return new Result(false, "You're missing something! Gather the required items and return to complete your quest.");
                } else {
                    getReward(Integer.parseInt(index), "leah", currentPlayer.getFriendship("leah").getFriendshipLevel());
                    deleteQuest(Integer.parseInt(index), "leah");
                    return new Result(true, "Well done, adventurer! The quest is complete and your prize awaits.");
                }
            }
        } else {
            if (getMission(Integer.parseInt(index), "robin") == null) {
                return new Result(false, "This quest has already been completed.");
            } else {
                if (!canDoRequest(Integer.parseInt(index), "robin")) {
                    return new Result(false, "You're missing something! Gather the required items and return to complete your quest.");
                } else {
                    getReward(Integer.parseInt(index), "robin", currentPlayer.getFriendship("robin").getFriendshipLevel());
                    deleteQuest(Integer.parseInt(index), "robin");
                    return new Result(true, "Well done, adventurer! The quest is complete and your prize awaits.");
                }
            }
        }
    }


    // Auxiliary functions :

    private boolean isNpcNearPlayer(Location playerLocation, Location npcLocation) {

        int playerX = playerLocation.x();
        int playerY = playerLocation.y();
        int npcX = npcLocation.x();
        int npcY = npcLocation.y();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (npcX == playerX + dx && npcY == playerY + dy) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setFriendshipScore(PlayerNPCInteraction interaction, int score) {
        int levelScore = interaction.getFriendshipScore() % 200;
        if (interaction.getFriendshipLevel() != 3) {
            if (levelScore + score > 200) {
                interaction.setFriendshipLevel(interaction.getFriendshipLevel() + 1);
                if (interaction.getFriendshipLevel() == 1) {
                    interaction.setActiveMission3(game.getTime().clone());
                    interaction.getActiveMission3().addToDay(interaction.getDaysPassed());
                }
                interaction.setFriendshipScore(interaction.getFriendshipScore() + score);
            }
        } else {
            if (interaction.getFriendshipScore() + score > 800) {
                interaction.setFriendshipScore(799);
            } else {
                interaction.setFriendshipScore(interaction.getFriendshipScore() + score);
            }
        }

    }

    private String getConditions(Time time, PlayerNPCInteraction interaction, Weather weather) {
        StringBuilder conditions = new StringBuilder();
        if (interaction.getFriendshipLevel() == 3) {
            conditions.append("HIGH|");
        } else if (interaction.getFriendshipLevel() == 2) {
            conditions.append("MEDIUM|");
        } else {
            conditions.append("LOW|");
        }

        if (time.getSeason().equals(Season.SPRING)) {
            conditions.append("SPRING|");
        } else if (time.getSeason().equals(Season.SUMMER)) {
            conditions.append("SUMMER|");
        } else if (time.getSeason().equals(Season.FALL)) {
            conditions.append("FALL|");
        } else {
            conditions.append("WINTER|");
        }

        if (time.getHour() < 15) {
            conditions.append("DAY|");
        } else {
            conditions.append("NIGHT|");
        }

        if (weather.getStatus().equals(WeatherStatus.SUNNY)) {
            conditions.append("CLEAR");
        } else {
            conditions.append("BAD WEATHER");
        }

        return conditions.toString();
    }

    private String getDialogueByConditions(String conditions, String NPCName, Game game) {
        String output;
        switch (NPCName) {
            case "sebastian" -> {
                HashMap<String, String> map = game.getNPC("sebastian").getDialogues();
                output = map.get(conditions);
                return output;
            }
            case "abigail" -> {
                HashMap<String, String> map = game.getNPC("abigail").getDialogues();
                output = map.get(conditions);
                return output;
            }
            case "harvey" -> {
                HashMap<String, String> map = game.getNPC("harvey").getDialogues();
                output = map.get(conditions);
                return output;
            }
            case "leah" -> {
                HashMap<String, String> map = game.getNPC("leah").getDialogues();
                output = map.get(conditions);
            }
            case "robin" -> {
                HashMap<String, String> map = game.getNPC("robin").getDialogues();
                output = map.get(conditions);
                return output;
            }
        }
        return null;
    }

    private int canGiveGift(String giftName, Game game, String NPCName) {
        Player player = game.getCurrentPlayer();
        if (!player.getInventory().hasItem(giftName)) {
            return 0;
        } else {
            ItemStack stack = player.getInventory().getItemByName(giftName);
            if (stack.getItem() instanceof Tool) {
                return 1;
            } else if (stack.getItem() instanceof ArtisanMachine) {
                return 2;
            } else if (game.getNPC(NPCName).getFavoriteItems().contains(stack.getItem())) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    private StringBuilder printFriendShip(String NPCName) {
        StringBuilder output = new StringBuilder();
        output.append(NPCName).append(" ->\n").append("friendShip level : ");
        output.append(currentPlayer.getFriendship(NPCName).getFriendshipLevel()).append("\n");
        output.append("friendShip score : ").append(currentPlayer.getFriendship(NPCName).getFriendshipScore()).append("\n");
        if (currentPlayer.getFriendship(NPCName).getFriendshipScore() == 799) {
            output.append("Congratulations! You've maxed out your friendship with ").append(NPCName).append(" !!!\n");
            output.append("---------------");
        }
        if (!NPCName.equals("robin")) {
            output.append("\n");
        }
        return output;
    }

    private StringBuilder activeMissions() {
        StringBuilder output = new StringBuilder();
        output.append("Quests in Progress :");
        int count = 0;
        for (PlayerNPCInteraction friendship : currentPlayer.getAllFriendships()) {
            if (getMission(1, friendship.getNPCName()) != null) {
                count++;
                output.append(count).append(") ").append(getMission(1, friendship.getNPCName())).append("\n");
            }
            if (getMission(2, friendship.getNPCName()) != null && currentPlayer.getFriendship(friendship.getNPCName()).getFriendshipLevel() >= 1) {
                count++;
                output.append(count).append(") ").append(getMission(2, friendship.getNPCName())).append("\n");
            }
            if (getMission(3, friendship.getNPCName()) != null &&
                currentPlayer.getFriendship(friendship.getNPCName()).getFriendshipLevel() >= 1 &&
                friendship.getActiveMission3().isGreater(game.getTime())) {
                count++;
                output.append(count).append(") ").append(getMission(3, friendship.getNPCName())).append("\n");
            }
            output.deleteCharAt(output.length() - 1);
        }
        return output;
    }

    private String getMission(int level, String NPCName) {
        for (Quest quest : game.getNPC(NPCName).getQuests()) {
            if (quest.getLevel() == level) {
                return game.getNPC(NPCName).getMissions().get(level - 1);
            }
        }
        return null;
    }

    private Boolean canDoRequest(int level, String NPCName) {
        Quest quest = game.getNPC(NPCName).getQuest(level);
        return currentPlayer.getInventory().hasEnoughStack(quest.getTask().getItem().getName(), quest.getTask().getAmount());
    }

    private void getReward(int level, String NPCName, int friendShipLevel) {
        if (level == 1) {
            game.getNPC(NPCName).getRewardMission1(friendShipLevel, game);
        } else if (level == 2) {
            game.getNPC(NPCName).getRewardMission2(friendShipLevel, game);
        } else if (level == 3) {
            game.getNPC(NPCName).getRewardMission3(friendShipLevel, game);
        }

    }

    private void deleteQuest(int level, String NPCName) {
        for (int i = 0; i < game.getNPC(NPCName).getQuests().size(); i++) {
            if (game.getNPC(NPCName).getQuests().get(i).getLevel() == level) {
                game.getNPC(NPCName).getQuests().remove(i);
                break;
            }
        }

    }

}
