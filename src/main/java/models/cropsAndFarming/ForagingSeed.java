package models.cropsAndFarming;

import models.Enums.Season;

public class ForagingSeed extends Seed{
    private ForagingSource source;
    private Season[] seasons;

    public ForagingSeed(String name, Season[] seasons) {
        super(name, seasons);
        this.source = ForagingSource.LAND_SEED;
    }

    public Season[] getSeasons() {
        return seasons;
    }

    public ForagingSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "ForagingSeed{name=" + getName() + ", seasons=" + java.util.Arrays.toString(seasons) + "}";
    }

    @Override
    public ForagingSeed clone() {
        return new ForagingSeed(this.getName(), this.getSeasons().clone());
    }
}
