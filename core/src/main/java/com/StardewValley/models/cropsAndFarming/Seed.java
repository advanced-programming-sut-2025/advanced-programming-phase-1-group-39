package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.Item;
import com.badlogic.gdx.graphics.Texture;

public class Seed extends Item {
    Season[] seasons;

    public Seed(String name, Season[] seasons) {
        super(name);
        this.seasons = seasons;
    }

    public String getName() {
        return name;
    }
}