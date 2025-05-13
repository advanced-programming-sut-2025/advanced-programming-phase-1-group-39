package models;

import models.animals.FishType;
import models.artisan.ArtisanGood;
import models.artisan.ArtisanMachine;
import models.cooking.FoodManager;
import models.crafting.CraftingRecipe;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;

import java.util.HashMap;

public class ItemManager {
    static HashMap<ArtisanGood, String> artisanItems = new HashMap<>();

    public static void loadItems() {
        CropManager.loadCrops("src/main/resources/data/Crops/crops.json");
        TreeManager.loadTrees("src/main/resources/data/Crops/trees.json");
        ForagingManager.loadCrops("src/main/resources/data/Crops/ForagingCrops.json");
        ForagingManager.loadSeeds("src/main/resources/data/Crops/ForagingSeeds.json");
        ForagingManager.loadTreeSeeds("src/main/resources/data/Crops/ForagingTrees.json");
        ForagingManager.loadMinerals("src/main/resources/data/Crops/ForagingMinerals.json");
        ForagingManager.loadMaterials("src/main/resources/data/Crops/ForagingMaterials.json");
    }

    public static void addArtisanGood(ArtisanGood good, String machineName) {
        artisanItems.put(good, machineName);
    }
    public static String getArtisanMachineByGood(String goodName) {
        for (ArtisanGood good : artisanItems.keySet()) {
            if (goodName.equalsIgnoreCase(good.getName())) {
                return artisanItems.get(good);
            }
        }
        return null;
    }
    public static ArtisanGood getArtisanGood(String name) {
        for (ArtisanGood good : artisanItems.keySet()) {
            if (name.equalsIgnoreCase(good.getName())) {
                return good;
            }
        }
        return null;
    }

    public static Item getItemByName(String name) {
        if (CropManager.getCropByName(name) != null) {
            return CropManager.getCropByName(name);
        }
        if (ForagingManager.getCropByName(name) != null) {
            return ForagingManager.getCropByName(name);
        }
        if (ForagingManager.getSeedByName(name) != null) {
            return ForagingManager.getSeedByName(name);
        }
        if (ForagingManager.getMineralByName(name) != null) {
            return ForagingManager.getMineralByName(name);
        }
        if (ForagingManager.getMaterialByName(name) != null) {
            return ForagingManager.getMaterialByName(name);
        }
        if (getArtisanGood(name) != null) {
            return getArtisanGood(name);
        }
        if (FoodManager.getFoodByName(name) != null) {
            return FoodManager.getFoodByName(name);
        }
        if (CraftingRecipe.getItemByName(name) != null) {
            return CraftingRecipe.getItemByName(name);
        }
        if (FishType.getFishByName(name) != null) {
            return FishType.getFishByName(name);
        }


        return null;
    }
}
