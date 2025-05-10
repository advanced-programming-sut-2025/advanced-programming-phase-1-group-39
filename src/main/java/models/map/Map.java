package models.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.*;
import models.Enums.Season;
import models.cropsAndFarming.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.PriorityQueue;

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

    public void addRandomFarm(FarmType farmType, int playerNumber) {
        try (FileReader reader = new FileReader("src/main/resources/data/Map/farmTypes.json")) {
            Gson gson = new Gson();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            int id = 0;
            if (farmType == FarmType.LAKE_FARM) {
                id = 1;
            }
            JsonObject farmObject = array.get(id).getAsJsonObject(); // FarmType

            Location location = Map.getStartOfFarm(playerNumber);
            int startX = location.x(), startY = location.y();

            // fixeds
            JsonObject fixedElements = farmObject.getAsJsonObject("fixedElements");
            // adding cabin, greenhouse, main lake
            JsonObject cabin = fixedElements.getAsJsonObject("cabin");
            addObjectToMap(cabin, "building", startX, startY);

            JsonObject greenhouse = fixedElements.getAsJsonObject("greenhouse");
            addObjectToMap(greenhouse, "building_greenhouse", startX, startY);

            JsonObject quarry = fixedElements.getAsJsonObject("quarry");
            addObjectToMap(quarry, "quarry", startX, startY);

            JsonObject sellBasket = fixedElements.getAsJsonObject("sell_basket");
            addObjectToMap(sellBasket, "basket", startX, startY);

            JsonArray lakes = fixedElements.getAsJsonArray("lakes");
            JsonObject mainLake = lakes.get(0).getAsJsonObject();
            addObjectToMap(mainLake, "lake", startX, startY);
            // add small lake
            if (farmType == FarmType.LAKE_FARM) {
                JsonObject smallLake = lakes.get(1).getAsJsonObject();
                addObjectToMap(smallLake, "lake", startX, startY);
            }

            // random fill map
            fillFarmWithRandoms(startX, startY, 0.25, 0.3, Season.SPRING, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addObjectToMap(JsonObject object, String type, int startX, int startY) {
        int x, y, h = 0, w = 0;
        x = object.get("x").getAsInt() + startX;
        y = object.get("y").getAsInt() + startY;

        if (object.has("w")) {
            w = object.get("w").getAsInt();
            h = object.get("h").getAsInt();
        }

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

            int doorX, doorY;
            if (object.has("door")) {
                JsonObject door = object.getAsJsonObject("door");
                doorX = door.get("x").getAsInt() + startX;
                doorY = door.get("y").getAsInt() + startY;
            } else {
                doorX = x + w/2;
                doorY = y + h - 1;
            }

            tiles[doorY][doorX].setType(TileType.INDOOR);
        } else if (type.equals("quarry")) {
            for (int i = x; i < x + w; i++) {
                for (int j = y; j < y + h; j++) {
                    tiles[j][i].setType(TileType.QUARRY);
                }
            }
        } else if (type.equals("basket")) {
            tiles[y][x].setType(TileType.SELL_BASKET);
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
        } else if (type.equals("path")) {
            for (int i = x; i < x + w; i++) {
                for (int j = y; j < y + h; j++) {
                    tiles[j][i].setType(TileType.PATH);
                }
            }
        } else if (type.equals("building_greenhouse")) {
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
                    tiles[j][i].setType(TileType.DESTROYED);
                }
            }

            int doorX, doorY;
            if (object.has("door")) {
                JsonObject door = object.getAsJsonObject("door");
                doorX = door.get("x").getAsInt() + startX;
                doorY = door.get("y").getAsInt() + startY;
            } else {
                doorX = x + w/2;
                doorY = y + h - 1;
            }

            tiles[doorY][doorX].setType(TileType.DESTROYED);
        }
    }

    public void fillFarmWithRandoms(int startX, int startY,
                                     double foragingPossibility, double quarryPossibility,
                                    Season nowSeason, boolean haveTree) {
        for (int i = startX; i < startX + Constants.FARM_WIDTH; i++) {
            for (int j = startY; j < startY + Constants.FARM_HEIGHT; j++) {
                Tile tile = tiles[j][i];

                if (tile.canAddItemToTile()) {
                    if (Math.random() < foragingPossibility) {
                        if (haveTree) {
                            if (Math.random() < 0.05) {
                                ForagingCrop randomCrop = ForagingManager.getRandomCrop(nowSeason);
                                tile.placeItem(new ItemStack(randomCrop, 1));
                            } else if (Math.random() < 0.5) {
                                Tree randomTree = ForagingManager.getRandomTree(nowSeason, tile);
                                tile.plantTree(randomTree);
                            } else {
                                ForagingMaterial randomMaterial = ForagingManager.getRandomMaterial();
                                tile.placeItem(new ItemStack(randomMaterial, 1));
                            }
                        } else {
                            if (Math.random() < 0.5) {
                                ForagingCrop randomCrop = ForagingManager.getRandomCrop(nowSeason);
                                tile.placeItem(new ItemStack(randomCrop, 1));
                            } else {
                                ForagingMaterial randomMaterial = ForagingManager.getRandomMaterial();
                                tile.placeItem(new ItemStack(randomMaterial, 1));
                            }
                        }
                    }
                } else if (tile.canAddMineralToQuarry()) {
                    if (Math.random() < quarryPossibility) {
                        if (Math.random() > 0.3) {
                            tile.placeItem(new ItemStack(ForagingManager.foragingMaterials.get("stone"), 1));
                        } else {
                            ForagingMineral randomMineral = ForagingManager.getRandomMineral();
                            tile.placeItem(new ItemStack(randomMineral, 1));
                        }
                    }
                }

            }
        }
    }

    private void addNpcMap() {
        try (FileReader reader = new FileReader("src/main/resources/data/Map/npcMap.json")) {
            Gson gson = new Gson();

            int startX = Constants.FARM_WIDTH;
            int startY = Constants.DISABLED_HEIGHT;

            JsonObject object = gson.fromJson(reader, JsonObject.class);

            // buildings
            JsonObject buildings = object.getAsJsonObject("buildings");

            JsonObject blacksmith = buildings.getAsJsonObject("blacksmith");
            addObjectToMap(blacksmith, "building", startX, startY);

            JsonObject jojamart = buildings.getAsJsonObject("jojamart");
            addObjectToMap(jojamart, "building", startX, startY);

            JsonObject pierres_store = buildings.getAsJsonObject("pierres_store");
            addObjectToMap(pierres_store, "building", startX, startY);

            JsonObject carpenters_shop = buildings.getAsJsonObject("carpenters_shop");
            addObjectToMap(carpenters_shop, "building", startX, startY);

            JsonObject fish_shop = buildings.getAsJsonObject("fish_shop");
            addObjectToMap(fish_shop, "building", startX, startY);

            JsonObject marnies_ranch = buildings.getAsJsonObject("marnies_ranch");
            addObjectToMap(marnies_ranch, "building", startX, startY);

            JsonObject stardrop_saloon = buildings.getAsJsonObject("stardrop_saloon");
            addObjectToMap(stardrop_saloon, "building", startX, startY);

            // paths
            JsonArray paths = object.getAsJsonArray("paths");

            for (JsonElement element : paths) {
                JsonObject path = element.getAsJsonObject();
                addObjectToMap(path, "path", startX, startY);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDisabledTiles() {
        int width = Constants.WORLD_MAP_WIDTH - 2 * Constants.FARM_WIDTH;
        int height = Constants.DISABLED_HEIGHT;

        int x = Constants.FARM_WIDTH;
        for (int i = x; i < x + width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[j][i].setType(TileType.DISABLE);
            }
            for (int j = Constants.WORLD_MAP_HEIGHT - height; j < Constants.WORLD_MAP_HEIGHT; j++) {
                tiles[j][i].setType(TileType.DISABLE);
            }
        }
    }

    public void loadMap() {
        addDisabledTiles();
        addNpcMap();
    }

    // printing
    public String printMapBySize(int x, int y, int size, ArrayList<Player> players) {
        StringBuilder text = new StringBuilder();
        int i1 = Math.max(y - size/2, 0);
        int i2 = Math.min(y + size - size/2, height);

        int j1 = Math.max(x - size/2, 0);
        int j2 = Math.min(x + size - size/2, width);

        for (int i = i1; i < i2; i++) {
            for (int j = j1; j < j2; j++) {
                Tile tile = tiles[i][j];
                if (tile.getType().equals(TileType.DISABLE)) continue;
                boolean doesSet = false;

                for (Player player : players) {
                    if (j == player.getLocation().x() && i == player.getLocation().y()) {
                        text.append(AnsiColors.ANSI_PURPLE_BOLD + tile.getTileColor() + " @ " + AnsiColors.ANSI_RESET);
                        doesSet = true;
                    }
                }
                if (!doesSet)
                    text.append(tile.getTileColor() + " " + tile.getSymbol() + " " + AnsiColors.ANSI_RESET);
            }
            text.append("\n");
        }

        return text.toString();
    }

    //Todo : Add all chars to tile getColor and getSymbol
    public String printColorMap(ArrayList<Player> players) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                boolean doesSet = false;
                Tile tile = tiles[i][j];
                for (Player player : players) {
                    if (j == player.getLocation().x() && i == player.getLocation().y()) {
                        text.append(AnsiColors.ANSI_PURPLE_BOLD + tile.getTileColor() + " @ " + AnsiColors.ANSI_RESET);
                        doesSet = true;
                    }
                }
                if (!doesSet)
                    text.append(tile.getTileColor() + " " + tile.getSymbol() + " " + AnsiColors.ANSI_RESET);
            }
            text.append("\n");
        }

        return text.toString();
    }

    public String helpReadingMap() {
        return     "Soil : .\n"
                + "Water : ~\n"
                + "Wall : O\n"
                + "Indoor : *\n"
                + "Quarry : Q\n"
                + "Plant : P\n"
                + "Tree : T\n"
                + "Stone : ●\n"
                + "Wood : /\n"
                + "Foragings : F\n"
                + "Quarry Minerals : M\n"

                + "Craft : C\n"
                + "Artisan Machine : M\n"

                + "Path : #\n"
                + "Player : @\n";
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public static Location getStartOfFarm(int number) {
        int startX, startY;
        switch (number + 1) {
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

        return new Location(startX, startY);
    }


    public Result canWalkTo(Location start, Location end, Player player) {
        MapMinPathFinder pathFinder = new MapMinPathFinder();
        if (!player.isInPlayerFarm(end))
            return new Result(false, "The end of path is not in your farm!");
        else return new Result(true, "");
    }

    public ArrayList<MapMinPathFinder.Node> findWalkingPath(Location start, Location end, Player player) {
        MapMinPathFinder pathFinder = new MapMinPathFinder();
        return pathFinder.findPath(tiles, start, end);
    }

    // player place check
    public boolean isNearWater(Player player) {
        int startX = player.getLocation().x() - 1;
        int startY = player.getLocation().y() - 1;

        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                Tile tile = tiles[i][j];
                if (tile.getType() == TileType.WATER) return true;
            }
        }
        return false;
    }

    public boolean isNearGreenHouse(Player player) {
        int startX = player.getLocation().x() - 1;
        int startY = player.getLocation().y() - 1;

        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                Tile tile = tiles[i][j];
                // TODO : complete near building
            }
        }
        return false;
    }
}

