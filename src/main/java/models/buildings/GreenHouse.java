package models.buildings;

import models.Location;
import models.map.Map;
import models.map.Tile;
import models.map.TileType;

public class GreenHouse extends Building {
    boolean isBuild = false;

    public GreenHouse(Location startLocation) {
        super("greenhouse", startLocation, 8, 8);
    }

    public void build() {
        isBuild = true;
    }
    public boolean isBuild() {
        return isBuild;
    }

    @Override
    public void updateMap(Map map) {
        if (!isBuild) return;
        Tile[][] tiles = map.getTiles();
        int x = this.getLocation().x();
        int y = this.getLocation().y();
        int w = this.getWidth();
        int h = this.getHeight();

        for (int i = x + 1; i < x + w - 1; i++) {
            tiles[y+1][i].setType(TileType.INDOOR);
            for (int j = y + 2; j < y + h - 1; j++) {
                tiles[j][i].setType(TileType.SOIL);
                tiles[j][i].plow();
            }
        }
        tiles[y+h - 1][x + w/2 - 1].setType(TileType.INDOOR);
        tiles[y+1][x + w/2 - 1].setType(TileType.WATER);
    }
}
