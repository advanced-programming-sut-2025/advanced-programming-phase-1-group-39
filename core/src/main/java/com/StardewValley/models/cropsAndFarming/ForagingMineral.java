package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;

public class ForagingMineral extends Item {
    private final Texture texture;
    private ForagingSource source;
    private String description;
    private int baseSellPrice;

    public ForagingMineral(String name, String description, int baseSellPrice, Texture texture) {
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
    public  Texture getTexture() {
        return texture;
    }
}
