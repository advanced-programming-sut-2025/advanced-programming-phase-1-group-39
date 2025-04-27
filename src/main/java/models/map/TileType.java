package models.map;

public enum TileType {
    EMPTY('.', true),
    TREE('T', false),
    ROCK('R', false),
    LAKE('~', false),
    GREENHOUSE('G', false),
    CABIN('C', false),
    FORAGE('F', true),
    QUARRY('Q', false);

    private final char symbol;
    private final boolean walkable;


    TileType(char symbol, boolean walkable) {
        this.symbol = symbol;
        this.walkable = walkable;
    }

    public boolean isWalkable() { return walkable; }
}

