package com.StardewValley.models;


import com.StardewValley.models.Shops.ShopItem;
import com.StardewValley.models.animals.AnimalProduct;
import com.StardewValley.models.animals.AnimalType;
import com.StardewValley.models.animals.FishType;
import com.StardewValley.models.artisan.*;
import com.StardewValley.models.cooking.FoodManager;
import com.StardewValley.models.crafting.CraftingRecipe;
import com.StardewValley.models.cropsAndFarming.CropManager;
import com.StardewValley.models.cropsAndFarming.ForagingManager;
import com.StardewValley.models.cropsAndFarming.TreeManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class ItemManager {
    static HashMap<ArtisanGood, String> artisanItems = new HashMap<>();
    private static HashMap<String, AnimalProduct> animalProducts = new HashMap<>();
    private static HashMap<String, ShopItem> shopsProducts = new HashMap<>();


    public static void loadItems() {
        CropManager.loadCrops("projectData/resources/data/crops.json");
        CropManager.loadMixedSeeds("projectData/resources/data/MixSeeds.json");
        TreeManager.loadTrees("projectData/resources/data/trees.json");
        ForagingManager.loadCrops("projectData/resources/data/ForagingCrops.json");
        ForagingManager.loadSeeds("projectData/resources/data/ForagingSeeds.json");
        ForagingManager.loadTreeSeeds("projectData/resources/data/ForagingTrees.json");
        ForagingManager.loadMinerals("projectData/resources/data/ForagingMinerals.json");
        ForagingManager.loadMaterials("projectData/resources/data/ForagingMaterials.json");

        for (AnimalProduct product : AnimalType.getAllAnimalProducts()) {
            animalProducts.put(product.getName(), product);
        }

        new BeeHouse("Bee House", 0, new Texture(Gdx.files.internal("crafting/Bee_House.png")));
        new CharcoalKiln("Charcoal Kiln", 0, new Texture(Gdx.files.internal("crafting/Charcoal_Kiln.png")));
        new CheesePress("Cheese Press", 0, new Texture(Gdx.files.internal("crafting/Cheese_Press.png")));
        new Dehydrator("Dehydrator", 0, new Texture(Gdx.files.internal("crafting/Dehydrator.png")));
        new FishSmoker("Fish Smoker", 0, new Texture(Gdx.files.internal("crafting/Fish_Smoker.png")));
        new Furnace("Furnace", 0, new Texture(Gdx.files.internal("crafting/Furnace.png")));
        new Keg("Keg", 0, new Texture(Gdx.files.internal("crafting/Keg.png")));
        new Loom("Loom", 0, new Texture(Gdx.files.internal("crafting/Loom.png")));
        new MayonnaiseMachine("Mayonnaise Machine", 0, new Texture(Gdx.files.internal("crafting/Mayonnaise_Machine.png")));
        new OilMaker("Oil Maker", 0, new Texture(Gdx.files.internal("crafting/Oil_maker.png")));
        new PreservesJar("Preserves Jar", 0, new Texture(Gdx.files.internal("crafting/Preserves_Jar.png")));

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
