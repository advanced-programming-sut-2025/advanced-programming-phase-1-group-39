package com.StardewValley.models.saveClasses;

import com.StardewValley.models.*;
import com.StardewValley.models.PlayerInteraction.Friendship;
import com.StardewValley.models.buildings.Building;

import java.util.ArrayList;

public class GameData {
    public int gameId;

    public ArrayList<Player> players;

    public Time time;
    public Weather todayWeather;
    public Weather tomorrowWeather;

//    public MapChanges mapChanges;

    public ArrayList<Friendship> friendships;
    public ArrayList<Building> buildings;

    public GameData(Game game) {
        gameId = game.getId();

        players = game.getPlayers();
        time = game.getTime();
        todayWeather = game.getTodayWeather();
        tomorrowWeather = game.getTomorrowWeather();
        friendships = game.getFriendships();

    }

    public static void settingOfTransients(GameData gameData) {
        for (Player player : gameData.players) {

            for (ItemStack stack : player.getInventory().getInventoryItems()) {
                if (stack != null && stack.getName() != null) {
                    stack.setItem(ItemManager.getItemByName(stack.getName()));
                }
            }

            ItemStack inHand = player.getInventory().getInHand();
            if (inHand != null && inHand.getName() != null) {
                inHand.setItem(ItemManager.getItemByName(inHand.getName()));
            }
        }
    }
}
