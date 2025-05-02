package models;

import models.buildings.Building;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<TradeItem> trades = new ArrayList<>();
    private Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();

    private Time time = new Time();

    public Game(Player one, Player two, Player three, Player four) {
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.gameMap = new Map();
    }

    public Map getMap() {
        return gameMap;
    }

    public void addToHour(int amount) {
        time.addToHour(amount);
        //TODO : Update of map and plants
    }
    public void goToNextDay() {
        time.goToNextDay();
    }
}
