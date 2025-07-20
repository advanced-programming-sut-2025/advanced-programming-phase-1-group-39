package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ForagingMaterial extends Item {
    private final TextureRegion texture;
    int baseSellPrice;
    public ForagingMaterial(String name, int baseSellPrice, TextureRegion texture) {
        super(name);
        this.baseSellPrice = baseSellPrice;
        this.texture = texture;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public ForagingMaterial clone() {
        return new ForagingMaterial(this.getName(), this.baseSellPrice, this.texture);
    }

    @Override
    public String toString() {
        return "ForagingMaterial{" +
                "baseSellPrice=" + baseSellPrice +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }
}
