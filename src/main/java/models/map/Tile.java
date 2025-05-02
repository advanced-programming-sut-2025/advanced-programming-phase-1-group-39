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
    private Item itemOnTile = null;

    private Plant plant = null;
    private Tree tree = null;
//    private PlacedObject placedObject = null;


    private boolean canPlant = false;
    private boolean isWatered = false;
    private boolean isFertilized = false;

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
        } else if (itemOnTile != null) {

        }
        return c;
    }

    public void plow() {
        setCanPlant();
    }

    public void removePlant() {
        plant = null;
    }
    public void cutDownTree() {
        ItemStack items = tree.cutDown();
        tree = null;
    }

    public void setCanPlant() { canPlant = true; }
    public void setIsFertilized() { isFertilized = true; }
    public void setIsWatered() { isWatered = true; }

    public boolean canPlant() { return canPlant; }
    public boolean isFertilized() { return isFertilized; }
    public boolean isWatered() { return isWatered; }

    //planting
    public void plantSeed(String seedName) {
        Plant newPlant = CropManager.createPlantBySeed(seedName, this);
        Tree newTree = TreeManager.getTreeBySeedName(seedName, this);
        if (newPlant != null) {
            this.plant = newPlant;
        } else if (newTree != null) {
            this.tree = newTree;
        }
    }
    public String showPlant() {
        return plant.toString();
    }

    public void placeItem(Item item) {
        this.itemOnTile = item;
    }
}

