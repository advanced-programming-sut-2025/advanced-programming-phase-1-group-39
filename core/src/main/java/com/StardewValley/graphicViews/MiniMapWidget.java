package com.StardewValley.graphicViews;

import com.StardewValley.models.Constants;
import com.StardewValley.models.Location;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.map.FarmType;
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
    private Map map;
    private Texture mapTexture;
    private Texture playerDotTexture;
    private Texture NPCDotTexture;

    private enum type {
        AllMap,
        Farms
    }
    private type currentType = type.AllMap;
    private int playerNum = 0;
    private FarmType farmType;

    public MiniMapWidget(Game game) {
        this.game = game;
        createMapTexture();
        createPlayerAndNPCDotTexture();
    }

    public MiniMapWidget(Map map, FarmType farmType, int playerNum) {
        this.map = map;
        this.currentType = type.Farms;
        this.farmType = farmType;
        this.playerNum = playerNum;
        createMapTexture();
        createPlayerAndNPCDotTexture();
    }

    private void createMapTexture() {
        Map map;
        if (game != null)
            map = game.getMap();
        else
            map = this.map;
        int width = Constants.WORLD_MAP_WIDTH;
        int height = Constants.WORLD_MAP_HEIGHT;
        int farmWidth = Constants.FARM_WIDTH;
        int farmHeight = Constants.FARM_HEIGHT;


        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        if (currentType == type.AllMap) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Tile tile = map.getTiles()[y][x];
                    if (tile != null) {
                        pixmap.setColor(getColorForTile(tile));
                        pixmap.drawPixel(x, y);
                    }
                }
            }
        } else {
            // TODO : set start and end of farm
            pixmap.setColor(new Color(0.8f, 0.6f, 0.3f, 1));
            pixmap.fill();

            Location farmStart = Map.getStartOfFarm(this.playerNum);
            int startX = farmStart.x();
            int startY = farmStart.y();
            int endX = farmWidth + startX;
            int endY = farmHeight + startY;
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    Tile tile = map.getTiles()[y][x];
                    if (tile != null) {
                        pixmap.setColor(getColorForTile(tile));
                        pixmap.drawPixel(x, y);
                    }
                }
            }
        }

        mapTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    private void createPlayerAndNPCDotTexture() {
        Pixmap pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED); // رنگ نقطه بازیکن
        pixmap.fill();
        playerDotTexture = new Texture(pixmap);
        pixmap.dispose();

        Pixmap NPCpixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        NPCpixmap.setColor(Color.PURPLE); // رنگ نقطه NPC
        NPCpixmap.fill();
        NPCDotTexture = new Texture(NPCpixmap);
        NPCpixmap.dispose();
    }

    private Color getColorForTile(Tile tile) {
        if (tile.getPlant() != null) {
            return Color.GREEN;
        }
        if (tile.getTree() != null) {
            return new Color(0, 0.4f, 0, 1); // dark green
        }
        if (tile.getItemOnTile() != null) {
            return Color.ORANGE;
        }

        switch (tile.getType()) {
            case WATER: return Color.ROYAL;
            case WALL: return Color.DARK_GRAY;
            case INDOOR: return Color.BROWN;
            case SELL_BASKET:
                return Color.YELLOW;
            case QUARRY:
                return Color.GRAY; // خاکستری برای معدن
            case PATH:
                return Color.TAN;
            case SOIL:
                if (tile.isPlowed() && tile.isWatered()) {
                    return new Color(0.35f, 0.2f, 0.1f, 1); // قهوه‌ای تیره برای خاک خیس
                } else if (tile.isPlowed()) {
                    return new Color(0.55f, 0.35f, 0.15f, 1); // قهوه‌ای برای خاک شخم‌زده
                } else {
                    return new Color(0.8f, 0.6f, 0.3f, 1); // رنگ شنی/خاکی برای خاک عادی
                }
            case Lawn:
                return Color.FOREST;
            default: return Color.BLACK;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(mapTexture, getX(), getY(), getWidth(), getHeight());

        if (currentType == type.Farms) return;
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

            batch.draw(NPCDotTexture, getX() + mapX, getY() + mapY);
        }
    }
}
