package com.StardewValley.models.crafting;

import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;

public class CraftingItem extends Item {
    private int sellPrice;
    private Texture texture;

    public CraftingItem(String name, int sellPrice, Texture texture) {
        super(name);
        this.sellPrice = sellPrice;
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
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

