package models.cropsAndFarming;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Enums.Season;
import models.map.Tile;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ForagingManager {
    public static HashMap<String, ForagingCrop> foragingCrops = new HashMap<>();
    public static HashMap<String, ForagingSeed> foragingSeeds = new HashMap<>();
    public static HashMap<String, ForagingSeed> foragingTreeSeeds = new HashMap<>();
    public static HashMap<String, ForagingMineral> foragingMinerals = new HashMap<>();
    public static HashMap<String, ForagingMaterial> foragingMaterials = new HashMap<>();


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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadTreeSeeds(String pathToJson) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<ForagingSeedJson>>() {}.getType();
            ArrayList<ForagingSeedJson> seedList = gson.fromJson(reader, listType);

            for (ForagingSeedJson data : seedList) {
                ForagingSeed seed = new ForagingSeed(
                        data.name,
                        data.seasons
                );
                foragingTreeSeeds.put(data.name, seed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ForagingSeedJson {
        String name;
        Season[] seasons;
    }

    public static void loadMinerals(String pathToJson) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<ForagingMineralJson>>() {}.getType();
            ArrayList<ForagingMineralJson> mineralList = gson.fromJson(reader, listType);

            for (ForagingMineralJson data : mineralList) {
                ForagingMineral mineral = new ForagingMineral(
                        data.name,
                        data.description,
                        data.sellPrice
                );
                foragingMinerals.put(data.name, mineral);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ForagingMineralJson {
        String name;
        String description;
        int sellPrice;
    }

    public static void loadMaterials(String pathToJson) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<ForagingMaterialJson>>() {}.getType();
            ArrayList<ForagingMaterialJson> mineralList = gson.fromJson(reader, listType);

            for (ForagingMaterialJson data : mineralList) {
                ForagingMaterial material = new ForagingMaterial(
                        data.name,
                        data.sellPrice
                );
                foragingMaterials.put(data.name, material);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static class ForagingMaterialJson {
        String name;
        int sellPrice;
    }


    public static ForagingCrop getCropByName(String name) {
        if (foragingCrops.containsKey(name)) {
            return foragingCrops.get(name).clone();
        }
        return null;
    }
    public static ForagingSeed getSeedByName(String name) {
        if (foragingSeeds.containsKey(name)) {
            return foragingSeeds.get(name).clone();
        }
        return null;
    }
    public static ForagingMineral getMineralByName(String name) {
        if (foragingMinerals.containsKey(name)) {
            return foragingMinerals.get(name).clone();
        }
        return null;
    }
    public static ForagingMaterial getMaterialByName(String name) {
        if (foragingMaterials.containsKey(name)) {
            return foragingMaterials.get(name).clone();
        }
        return null;
    }


    public static ForagingCrop getRandomCrop(Season season) {
        if (foragingCrops.isEmpty()) return null;

        ArrayList<ForagingCrop> filtered = new ArrayList<>();
        for (ForagingCrop crop : foragingCrops.values()) {
            for (Season s : crop.getSeasons()) {
                if (s == season) {
                    filtered.add(crop);
                    break;
                }
            }
        }

        if (filtered.isEmpty()) return null;

        int randomIndex = (int) (Math.random() * filtered.size());
        return filtered.get(randomIndex).clone();
    }

    public static ForagingSeed getRandomSeed(Season season) {
        if (foragingSeeds.isEmpty()) return null;

        ArrayList<ForagingSeed> filtered = new ArrayList<>();
        for (ForagingSeed seed : foragingSeeds.values()) {
            for (Season s : seed.getSeasons()) {
                if (s == season) {
                    filtered.add(seed);
                    break;
                }
            }
        }

        if (filtered.isEmpty()) return null;

        int randomIndex = (int) (Math.random() * filtered.size());
        return filtered.get(randomIndex).clone();
    }

    public static Tree getRandomTree(Season season, Tile tile) {
        if (foragingTreeSeeds.isEmpty()) return null;

        ArrayList<ForagingSeed> filtered = new ArrayList<>();
        for (ForagingSeed seed : foragingTreeSeeds.values()) {
            for (Season s : seed.getSeasons()) {
                if (s == season) {
                    filtered.add(seed);
                    break;
                }
            }
        }

        if (filtered.isEmpty()) return null;

        int randomIndex = (int) (Math.random() * filtered.size());
        Tree tree = TreeManager.getTreeBySeedName(filtered.get(randomIndex).clone().getName(), tile);
        tree.setStageToLast();
        return tree;
    }

    public static ForagingMineral getRandomMineral() {
        if (foragingMinerals.isEmpty()) return null;

        ArrayList<ForagingMineral> values = new ArrayList<>(foragingMinerals.values());
        int randomIndex = (int) (Math.random() * values.size());
        System.out.println(values.get(randomIndex));
        return values.get(randomIndex).clone();
    }

    public static ForagingMaterial gforagingMaterial() {
        if (foragingMaterials.isEmpty()) return null;

        ArrayList<ForagingMaterial> values = new ArrayList<>(foragingMaterials.values());
        int randomIndex = (int) (Math.random() * values.size());
        System.out.println(values.get(randomIndex));
        return values.get(randomIndex).clone();
    }

    public static String showForagingInfo(String name) {
        if (foragingCrops.containsKey(name)) {
            return foragingCrops.get(name).toString();
        }
        if (foragingSeeds.containsKey(name)) {
            return foragingSeeds.get(name).toString();
        }
        if (foragingMinerals.containsKey(name)) {
            return foragingMinerals.get(name).toString();
        }
        return name + " doesn't exist";
    }
}
