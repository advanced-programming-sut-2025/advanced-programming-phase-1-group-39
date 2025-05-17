package models.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import models.*;
import models.Enums.Season;
import models.NPC.NPC;
import models.Shops.Shop;
import models.artisan.ArtisanMachine;
import models.buildings.Building;
import models.buildings.GreenHouse;
import models.cropsAndFarming.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private final int width = Constants.WORLD_MAP_WIDTH, height = Constants.WORLD_MAP_HEIGHT;
    private Tile[][] tiles;

    private int npcMapStartX = Constants.FARM_WIDTH;
    private int npcMapStartY = Constants.DISABLED_HEIGHT;

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


    public void addRandomFarm(FarmType farmType, int playerNumber, Player player) {
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
            // adding buildings
            Building cabin = player.getBuildingByName("cabin");
            addObjectToMap(cabin, "cabin", 0,0);

            Building greenhouse = player.getBuildingByName("greenhouse");
            addObjectToMap(greenhouse, "greenhouse", 0,0);

            Building shippingBin = player.getBuildingByName("Shipping Bin");
            addObjectToMap(shippingBin, "Shipping Bin", 0,0);


            // adding main lake and quarry from json
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
            ArrayList<Building> buildings = new ArrayList<>(List.of(cabin, greenhouse, shippingBin));

            // random fill map
            fillFarmWithRandoms(startX, startY, 0.25, 0.3, Season.SPRING, true, buildings);
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
        }
    }

    private void addObjectToMap(Building building, String name, int startX, int startY) {
        int x = building.getLocation().x() + startX;
        int y = building.getLocation().y() + startY;
        int w = building.getWidth();
        int h = building.getHeight();

        if (name.equals("cabin") || name.equals("greenhouse") || name.equals("shop")) {
            for (int i = x; i < x + w; i++) {
                tiles[y][i].setType(TileType.WALL);
                tiles[y + h - 1][i].setType(TileType.WALL);
            }
            for (int j = y ; j < y + h ; j++) {
                tiles[j][x].setType(TileType.WALL);
                tiles[j][x + w - 1].setType(TileType.WALL);
            }
            if (name.equals("greenhouse") && !((GreenHouse)building).isBuild()) {
                for (int i = x + 1; i < x + w - 1; i++) {
                    for (int j = y + 1; j < y + h - 1; j++) {
                        tiles[j][i].setType(TileType.DESTROYED);
                    }
                }
            } else {
                for (int i = x + 1; i < x + w - 1; i++) {
                    for (int j = y + 1; j < y + h - 1; j++) {
                        tiles[j][i].setType(TileType.INDOOR);
                    }
                }
            }
            int doorX = x + w/2 - 1;
            int doorY = y + h - 1;
            tiles[doorY][doorX].setType(TileType.INDOOR);
            if (name.equals("greenhouse")) tiles[doorY][doorX].setType(TileType.DESTROYED);
        } else if (name.equals("shippingBin")) {
            tiles[y][x].setType(TileType.SELL_BASKET);
        }
    }


    public void fillFarmWithRandoms(int startX, int startY,
                                    double foragingPossibility, double quarryPossibility,
                                    Season nowSeason, boolean haveTree,
                                    ArrayList<Building> buildings) {
        for (int i = startX; i < startX + Constants.FARM_WIDTH; i++) {
            for (int j = startY; j < startY + Constants.FARM_HEIGHT; j++) {
                Tile tile = tiles[j][i];
                boolean isInBuilding = false;
                for (Building building : buildings) {
                    if (isInBuilding(building, tile)) isInBuilding = true;
                }
                if (isInBuilding) continue;

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
                            if (Math.random() < 0.3) {
                                ForagingCrop randomCrop = ForagingManager.getRandomCrop(nowSeason);
                                tile.placeItem(new ItemStack(randomCrop, 1));
                            } else {
                                ForagingMaterial randomMaterial = ForagingManager.getRandomMaterial();
                                tile.placeItem(new ItemStack(randomMaterial, 1));
                            }
                        }
                    }
                }
                if (tile.canAddMineralToQuarry()) {
                    if (Math.random() < quarryPossibility) {
                        if (Math.random() > 0.3) {
                            tile.placeItem(new ItemStack(ForagingManager.foragingMaterials.get("Stone"), 1));
                        } else {
                            ForagingMineral randomMineral = ForagingManager.getRandomMineral();
                            tile.placeItem(new ItemStack(randomMineral, 1));
                        }
                    }
                }

            }
        }
    }

    private void addNpcMap(ArrayList<Shop> shops) {

        int startX = Constants.FARM_WIDTH;
        int startY = Constants.DISABLED_HEIGHT;

        for (Shop shop : shops) {
            addObjectToMap(shop, "shop", 0, 0);
        }

        try (FileReader reader = new FileReader("src/main/resources/data/Map/npcMap.json")) {
            Gson gson = new Gson();

            JsonObject object = gson.fromJson(reader, JsonObject.class);

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


    public void growWateredPlantsAndTrees() {
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Tile tile = tiles[j][i];
                if (tile.getType() == TileType.SOIL) {
                    Plant plant = tile.getPlant();
                    if (plant != null) {
                        plant.updateDaily();
                    } else if (tile.getTree() != null) {
                        tile.getTree().updateDaily();
                    }
                }
            }
        }
    }

    public void setWaterAllTiles(boolean isWatered) {
        if (isWatered) {
            for(int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Tile tile = tiles[j][i];
                    tile.setIsWatered();
                }
            }
        } else {
            for(int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Tile tile = tiles[j][i];
                    tile.setNotWatered();
                }
            }
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

    public void loadMap(ArrayList<Shop> shops) {
        addDisabledTiles();
        addNpcMap(shops);
    }

    // printing
    public String printMapBySize(int x, int y, int size, ArrayList<Player> players,
                                 ArrayList<NPC> npcs) {
        StringBuilder text = new StringBuilder();
        int i1 = Math.max(y - size/2, 0);
        int i2 = Math.min(y + size - size/2, height);

        int j1 = Math.max(x - size/2, 0);
        int j2 = Math.min(x + size - size/2, width);

        int npcMapStartX = Constants.FARM_WIDTH;
        int npcMapStartY = Constants.DISABLED_HEIGHT;

        String[] playerColors = new String[]{AnsiColors.ANSI_PURPLE_BOLD, AnsiColors.ANSI_RED_BOLD, AnsiColors.ANSI_DARK_GREEN_BOLD, AnsiColors.ANSI_ORANGE_BOLD};
        String[] npcColors = new String[]{
                AnsiColors.ANSI_PURPLE_BOLD, AnsiColors.ANSI_RED_BOLD, AnsiColors.ANSI_DARK_GREEN_BOLD, AnsiColors.ANSI_ORANGE_BOLD,
                AnsiColors.ANSI_BLACK_BOLD, AnsiColors.ANSI_CYAN_BOLD, AnsiColors.ANSI_BROWN_BOLD, AnsiColors.ANSI_BLUE_BOLD,
                AnsiColors.ANSI_DARK_BLUE_BOLD, AnsiColors.ANSI_GREEN_BLUE_BOLD, AnsiColors.ANSI_CYAN_BOLD
        };

        int playerCounter = 0;
        int npcCounter = 0;
        for (int i = i1; i < i2; i++) {
            for (int j = j1; j < j2; j++) {
                Tile tile = tiles[i][j];
                if (tile.getType().equals(TileType.DISABLE)){
                    text.append("   ");
                    continue;
                }
                boolean doesSetPlayer = false;
                boolean doesSetNpc = false;


                for (Player player : players) {
                    if (j == player.getLocation().x() && i == player.getLocation().y()) {
                        text.append(tile.getTileColor() + playerColors[playerCounter++] + " @ " + AnsiColors.ANSI_RESET);
                        doesSetPlayer = true;
                        break;
                    }
                }
                if (doesSetPlayer) continue;

                for (NPC npc : npcs) {
                    if (j == npc.getLocation().x() + npcMapStartX && i == npc.getLocation().y() + npcMapStartY) {
                        text.append(AnsiColors.ANSI_REVERSE + npcColors[npcCounter++] + tile.getTileColor() + " N " + AnsiColors.ANSI_RESET);
                        doesSetNpc = true;
                        break;
                    }
                }
                if (doesSetNpc) continue;

                text.append(tile.getTileColor() + " " + tile.getSymbol() + " " + AnsiColors.ANSI_RESET);
            }
            text.append("\n");
        }

        return text.toString();
    }


    public String printColorMap(ArrayList<Player> players, ArrayList<NPC> npcs) {
        return printMapBySize(150, 75, 150, players, npcs);
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
        if (x < 0 || x > width || y < 0 || y > height) return null;
        return tiles[y][x];
    }

    public Tile[][] getTiles() {
        return tiles;
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

    // walking
    public Result canWalkTo(Location start, Location end, Player player, ArrayList<Player> gamePlayers) {
        if (!player.isInPlayerFarm(end) && !isInNPCMap(end) && !isInSpouseFarm(end, player, gamePlayers))
            return new Result(false, "You aren't allowed to go to this location!");
        else return new Result(true, "");
    }

    public ArrayList<MapMinPathFinder.Node> findWalkingPath(Location start, Location end, Player player) {
        MapMinPathFinder pathFinder = new MapMinPathFinder();
        return pathFinder.findPath(tiles, start, end);
    }

    // player place check
    public boolean isInSpouseFarm(Location location, Player player, ArrayList<Player> gamePlayers) {
        Player spouse = null;
        for (Player p : gamePlayers) {
            if (p.getUsername().equals(player.getSpouseName())) spouse = p;
        }
        if (spouse != null)
            return spouse.isInPlayerFarm(location);

        return false;
    }

    public boolean isInNPCMap(Location location) {
        return (location.x() >= Constants.FARM_WIDTH &&
                location.x() <= (Constants.WORLD_MAP_WIDTH - Constants.FARM_WIDTH) &&
                location.y() >= Constants.DISABLED_HEIGHT &&
                location.y() <= Constants.WORLD_MAP_HEIGHT - Constants.DISABLED_HEIGHT);
    }

    public boolean isNearWater(Player player) {
        int startX = player.getLocation().x() - 1;
        int startY = player.getLocation().y() - 1;

        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                Tile tile = tiles[j][i];
                if (tile.getType() == TileType.WATER) return true;
            }
        }
        return false;
    }

    public boolean isNearBuilding(Player player, Building building) {
        int startX = player.getLocation().x() - 1;
        int startY = player.getLocation().y() - 1;

        int buildingX = building.getLocation().x();
        int buildingY = building.getLocation().y();
        int width = building.getWidth();
        int height = building.getHeight();

        for (int i = buildingX; i < buildingX + width; i++) {
            for (int j = buildingY; j < buildingY + height; j++) {
                if (player.isNearLocation(new Location(i, j))) return true;
            }
        }

        return false;
    }

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
        return (player.getLocation().x() >= building.getLocation().x() &&
                player.getLocation().x() <= building.getLocation().x() + building.getWidth() &&
                player.getLocation().y() >= building.getLocation().y() &&
                player.getLocation().y() <= building.getLocation().y() + building.getHeight());
    }
    public boolean isInBuilding(Building building, Tile tile) {
        return (tile.getLocation().x() >= building.getLocation().x() &&
                tile.getLocation().x() <= building.getLocation().x() + building.getWidth() &&
                tile.getLocation().y() >= building.getLocation().y() &&
                tile.getLocation().y() <= building.getLocation().y() + building.getHeight());
    }

    public boolean canAddBuilding(Location start, int width, int height) {
        for (int i = start.x(); i < start.x() + width; i++) {
            for (int j = start.y(); j < start.y() + height; j++) {
                Tile tile = tiles[j][i];

                if (!tile.canAddItemToTile()) return false;
            }
        }
        return true;
    }
}

