package models.map;

import models.Item;
import models.Player;
import models.artisan.ArtisanMachine;
import models.buildings.Building;

public class Map {
    private int width, height;
    private Tile[][] tiles;
    // TODO: sell baskets


    private void initializeTiles() {}

    public Tile getTile(int x, int y) {
        if (x < 0 || x > width || y < 0 || y > height) return null;
        return tiles[y][x];
    }

    public void printMap(int centerX, int centerY, int size) {}

    public void fillRandom(){};

    public ArtisanMachine getNearArtisanMachine(Player player, String name) {
        int startX = player.getLocation().x() - 1;
        int startY = player.getLocation().y() - 1;

        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                Tile tile = tiles[i][j];
                Item machine = tile.getItemOnTile().getItem();
                if (machine instanceof ArtisanMachine
                        && machine.getName().equalsIgnoreCase(name)) {
                    return (ArtisanMachine) machine;
                }
            }
        }

        return null;
    }

    public boolean isInBuilding(Building building, Player player) {
        return (player.getLocation().x() <= building.getLocation().x() &&
                player.getLocation().x() >= building.getLocation().x() + building.getWidth() &&
                player.getLocation().y() <= building.getLocation().y() &&
                player.getLocation().y() >= building.getLocation().y() + building.getHeight());

    }

}

