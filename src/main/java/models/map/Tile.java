package models.map;

import models.ItemStack;
import models.Location;
import models.cropsAndFarming.*;


public class Tile {
    int x, y;
    private TileType type = TileType.SOIL;
    private ItemStack itemOnTile = null;

    private Plant plant = null;
    private Tree tree = null;
//    private PlacedObject placedObject = null;

    private FertilizerType fertilizer = null;

    private boolean canPlant = false;
    private boolean isWatered = false;
    private boolean isFertilized = false;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location getLocation() {
        return new Location(x, y);
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
            if (itemOnTile.getItem() instanceof ForagingCrop) c = 'F';
            else if (itemOnTile.getItem() instanceof ForagingMaterial) {
                if (itemOnTile.getItem().getName().equalsIgnoreCase("Wood")) c = '/';
                else if (itemOnTile.getItem().getName().equalsIgnoreCase("Stone")) c = '●';
                else if (itemOnTile.getItem().getName().equalsIgnoreCase("Grass")) c = '=';
            } else if (itemOnTile.getItem() instanceof ForagingMineral) c = 'M';
        }
        return c;
    }

    public String getTileColor() {
        char c = getSymbol();
        if (c == '.'){
            if (canPlant && isWatered)
                return AnsiColors.ANSI_WATERED_TILE_BACKGROUND;
            else if (canPlant)
                return AnsiColors.ANSI_PLOWED_TILE_BACKGROUND;
            else
                return AnsiColors.ANSI_GOLDEN_BACKGROUND;
        }
        else if (c == '~') return AnsiColors.ANSI_BLUE_BACKGROUND;
        else if (c == '*') return AnsiColors.ANSI_WHITE_BACKGROUND;
        else if (c == 'O') return AnsiColors.ANSI_BLACK_BACKGROUND;
        else if (c == '×') return AnsiColors.ANSI_BLACK_BOLD;
        else if (c == 'Q') return AnsiColors.ANSI_GRAY_BACKGROUND;

        else if (c == 'P') return AnsiColors.ANSI_LIGHT_GREEN_BOLD;
        else if (c == 'T') return AnsiColors.ANSI_BROWN_BOLD + AnsiColors.ANSI_DARK_GREEN_BACKGROUND;
        else if (c == '/') return AnsiColors.ANSI_BROWN_BOLD + AnsiColors.ANSI_GOLDEN_BACKGROUND;

        else if (c == '●' && type.equals(TileType.QUARRY))
            return AnsiColors.ANSI_DARK_GRAY_BOLD + AnsiColors.ANSI_GRAY_BACKGROUND;
        else if (c == '●') return AnsiColors.ANSI_DARK_GRAY_BOLD + AnsiColors.ANSI_GOLDEN_BACKGROUND;

        else if (c == 'F') return AnsiColors.ANSI_ORANGE_BACKGROUND;
        else if (c == 'M') return AnsiColors.ANSI_YELLOW_BACKGROUND + AnsiColors.ANSI_BLACK_BOLD;
        else if (c == '=') return AnsiColors.ANSI_DARK_GREEN_BOLD + AnsiColors.ANSI_GOLDEN_BACKGROUND;

            // basket
        else if (c == 'B') return AnsiColors.ANSI_YELLOW_BACKGROUND + AnsiColors.ANSI_BLACK_BOLD;
        else if (c == '#') return AnsiColors.ANSI_LIGHT_GOLDEN_BACKGROUND;
        else {
            return AnsiColors.ANSI_WHITE;
        }
    }

    public void plow() {
        canPlant = true;
    }
    public void setNotPlow() {
        canPlant = false;
    }
    public boolean isPlowed() {return canPlant;}
    public boolean canPlant() {
        return canPlant && itemOnTile == null && plant == null && tree == null;
    }

    public void removePlant() {
        plant = null;
    }

    public void removeTree() {
        tree = null;
    }

    public void burnTree() {
        tree.burn();
    }
    public void cutDownTree() {
        ItemStack items = tree.cutDown();
        tree = null;
    }

    public void setIsFertilized() { isFertilized = true; }

    public FertilizerType getFertilizer() {
        return fertilizer;
    }
    public void setFertilizer(FertilizerType fertilizer) {
        this.fertilizer = fertilizer;
    }

    public void setIsWatered() { isWatered = true; }
    public void setNotWatered() { isWatered = false; }

    public boolean isFertilized() { return isFertilized; }
    public boolean isWatered() { return isWatered; }

    //planting
    public void plantSeed(String seedName) {
        Plant newPlant = CropManager.createPlantBySeed(seedName, this);
        Tree newTree = TreeManager.getTreeBySeedName(seedName, this);
        if (!canPlant) return;
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
    public boolean canAddItemToTile() {
        if (type != TileType.SOIL) return false;
        if (itemOnTile != null) return false;
        if (tree != null) return false;
        if (plant != null) return false;
        return true;
    }

    public boolean canAddMineralToQuarry() {
        if (type != TileType.QUARRY) return false;
        if (itemOnTile != null) return false;
        return true;
    }

    public void placeItem(ItemStack item) {
        this.itemOnTile = item;
    }

    public void plantTree(Tree tree) {
        this.tree = tree;
    }

    public ItemStack getItemOnTile() {
        return itemOnTile;
    }

    public void removeItemOnTile() {
        itemOnTile = null;
    }

    public Tree getTree() {
        return tree;
    }

    public Plant getPlant() {
        return plant;
    }

    // walk
    public boolean canWalkOnTile() {
        if (!type.isWalkable()) return false;
        if (itemOnTile != null) return false; // TODO : for grass it is walkable
        if (tree != null) return false;
        if (plant != null) return false;
        return true;
    }
}