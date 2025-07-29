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

    public ArrayList<Friendship> friendships;
    public ArrayList<Building> buildings;

    public long mapRandSeed;
    public MapChanges mapChanges;

    public GameData(Game game) {
        gameId = game.getId();

        players = game.getPlayers();
        time = game.getTime();
        todayWeather = game.getTodayWeather();
        tomorrowWeather = game.getTomorrowWeather();
        friendships = game.getFriendships();

        mapRandSeed = game.getMapRandSeed();
        mapChanges = new MapChanges();
        mapChanges.findMapChanges(game.getMap(), game.getBaseMap());
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
