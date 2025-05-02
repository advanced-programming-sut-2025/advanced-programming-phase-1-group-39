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

    // symbol
    public char getSymbol() {
        char c = type.getSymbol();
        if (plant != null) {
            c = 'P';
        } else if (tree != null) {
            c = 'T';
        } else if (itemOnTile != null) {
            // wood : / , Stone: 0 ,
        }
        return c;
    }

    public String getTileColor() {
        char c = getSymbol();
        if (c == '.') return AnsiColors.ANSI_GOLDEN_BACKGROUND;
        else if (c == '~') return AnsiColors.ANSI_BLUE_BACKGROUND;
        else if (c == '*') return AnsiColors.ANSI_WHITE_BACKGROUND;
        else if (c == 'O') return AnsiColors.ANSI_BLACK_BACKGROUND;
        else if (c == '×') return AnsiColors.ANSI_BLACK_BOLD;
        else if (c == 'Q') return AnsiColors.ANSI_GRAY_BACKGROUND;

        else if (c == 'B') return AnsiColors.ANSI_YELLOW_BOLD;
        else if (c == 'P') return AnsiColors.ANSI_GREEN_BOLD;
        else if (c == 'T') return AnsiColors.ANSI_DARK_GREEN_BOLD;
        else if (c == '/') return AnsiColors.ANSI_BROWN_BOLD;
        else if (c == '0') return AnsiColors.ANSI_DARK_GRAY_BOLD;
        else {
            return AnsiColors.ANSI_WHITE;
        }
    }

    public void plow() {
        canPlant = true;
    }

    public void removePlant() {
        plant = null;
    }
    public void cutDownTree() {
        ItemStack items = tree.cutDown();
        tree = null;
    }

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


    // placed Items
    public void placeItem(Item item) {
        this.itemOnTile = item;
    }

    public Item getItemOnTile() {
        return itemOnTile;
    }

    public Tree getTree() {
        return tree;
    }

    public Plant getPlant() {
        return plant;
    }
}

