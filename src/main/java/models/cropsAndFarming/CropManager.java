package models.cropsAndFarming;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.App;
import models.Enums.Season;
import models.map.Tile;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CropManager {
    private static HashMap<String, CropData> crops = new HashMap<>();
    private static HashMap<Season, ArrayList<String>> mixedSeedOptions = new HashMap<>();

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

    public static void loadMixedSeeds(String pathToJson) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(pathToJson)) {
            Type type = new TypeToken<HashMap<Season, ArrayList<String>>>(){}.getType();
            mixedSeedOptions = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FarmingProduct getCropByName(String name) {
        CropData data = null;
        for (CropData check : crops.values()) {
            if (check.name.equalsIgnoreCase(name)) {
                data = check;
            }
        }
        if (data == null) {
            return null;
        }
        FarmingProduct product = new FarmingProduct(
                data.name,
                data.baseSellPrice,
                data.isEdible,
                data.baseEnergy,
                data.baseHealth,
                data.seasons,
                data.canBecomeGiant
        );

        return product;
    }
    public static Seed getSeedByName(String name) {
        CropData data = crops.get(name);
        if (data == null) {
            return null;
        }

        return new Seed(name, data.seasons);
    }
    public static Plant createPlantBySeed(String seedName, Tile tile) {
        if (seedName.equalsIgnoreCase("Mixed Seeds")) {
            Season season = App.getApp().getCurrentGame().getTime().getSeason();
            ArrayList<String> options = mixedSeedOptions.get(season);
            if (options == null || options.isEmpty()) return null;

            String randomCropName = options.get(new Random().nextInt(options.size()));
            return createPlantBySeed(randomCropName, tile);
        }

        CropData data = crops.get(seedName);
        if (data == null) return null;

        FarmingProduct product = new FarmingProduct(
                data.name,
                data.baseSellPrice,
                data.isEdible,
                data.baseEnergy,
                data.baseHealth,
                data.seasons,
                data.canBecomeGiant
        );
        Seed seed = new Seed(data.source, data.seasons);

        return new Plant(tile, product, seed, data.stages, data.oneTimeHarvest, data.regrowthTime, data.canBecomeGiant);
    }


    public static String getCropInfo(String cropName) {
        CropData data = crops.get(cropName);
        if (data != null) {
            return data.toString();
        }
        return cropName + " doesn't exist";
    }
}
