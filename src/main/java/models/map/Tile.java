package models.map;

import models.Item;

import java.util.ArrayList;

public class Tile {
    int x, y;
    private TileType type = TileType.SOIL;
    private ArrayList<Item> itemsOnTile = new ArrayList<>();

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public TileType getType() {
        return type;
    }
    public char getSymbol() {
        return type.getSymbol();
    }
}

