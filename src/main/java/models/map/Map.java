package models.map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;

public class Map {


    private int width, height;
    private Tile[][] tiles;


    public void readFarmMapData(FarmType farmType) {
        try (FileReader reader = new FileReader("src/main/resources/data/Map/farmTypes.json")) {
            Gson gson = new Gson();
            JsonArray jsonObject = gson.fromJson(reader, JsonArray.class);

            int id = 0;
            if (farmType == FarmType.LAKE_FARM) {
                id = 1;
            }

            JsonObject mapObject = jsonObject.get(id).getAsJsonObject(); // Farm
            System.out.println("width: " + width);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeTiles() {}

    public void printMap(int centerX, int centerY, int size) {}

    public void fillRandom(){};
}

