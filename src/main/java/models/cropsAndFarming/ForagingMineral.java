package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;

public class ForagingMineral extends Item {
    private ForagingSource source;
    private String description;
    private int baseSellPrice;

    public ForagingMineral(String name, String description, int baseSellPrice) {
        super(name);
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
        return new ForagingMineral(this.getName(), this.description, this.baseSellPrice);
    }
}
