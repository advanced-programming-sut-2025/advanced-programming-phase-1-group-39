package com.StardewValley.models.artisan;

import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ArtisanGood extends Item {
    private final String description;
    private final int energy;
    private final int sellPrice;
    private final TextureRegion texture;

    public ArtisanGood(String name, String description, int energy, int sellPrice, Texture texture) {
        super(name);
        this.description = description;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.texture = new TextureRegion(texture);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergy() { return energy; }
    public int getSellPrice() { return sellPrice; }
}
