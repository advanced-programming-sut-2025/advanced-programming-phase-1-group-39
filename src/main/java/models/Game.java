package models;

import models.buildings.Building;
import models.map.FarmType;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<String> playerManagers;
    private Player mainPlayer;
    private ArrayList<TradeItem> trades = new ArrayList<>();
    private Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    // first player should be the mainPlayer of game
    public Game(int gameId,Player one, Player two, Player three, Player four) {
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.mainPlayer = one;
        this.gameMap = new Map();

        todayWeather.setWeatherSunny();
    }

    public void startGame() {
        gameMap.loadMap();

        for (Player player : players) {
            resetPlayerLocation(player);
        }
    }

    // Map
    public Map getMap() {
        return gameMap;
    }

    public String printColorMap() {
        return gameMap.printColorMap(players);
    }

    public void addRandomFarmForPlayer(Player player, FarmType farmType) {
        int number = players.indexOf(player);
        if (number == -1) {return;}

        player.setFarmBound(Map.getStartOfFarm(number));
        gameMap.addRandomFarm(farmType, number);
    }

    // time
    public void addToHour(int amount) {
        time.addToHour(amount);
        //TODO : Update of map and plants
    }
    public void addToDay(int amount) {
        time.addToDay(amount);
        // TODO : changing weather
        // TODO : update plants
    }
    public void goToNextDay() {
        time.goToNextDay();
        // TODO : changing weather
    }

    public Time getTime() {
        return time;
    }



    // player
    public void resetPlayerLocation(Player player) {
        player.setLocationRelative(74, 8);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<String> getPlayerManagers() {
        return playerManagers;
    }

    public void addPlayerManager(String playerManager) {
        playerManagers.add(playerManager);
    }
}