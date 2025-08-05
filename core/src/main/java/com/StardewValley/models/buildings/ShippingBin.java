package com.StardewValley.models.buildings;

import com.StardewValley.models.Location;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.map.TileType;

public class ShippingBin extends Building{
    public ShippingBin(String name, Location location, int width, int height) {
        super(name, location, width, height);
    }
    public ShippingBin() {}

    @Override
    public void updateMap(Map map) {
        Tile[][] tiles = map.getTiles();
        int x = this.getLocation().x();
        int y = this.getLocation().y();
        int w = this.getWidth();
        int h = this.getHeight();

        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                tiles[j][i].setType(TileType.SELL_BASKET);
            }
        }
    }
}
