package models.map;

public enum TileType {
    SOIL('.', true),
    WATER('~', false),
    WALL('O', false),
    INDOOR('*', true),
    QUARRY('Q', true),
    SELL_BASKET('B', false),
    DISABLE(' ', false),
    ;

    private final char symbol;
    private final boolean walkable;

    private boolean canPlant = false;
    private boolean isWatered = false;
    private boolean isFertalized = false;

    TileType(char symbol, boolean walkable) {
        this.symbol = symbol;
        this.walkable = walkable;
    }

    public boolean isWalkable() { return walkable; }

    public void setCanPlant() { canPlant = true; }
    public void setIsFertalized() { isFertalized = true; }
    public void setIsWatered() { isWatered = true; }

    public boolean canPlant() { return canPlant; }
    public boolean isFertalized() { return isFertalized; }
    public boolean isWatered() { return isWatered; }

    public char getSymbol() { return symbol; }
}

