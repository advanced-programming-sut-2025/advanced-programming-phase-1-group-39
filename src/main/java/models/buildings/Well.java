package models.buildings;

import models.Location;
import models.map.Map;
import models.map.Tile;
import models.map.TileType;

public class Well extends Building {
    public Well(String name, Location location, int width, int height) {
        super(name, location, width, height);
    }

    @Override
    public void updateMap(Map map) {
        Tile[][] tiles = map.getTiles();
        int x = this.getLocation().x();
        int y = this.getLocation().y();
        int w = this.getWidth();
        int h = this.getHeight();

        for (int i = x + 1; i < x + w - 1; x++) {
            for (int j = y + 1; j < y + h - 1; j++) {
                tiles[j][i].setType(TileType.WATER);
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
