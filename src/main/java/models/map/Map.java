package models.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import models.Constants;

import java.io.FileReader;

public class Map {
    private final int width = Constants.WORLD_MAP_WIDTH, height = Constants.WORLD_MAP_HEIGHT;
    private Tile[][] tiles;

    public Map() {
        tiles = new Tile[width][height];
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

    public void readFarmMapData(FarmType farmType) {
        try (FileReader reader = new FileReader("src/main/resources/data/Map/farmTypes.json")) {
            Gson gson = new Gson();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            int id = 0;
            if (farmType == FarmType.LAKE_FARM) {
                id = 1;
            }

            JsonObject mapObject = array.get(id).getAsJsonObject(); // FarmType

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeTiles() {}

    public void printMap(int centerX, int centerY, int size) {}

    public void fillRandom(){};
}

