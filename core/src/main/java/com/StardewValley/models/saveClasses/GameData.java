package com.StardewValley.models.saveClasses;

import com.StardewValley.models.*;
import com.StardewValley.models.PlayerInteraction.Friendship;
import com.StardewValley.models.buildings.Building;

import java.util.ArrayList;

public class GameData {
    public int gameId;

    public ArrayList<Player> players;

    public ArrayList<Building> playerBuildings;


    public ArrayList<Friendship> friendships;

    public Time time;
    public Weather todayWeather;
    public Weather tomorrowWeather;

    public long mapRandSeed;
    public MapChanges mapChanges;

    public int currentGiftNumber;

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

        playerBuildings = new ArrayList<>();
        for (Player player : players) {
            playerBuildings.addAll(player.getFarmBuildings());
        }

        currentGiftNumber = game.getCurrentGiftNumber();
    }

    // for kryo loader
    public GameData() {}

    public void settingOfTransients() {
        for (Player player : this.players) {
            for (ItemStack stack : player.getInventory().getInventoryItems()) {

                if (stack != null && stack.getName() != null) {
                    if (stack.getToolData() != null)
                        stack.setItem(stack.getToolData().getTool());
                    else
                        stack.setItem(ItemManager.getItemByName(stack.getName()));
                }
            }

            ItemStack inHand = player.getInventory().getInHand();
            if (inHand != null && inHand.getName() != null) {
                if (inHand.getToolData() != null)
                    inHand.setItem(inHand.getToolData().getTool());
                else
                    inHand.setItem(ItemManager.getItemByName(inHand.getName()));
            }
        }
    }
}
