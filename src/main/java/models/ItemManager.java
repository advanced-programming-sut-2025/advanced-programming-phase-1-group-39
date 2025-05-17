package models;

import models.Shops.ShopItem;
import models.animals.AnimalProduct;
import models.animals.AnimalType;
import models.animals.FishType;
import models.artisan.*;
import models.cooking.FoodManager;
import models.crafting.CraftingRecipe;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.Tree;
import models.cropsAndFarming.TreeManager;

import java.util.HashMap;

public class ItemManager {
    static HashMap<ArtisanGood, String> artisanItems = new HashMap<>();
    private static HashMap<String, AnimalProduct> animalProducts = new HashMap<>();
    private static HashMap<String, ShopItem> shopsProducts = new HashMap<>();


    public static void loadItems() {
        CropManager.loadCrops("src/main/resources/data/crops.json");
        CropManager.loadMixedSeeds("src/main/resources/data/MixSeeds.json");
        TreeManager.loadTrees("src/main/resources/data/trees.json");
        ForagingManager.loadCrops("src/main/resources/data/ForagingCrops.json");
        ForagingManager.loadSeeds("src/main/resources/data/ForagingSeeds.json");
        ForagingManager.loadTreeSeeds("src/main/resources/data/ForagingTrees.json");
        ForagingManager.loadMinerals("src/main/resources/data/ForagingMinerals.json");
        ForagingManager.loadMaterials("src/main/resources/data/ForagingMaterials.json");

        for (AnimalProduct product : AnimalType.getAllAnimalProducts()) {
            animalProducts.put(product.getName(), product);
        }

        new BeeHouse("Bee House", 0);
        new CharcoalKiln("Charcoal Kiln", 0);
        new CheesePress("Cheese Press", 0);
        new Dehydrator("Dehydrator", 0);
        new FishSmoker("Fish Smoker", 0);
        new Furnace("Furnace", 0);
        new Keg("Keg", 0);
        new Loom("Loom", 0);
        new MayonnaiseMachine("Mayonnaise Machine", 0);
        new OilMaker("OilMaker", 0);
        new PreservesJar("Preserves Jar", 0);
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

    public static void addShopItems(ShopItem item) {
        shopsProducts.put(item.getName(), item);
    }

    public static Item getItemByName(String name) {
        if (CropManager.getCropByName(name) != null) {
            return CropManager.getCropByName(name);
        }
        if (CropManager.getSeedByName(name) != null) {
            return CropManager.getSeedByName(name);
        }
        if (TreeManager.getFruitByName(name) != null) {
            return TreeManager.getFruitByName(name);
        }
        if (TreeManager.getSeedByName(name) != null) {
            return TreeManager.getSeedByName(name);
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
        if (animalProducts.containsKey(name)) {
            return animalProducts.get(name);
        }
        if (shopsProducts.containsKey(name)) {
            return shopsProducts.get(name);
        }



        return null;
    }
}
