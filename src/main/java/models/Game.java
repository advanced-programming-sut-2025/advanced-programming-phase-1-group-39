package models;

import models.buildings.Building;
import models.map.Map;
import models.trading.TradeItem;

import java.util.ArrayList;

public class Game {
    ArrayList<Player> players;
    ArrayList<TradeItem> trades;
    Map gameMap;
    ArrayList<Building> buildings;
}
