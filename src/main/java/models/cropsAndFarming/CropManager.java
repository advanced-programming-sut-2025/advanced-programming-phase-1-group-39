package models.cropsAndFarming;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.map.Tile;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class CropManager {
    private static HashMap<String, CropData> crops = new HashMap<>();

    public static void loadCrops(String pathToJson) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<CropData>>(){}.getType();
            ArrayList<CropData> cropList = gson.fromJson(reader, listType);

            for (CropData cropData : cropList) {
                crops.put(cropData.source, cropData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Plant createPlantBySeed(String seedName, Tile tile) {
        CropData data = crops.get(seedName);
        if (data == null) {
            return null;
        }

        FarmingProduct product = new FarmingProduct(
                data.name,
                tile,
                data.baseSellPrice,
                data.isEdible,
                data.baseEnergy,
                data.baseHealth,
                data.seasons,
                data.canBecomeGiant
        );

        Seed seed = new Seed(data.source, data.seasons);

        Plant plant = new Plant(product, seed, data.stages, data.oneTimeHarvest, data.regrowthTime, data.canBecomeGiant);

        return plant;
    }

    public static String getCropInfo(String cropName) {
        CropData data = crops.get(cropName);
        if (data != null) {
            return data.toString();
        }
        return cropName + " doesn't exist";
    }
}
