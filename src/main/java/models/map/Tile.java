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
    private ItemStack itemOnTile = null;
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

    public void placeItem(ItemStack item) {
        this.itemOnTile = item;
    }
    public ItemStack pickItem() {
        ItemStack result = itemOnTile;
        itemOnTile = null;
        return result;
    }

    public ItemStack getItemOnTile() {
        return itemOnTile;
    }

    public boolean canAddItemToTile() {
        if (type != TileType.SOIL && type != TileType.INDOOR &&
                type != TileType.QUARRY && type != TileType.PATH) return false;
        if (itemOnTile != null) return false;
        if (tree != null) return false;
        if (plant != null) return false;
        return true;
    }
}
