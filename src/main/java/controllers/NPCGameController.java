package controllers;

import models.*;
import models.Enums.Season;
import models.Enums.WeatherStatus;
import models.NPC.SebastianNPC;
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

    public Result meetNPC(Matcher matcher) {
        String npcName = matcher.group("NPCName");
        String condition;
        String output;
        ArrayList<String> NPCNames = new ArrayList<>(List.of("sebastian", "abigail", "leah", "robin", "harvey"));

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

    public Result


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

    private int canGiveGift(String giftName, Game game) {
        Player player = game.getCurrentPlayer();
        if (!player.getInventory().hasItem(giftName)) {
            return 0;
        } else {
            ItemStack stack = player.getInventory().getItemByName(giftName);
            if (stack.getItem() instanceof Tool) {
                return 1;
            } else if (stack.getItem() instanceof ArtisanMachine) {
                return 2;
            } else {
                return 3;
            }
        }
    }


}
