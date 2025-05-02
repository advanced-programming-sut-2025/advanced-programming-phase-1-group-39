package models;

import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;

public class ItemManager {
    public static void loadItems() {
        CropManager.loadCrops("src/main/resources/data/Crops/crops.json");
        TreeManager.loadTrees("src/main/resources/data/Crops/trees.json");
        ForagingManager.loadCrops("src/main/resources/data/Crops/ForagingCrops.json");
        ForagingManager.loadSeeds("src/main/resources/data/Crops/ForagingSeeds.json");
        ForagingManager.loadTreeSeeds("src/main/resources/data/Crops/ForagingTrees.json");
        ForagingManager.loadMinerals("src/main/resources/data/Crops/ForagingMinerals.json");
        ForagingManager.loadMaterials("src/main/resources/data/Crops/ForagingMaterials.json");
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

        return null;
    }
}
