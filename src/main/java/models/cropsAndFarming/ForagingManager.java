package models.cropsAndFarming;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Enums.Season;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ForagingManager {
    public static HashMap<String, ForagingCrop> foragingCrops = new HashMap<>();
    public static HashMap<String, ForagingSeed> foragingSeeds = new HashMap<>();

    public static void loadCrops(String pathToJson) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<ForagingCropJson>>() {}.getType();
            ArrayList<ForagingCropJson> cropList = gson.fromJson(reader, listType);

            for (ForagingCropJson data : cropList) {
                ForagingCrop crop = new ForagingCrop(
                        data.name,
                        data.baseSellPrice,
                        data.energy,
                        data.source,
                        data.seasons
                );
                foragingCrops.put(data.name, crop);
                System.out.println(crop);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ForagingCropJson {
        String name;
        int baseSellPrice;
        int energy;
        ForagingSource source;
        Season[] seasons;
    }

    public static void loadSeeds(String pathToJson) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<ForagingSeedJson>>() {}.getType();
            ArrayList<ForagingSeedJson> seedList = gson.fromJson(reader, listType);

            for (ForagingSeedJson data : seedList) {
                ForagingSeed seed = new ForagingSeed(
                        data.name,
                        data.seasons
                );
                foragingSeeds.put(data.name, seed);
                System.out.println(seed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ForagingSeedJson {
        String name;
        Season[] seasons;
    }

}
