package models.buildings;

import models.Location;
import models.map.Map;
import models.map.Tile;
import models.map.TileType;

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

    public Location getLocation() {
        return location;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // should be overrided for some building classes
    public void updateMap(Map map) {
        Tile[][] tiles = map.getTiles();
        int x = this.getLocation().x();
        int y = this.getLocation().y();
        int w = this.getWidth();
        int h = this.getHeight();

        for (int i = x + 1; i < x + w - 1; i++) {
            for (int j = y + 1; j < y + h - 1; j++) {
                tiles[j][i].setType(TileType.INDOOR);
            }
        }

        for (int i = x; i < x + w; i++) {
            tiles[y][i].setType(TileType.WALL);
            tiles[y + h - 1][i].setType(TileType.WALL);
        }
        for (int j = y ; j < y + h ; j++) {
            tiles[j][x].setType(TileType.WALL);
            tiles[j][x + w - 1].setType(TileType.WALL);
        }
    }
}