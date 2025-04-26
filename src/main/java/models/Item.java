package models;

public abstract class Item {
    protected String name;
    protected int baseSellPrice;

    public Item(String name, int baseSellPrice) {
        this.name = name;
        this.baseSellPrice = baseSellPrice;
    }

    public String getName() { return name; }
    public int getBaseSellPrice() { return baseSellPrice; }

}

