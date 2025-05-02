package models;

import models.buildings.Building;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private Player creator;
    private ArrayList<TradeItem> trades = new ArrayList<>();
    private Map gameMap;
    private ArrayList<Building> buildings = new ArrayList<>();

    private Time time = new Time();

    private Weather todayWeather = new Weather();
    private Weather tomorrowWeather = new Weather();

    // first player should be the creator of game
    public Game(Player one, Player two, Player three, Player four) {
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.creator = one;
        this.gameMap = new Map();

        todayWeather.setWeatherSunny();
    }

    public Map getMap() {
        return gameMap;
    }

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
}
