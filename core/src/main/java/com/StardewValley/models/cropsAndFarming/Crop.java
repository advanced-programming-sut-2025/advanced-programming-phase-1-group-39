package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Crop extends Item {
    int baseSellPrice;
    boolean canBeEaten;
    int baseEnergy;
    int baseHealth;
    Season[] seasons;


    public Crop(String name) {
        super(name);
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }
}

