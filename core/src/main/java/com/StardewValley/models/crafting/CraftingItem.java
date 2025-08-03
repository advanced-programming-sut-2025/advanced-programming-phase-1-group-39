package com.StardewValley.models.crafting;

import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CraftingItem extends Item {
    private int sellPrice;
    private TextureRegion texture;

    public CraftingItem(String name, int sellPrice, Texture texture) {
        super(name);
        this.sellPrice = sellPrice;
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.texture = new TextureRegion(texture);
    }

    @Override
    public TextureRegion getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public String toString() {
        return "CraftingItem{" +
                "sellPrice=" + sellPrice +
                ", name='" + name + '\'' +
                '}';
    }
}

