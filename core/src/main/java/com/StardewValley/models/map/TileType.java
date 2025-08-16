package com.StardewValley.models.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {
    SOIL('.', true, "soil2.png"),
    WATER('~', false, "water.png"),
    WALL('O', false, "wall.png"),
    INDOOR('*', true, "indoor.png"),
    DESTROYED('×', false, "destroyed.png"),
    QUARRY('Q', true,  "quarry.png"),
    SELL_BASKET('B', false, "soil2.png"),
    DISABLE(' ', false, "disable.png"),
    PATH('#', true,  "path.png"),
    Lawn('.', true,  "lawn.png");

    private final char symbol;
    private final boolean walkable;
    private final String path;

    private transient TextureRegion textureRegion;

    TileType(char symbol, boolean walkable, String path) {
        this.symbol = symbol;
        this.walkable = walkable;
        this.path = path;
    }

    public boolean isWalkable() { return walkable; }
    public char getSymbol() { return symbol; }
    private String getPath() { return "map/tiles/" + path; }

    public static void loadAllTextures() {
        for (TileType type : values()) {
            Texture texture = new Texture(Gdx.files.internal(type.getPath()));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            type.textureRegion = new TextureRegion(texture);
        }
    }


    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}