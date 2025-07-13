package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.Item;

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