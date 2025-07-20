package com.StardewValley.models;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OddItems extends Item {
    private TextureRegion texture;

    public OddItems(String name, TextureRegion textureRegion) {
        super(name);
        this.texture = textureRegion;
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }
}
