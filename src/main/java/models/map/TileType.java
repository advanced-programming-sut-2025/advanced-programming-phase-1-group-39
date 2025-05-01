package models.map;

public enum TileType {
    SOIL('.', true),
    WATER('~', false),
    WALL('O', false),
    INDOOR('*', true),
    DESTROYED('×', false),
    QUARRY('Q', true),
    SELL_BASKET('B', false),
    DISABLE(' ', false),
    ;

    private final char symbol;
    private final boolean walkable;

    TileType(char symbol, boolean walkable) {
        this.symbol = symbol;
        this.walkable = walkable;
    }

    public boolean isWalkable() { return walkable; }

    public char getSymbol() { return symbol; }
}

