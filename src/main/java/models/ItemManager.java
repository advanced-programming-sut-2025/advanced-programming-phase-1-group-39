package models;

import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;

public class ItemManager {
    public static void loadItems() {
        CropManager.loadCrops("src/main/resources/data/crops.json");
        TreeManager.loadTrees("src/main/resources/data/trees.json");
        ForagingManager.loadCrops("src/main/resources/data/ForagingCrops.json");
        ForagingManager.loadSeeds("src/main/resources/data/ForagingSeeds.json");
        ForagingManager.loadTreeSeeds("src/main/resources/data/ForagingTrees.json");
        ForagingManager.loadMinerals("src/main/resources/data/ForagingMinerals.json");
        ForagingManager.loadMaterials("src/main/resources/data/ForagingMaterials.json");
    }
}
