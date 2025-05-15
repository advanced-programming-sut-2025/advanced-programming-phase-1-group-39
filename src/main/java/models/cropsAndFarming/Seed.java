package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;

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