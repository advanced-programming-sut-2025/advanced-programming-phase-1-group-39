package com.StardewValley.models.saveClasses;

import com.StardewValley.models.Constants;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;

import java.util.ArrayList;

public class MapChanges {
    public ArrayList<Tile> modifiedTiles = new ArrayList<>();

    public MapChanges() {}

    public void findMapChanges(Map changed, Map base) {
        int height = Constants.WORLD_MAP_HEIGHT;
        int width = Constants.WORLD_MAP_WIDTH;

        Tile[][] changedTiles = changed.getTiles();
        Tile[][] baseTiles = base.getTiles();

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (!changedTiles[j][i].equals(baseTiles[j][i]))
                    modifiedTiles.add(changedTiles[j][i]);
            }
        }
    }

    public Map getMapChanged(Map baseMap) {
        for (Tile tile : modifiedTiles) {
            baseMap.replaceTile(tile);
        }

        return baseMap;
    }
}
