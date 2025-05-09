package models;

import models.buildings.Building;
import models.map.FarmType;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private Player mainPlayer;

    private Player playerInTurn;

    private ArrayList<TradeItem> trades = new ArrayList<>();
    private Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    // first player should be the mainPlayer of game
    public Game(Player one, Player two, Player three, Player four) {
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.mainPlayer = one;
        this.playerInTurn = one;
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
        for (int i = 0; i < amount; i++)
            goToNextDay();
    }

    public void goToNextDay() {
        time.goToNextDay();
        // TODO : changing weather
        // TODO : update plants
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

    //turn
    public Player getPlayerInTurn() {
        return playerInTurn;
    }

    public void setPlayerInTurn(Player playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public boolean areAllNotConscious() {
        boolean areAllNotConscious = true;
        for (Player player : players) {
            if (player.isConscious()) {
                areAllNotConscious = false;
                break;
            }
        }
        return areAllNotConscious;
    }

    public boolean nextTurn() {
        if (areAllNotConscious()) return false;

        int index = players.indexOf(playerInTurn);
        int newIndex = index + 1;
        if (newIndex == players.size()) {
            newIndex = 0;
        }

        playerInTurn = players.get(newIndex);
        if (!playerInTurn.isConscious()) return nextTurn();
        return true;
    }
}
