package com.StardewValley.graphicViews;

import com.StardewValley.models.Constants;
import com.StardewValley.models.Location;
import com.StardewValley.models.NPC.NPC;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.StardewValley.models.Game;
import com.StardewValley.models.Player;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;

public class MiniMapWidget extends Actor {
    private Game game;
    private Texture mapTexture;
    private Texture playerDotTexture;

    private enum type {
        MapWithoutPlayer,
        Buildings
    }

    public MiniMapWidget(Game game) {
        this.game = game;
        createMapTexture();
        createPlayerDotTexture();
    }

    private void createMapTexture() {
        Map map = game.getMap();
        int width = Constants.WORLD_MAP_WIDTH;
        int height = Constants.WORLD_MAP_HEIGHT;

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = map.getTiles()[y][x];
                if (tile != null) {
                    pixmap.setColor(getColorForTile(tile));
                    pixmap.drawPixel(x, y);
                }
            }
        }
        mapTexture = new Texture(pixmap);
        pixmap.dispose();
    }


    private void createPlayerDotTexture() {
         for (Player player : game.getPlayers()) {
            Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.RED); // رنگ نقطه بازیکن
            pixmap.fill();
            playerDotTexture = new Texture(pixmap);
            pixmap.dispose();
         }
    }

    private Color getColorForTile(Tile tile) {
        switch (tile.getType()) {
            case WATER: return Color.BLUE;
            case WALL: return Color.DARK_GRAY;
            case INDOOR: return Color.BROWN;
            case SOIL: return Color.TAN;
            case PATH: return Color.LIGHT_GRAY;
            default: return Color.FOREST;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(mapTexture, getX(), getY(), getWidth(), getHeight());

        for (Player player : game.getPlayers()) {
            float mapX = (player.getX() / (Map.TILE_SIZE * Constants.WORLD_MAP_WIDTH)) * getWidth();

            float mapY = (player.getY() / (Map.TILE_SIZE * Constants.WORLD_MAP_HEIGHT)) * getHeight();

            batch.draw(playerDotTexture, getX() + mapX, getY() + mapY);
        }

        for (NPC npc : game.getNpcs()) {
            Location npcLoc = npc.getLocation();
            float x = npcLoc.x(),
                y = Constants.WORLD_MAP_HEIGHT - npcLoc.y() - 1;
            float mapX = (x / (Constants.WORLD_MAP_WIDTH)) * getWidth();

            // محور Y را باید معکوس کنیم
            float mapY = (1 - (y / (Constants.WORLD_MAP_HEIGHT))) * getHeight();

            batch.draw(playerDotTexture, getX() + mapX, getY() + mapY);
        }
    }
}
