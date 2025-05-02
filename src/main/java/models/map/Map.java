package models.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.Constants;
import models.Enums.Season;
import models.ItemStack;
import models.cropsAndFarming.ForagingCrop;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.ForagingSeed;
import models.cropsAndFarming.Tree;

import java.io.FileReader;

public class Map {
    private final int width = Constants.WORLD_MAP_WIDTH, height = Constants.WORLD_MAP_HEIGHT;
    private Tile[][] tiles;

    public Map() {
        tiles = new Tile[height][width];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                tiles[j][i] = new Tile(i, j);
            }
        }
    }

    public String showFarmTypesInfo() {
        StringBuilder text = new StringBuilder();

        try (FileReader reader = new FileReader("src/main/resources/data/Map/farmTypes.json")) {
            Gson gson = new Gson();
            JsonArray array = gson.fromJson(reader, JsonArray.class);
            text.append("1\n");
            text.append(array.get(0).getAsJsonObject().get("mapType").getAsString() + "\n");
            text.append(array.get(0).getAsJsonObject().get("description").getAsString() + "\n");
            text.append("2\n");
            text.append(array.get(1).getAsJsonObject().get("mapType").getAsString() + "\n");
            text.append(array.get(1).getAsJsonObject().get("description").getAsString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text.toString();
    }

    // player number is from 1 to 4
    public void addRandomFarm(FarmType farmType, int playerNumber) {
        try (FileReader reader = new FileReader("src/main/resources/data/Map/farmTypes.json")) {
            Gson gson = new Gson();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            int id = 0;
            if (farmType == FarmType.LAKE_FARM) {
                id = 1;
            }
            JsonObject farmObject = array.get(id).getAsJsonObject(); // FarmType

            int startX, startY;
            switch (playerNumber) {
                case 1:
                    startX = Constants.WORLD_MAP_WIDTH - Constants.FARM_WIDTH;
                    startY = 0;
                    break;
                case 2:
                    startX = 0;
                    startY = 0;
                    break;
                case 3:
                    startX = 0;
                    startY = Constants.WORLD_MAP_HEIGHT - Constants.FARM_HEIGHT;
                    break;
                default:
                    startX = Constants.WORLD_MAP_WIDTH - Constants.FARM_WIDTH;
                    startY = Constants.WORLD_MAP_HEIGHT - Constants.FARM_HEIGHT;
                    break;
            }
            // fixeds
            JsonObject fixedElements = farmObject.getAsJsonObject("fixedElements");
            // adding cabin, greenhouse, main lake
            JsonObject cabin = fixedElements.getAsJsonObject("cabin");
            addObjectToMap(cabin, "building", startX, startY);

            JsonObject greenhouse = fixedElements.getAsJsonObject("greenhouse");
            addObjectToMap(greenhouse, "building", startX, startY);

            JsonObject quarry = fixedElements.getAsJsonObject("quarry");
            addObjectToMap(quarry, "quarry", startX, startY);

            JsonArray lakes = fixedElements.getAsJsonArray("lakes");
            JsonObject mainLake = lakes.get(0).getAsJsonObject();
            addObjectToMap(mainLake, "lake", startX, startY);
            // add small lake
            if (farmType == FarmType.LAKE_FARM) {
                JsonObject smallLake = lakes.get(1).getAsJsonObject();
                addObjectToMap(smallLake, "lake", startX, startY);
            }

            // random fill map
            fillFarmWithRandoms(startX, startY, 0.25);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addObjectToMap(JsonObject object, String type, int startX, int startY) {
        int x, y, h, w;
        x = object.get("x").getAsInt() + startX;
        y = object.get("y").getAsInt() + startY;
        w = object.get("w").getAsInt();
        h = object.get("h").getAsInt();

        if (type.equals("building")) {
            for (int i = x; i < x + w; i++) {
                tiles[y][i].setType(TileType.WALL);
                tiles[y + h - 1][i].setType(TileType.WALL);
            }
            for (int j = y ; j < y + h ; j++) {
                tiles[j][x].setType(TileType.WALL);
                tiles[j][x + w - 1].setType(TileType.WALL);
            }

            for (int i = x + 1; i < x + w - 1; i++) {
                for (int j = y + 1; j < y + h - 1; j++) {
                    tiles[j][i].setType(TileType.INDOOR);
                }
            }

            JsonObject door = object.getAsJsonObject("door");
            int doorX = door.get("x").getAsInt() + startX;
            int doorY = door.get("y").getAsInt() + startY;
            tiles[doorY][doorX].setType(TileType.INDOOR);
        }
        else if (type.equals("quarry")) {
            for (int i = x; i < x + w; i++) {
                for (int j = y; j < y + h; j++) {
                    tiles[j][i].setType(TileType.QUARRY);
                }
            }
        } else if (type.equals("lake")) {
            for (int i = x; i < x + w; i++) {
                for (int j = y; j < y + h; j++) {
                    tiles[j][i].setType(TileType.WATER);
                }
            }

            // empty tiles
            JsonArray emptyTiles = object.getAsJsonArray("emptyTiles");

            for (JsonElement element : emptyTiles) {
                JsonObject tile = element.getAsJsonObject();
                int tileX = tile.get("x").getAsInt() + startX;
                int tileY = tile.get("y").getAsInt() + startY;

                tiles[tileY][tileX].setType(TileType.SOIL);
            }
        }
    }

    private void fillFarmWithRandoms(int startX, int startY, double possibility) {
//        Season nowSeason = App.getApp().getCurrentGame().getTime().getSeason();
        Season nowSeason = Season.SPRING;
        for (int i = startX; i < startX + Constants.FARM_WIDTH; i++) {
            for (int j = startY; j < startY + Constants.FARM_HEIGHT; j++) {
                Tile tile = tiles[j][i];

                if (tile.canAddItemToTile()) {
                    if (Math.random() < possibility) {
                        // TODO : should be corrected in loading
                        if (Math.random() < 0.1) {
                            ForagingCrop randomCrop = ForagingManager.getRandomCrop(nowSeason);
                            tile.placeItem(new ItemStack(randomCrop, 1));
                        } else if (Math.random() < 0.5) {
                            Tree randomTree = ForagingManager.getRandomTree(nowSeason, tile);
                            tile.plantTree(randomTree);
                        } else {
                            // TODO : add wood and stoneش
                        }

                    }
                }

            }
        }
    }


    public String printWholeMap() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                text.append(tiles[i][j].getSymbol() + " ");
            }
            text.append("\n");
        }

        return text.toString();
    }

    public String printMapBySize(int x, int y, int size) {
        StringBuilder text = new StringBuilder();
        for (int i = y - size/2; i < y + size - size/2; i++) {
            for (int j = x - size/2; j < x + size - size/2; j++) {
                if (i < 0 || i > height || j < 0 || j > width) {continue;}
                Tile tile = tiles[i][j];
                text.append(tile.getTileColor() + " " + tile.getSymbol() + " " + AnsiColors.ANSI_RESET);
            }
            text.append("\n");
        }

        return text.toString();
    }

    //Todo : Add chars to tile getColor and getSymbol
    public String printColorMap() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = tiles[i][j];
                text.append(tile.getTileColor() + " " + tile.getSymbol() + " " + AnsiColors.ANSI_RESET);
            }
            text.append("\n");
        }

        return text.toString();
    }

    public String helpReadingMap() {
        return "Soil : .\n"
                + "Water : ~\n"
                + "Wall : O\n"
                + "Indoor : *\n"
                + "Quarry : Q\n"
                + "Plant : P\n"
                + "Tree : T\n"
                + "Craft : C\n"
                + "Artisan Machine : M\n"
                + "Stone : S\n"
                + "Wood : W\n";
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }
}

