package models.map;

import models.Item;
import models.ItemStack;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.Plant;
import models.cropsAndFarming.Tree;
import models.cropsAndFarming.TreeManager;

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

    public void removePlant() {
        plant = null;
    }
    public void cutDownTree() {
        ItemStack items = tree.cutDown();
        tree = null;
    }

    public boolean canPlant() {
        return type.canPlant();
    }

    public void plantSeed(String seedName) {
        Plant newPlant = CropManager.createPlantBySeed(seedName, this);
        Tree newTree = TreeManager.getTreeBySeedName(seedName, this);
        if (newPlant != null) {
            this.plant = plant;
        } else if (newTree != null) {
            this.tree = tree;
        }
    }
}
