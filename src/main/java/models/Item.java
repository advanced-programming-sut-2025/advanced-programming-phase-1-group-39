package models;

public abstract class Item {
    protected String name;
    //Todo: description

    public Item(String name) {
        this.name = name;
    }

    public String getName() { return name; }

}

