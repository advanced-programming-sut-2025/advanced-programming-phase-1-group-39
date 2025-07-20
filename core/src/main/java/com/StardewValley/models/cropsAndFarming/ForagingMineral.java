package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ForagingMineral extends Item {
    private final TextureRegion texture;
    private ForagingSource source;
    private String description;
    private int baseSellPrice;

    public ForagingMineral(String name, String description, int baseSellPrice, TextureRegion texture) {
        super(name);
        this.texture = texture;
        this.source = ForagingSource.MINERAL;
        this.description = description;
        this.baseSellPrice = baseSellPrice;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    @Override
    public String toString() {
        return "ForagingMineral{" +
                "name='" + name + '\'' +
                ", baseSellPrice=" + baseSellPrice +
                ", description='" + description + '\'' +
                ", source=" + source +
                '}';
    }

    @Override
    protected ForagingMineral clone(){
        return new ForagingMineral(this.getName(), this.description, this.baseSellPrice, this.texture);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }
}
