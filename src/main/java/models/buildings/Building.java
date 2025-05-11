package models.buildings;

import models.Location;

public abstract class Building {
    private String name;
    private Location location;
    private int width;
    private int height;

    public Building(String name, Location location, int width, int height) {
        this.name = name;
        this.location = location;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }
}
