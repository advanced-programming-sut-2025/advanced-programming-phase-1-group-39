package models.map;

import models.Item;
import models.cropsAndFarming.Plant;
import models.cropsAndFarming.Tree;

import java.util.ArrayList;

public class Tile {
    int x, y;
    private TileType type = TileType.SOIL;
    private ArrayList<Item> itemsOnTile = new ArrayList<>();
    private Plant plant = null;
    private Tree tree = null;

//    private PlacedObject placedObject = null;

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
        char c = type.getSymbol();
        if (plant != null) {
            c = 'P';
        } else if (tree != null) {
            c = 'T';
        }
        return c;
    }
}

