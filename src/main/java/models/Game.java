package models;

import models.buildings.Building;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class Game {
    ArrayList<Player> players;
    ArrayList<TradeItem> trades = new ArrayList<>();
    Map gameMap;
    ArrayList<Building> buildings = new ArrayList<>();

    public Game(Player one, Player two, Player three, Player four) {
        this.players = new ArrayList<>(List.of(one, two, three, four));
        this.gameMap = new Map();
    }
}
