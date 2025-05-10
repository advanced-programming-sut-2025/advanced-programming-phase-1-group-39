package models;

import models.buildings.Building;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private ArrayList<TradeItem> trades;
    private Map gameMap;
    private ArrayList<Building> buildings;

    private Player playerInTurn;

    public Player getPlayerInTurn() {
        return playerInTurn;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }
}
