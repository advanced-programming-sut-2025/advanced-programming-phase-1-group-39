package com.StardewValley.models.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {
    SOIL('.', true, "soil.png"),
    WATER('~', false, "water.png"),
    WALL('O', false, "wall.png"),
    INDOOR('*', true, "indoor.png"),
    DESTROYED('×', false, "destroyed.png"),
    QUARRY('Q', true,  "quarry.png"),
    SELL_BASKET('B', false, "soil.png"),
    DISABLE(' ', false, "disable.png"),
    PATH('#', true,  "path.png"),
    Lawn('.', true,  "lawn.png"),
    ;

    private final char symbol;
    private final boolean walkable;
    private final String path;

    TileType(char symbol, boolean walkable, String path) {
        this.symbol = symbol;
        this.walkable = walkable;
        this.path = path;
    }

    public boolean isWalkable() { return walkable; }

    public char getSymbol() { return symbol; }

    public String getPath() {
        return "map/tiles/" + path;
    }
    public TextureRegion getTextureRegion() {
        return new TextureRegion(new Texture(Gdx.files.internal(getPath())));
    }
}
