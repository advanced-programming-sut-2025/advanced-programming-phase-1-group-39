package models.map;

public class Map {
    private int width, height;
    private Tile[][] tiles;
    // TODO: sell baskets


    private void initializeTiles() {}

    public Tile getTile(int y, int x) {
        return tiles[y][x];
    }

    public void printMap(int centerX, int centerY, int size) {}

    public void fillRandom(){};


}

