package models.map;

import models.Item;

import java.util.ArrayList;

public class Tile {
    int x, y;
    private TileType type;
    private ArrayList<Item> itemsOnTile;

    boolean isWatered;
    boolean canPlant;
}

