package models.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import models.Enums.Menu;
import models.NPC.*;
import models.Shops.*;
import models.animals.Animal;
import models.animals.AnimalProduct;
import models.animals.Fish;
import models.artisan.*;
import models.buildings.*;
import models.cooking.Food;
import models.crafting.CraftingItem;
import models.cropsAndFarming.*;
import models.tools.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class AppDataManager {
    private static final String APP_FILE_PATH = "src/main/data/app/appData.json";
    private static final Gson gson = new GsonBuilder()

            // ITEM Abstract class
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                    .of(Item.class, "classType")

                    .registerSubtype(CraftingItem.class, "CraftingItem")

                    .registerSubtype(ArtisanGood.class, "ArtisanGood")

                    .registerSubtype(Fish.class, "Fish")
                    .registerSubtype(Food.class, "Food")
                    .registerSubtype(ForagingMaterial.class, "ForagingMaterial")
                    .registerSubtype(ForagingMineral.class, "ForagingMineral")
                    .registerSubtype(OddItems.class, "OddItems")
                    .registerSubtype(AnimalProduct.class, "AnimalProduct")

                    // 🔹 ShopItem ← ForagingCrop, ForagingSeed, FarmingProduct, Crop, Seed
                    .registerSubtype(ForagingCrop.class, "ForagingCrop")
                    .registerSubtype(ForagingSeed.class, "ForagingSeed")
                    .registerSubtype(FarmingProduct.class, "FarmingProduct")
                    .registerSubtype(Seed.class, "Seed")

                    // 🔹 Tool ← Axe, Pickaxe, Hoe, Scythe, Shear, WateringCan, FishingPole, MilkPail
                    .registerSubtype(Axe.class, "Axe")
                    .registerSubtype(Pickaxe.class, "Pickaxe")
                    .registerSubtype(Hoe.class, "Hoe")
                    .registerSubtype(Scythe.class, "Scythe")
                    .registerSubtype(Shear.class, "Shear")
                    .registerSubtype(WateringCan.class, "WateringCan")
                    .registerSubtype(FishingPole.class, "FishingPole")
                    .registerSubtype(MilkPail.class, "MilkPail")

                    // 🔹 ArtisanMachine ← BeeHouse, Loom, Keg, CharcoalKiln, CheesePress, PreservesJar, OilMaker, Furnace, Dehydrator, FishSmoker, MayonnaiseMachine
                    .registerSubtype(BeeHouse.class, "BeeHouse")
                    .registerSubtype(Loom.class, "Loom")
                    .registerSubtype(Keg.class, "Keg")
                    .registerSubtype(CharcoalKiln.class, "CharcoalKiln")
                    .registerSubtype(CheesePress.class, "CheesePress")
                    .registerSubtype(PreservesJar.class, "PreservesJar")
                    .registerSubtype(OilMaker.class, "OilMaker")
                    .registerSubtype(Furnace.class, "Furnace")
                    .registerSubtype(Dehydrator.class, "Dehydrator")
                    .registerSubtype(FishSmoker.class, "FishSmoker")
                    .registerSubtype(MayonnaiseMachine.class, "MayonnaiseMachine")


                    .registerSubtype(FishingShop.FishingShopItem.class, "FishingShopItem")
                    .registerSubtype(MarniesRanch.LivestockItem.class, "LivestockItem")
                    .registerSubtype(PierresGeneralStore.SeasonalItem.class, "SeasonalItem")
                    .registerSubtype(ShopItem.class, "ShopItem")


            )

            .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory
                            .of(NPC.class, "classType")
                            .registerSubtype(Pierre.class, "Pierre")
                            .registerSubtype(Gus.class, "Gus")
                            .registerSubtype(Marnie.class, "Marnie")
                            .registerSubtype(RobinNPC.class, "RobinNPC")
                            .registerSubtype(AbigailNPC.class, "AbigailNPC")
                            .registerSubtype(HarveyNPC.class, "HarveyNPC")
                            .registerSubtype(Willy.class, "Willy")
                            .registerSubtype(Clint.class, "Clint")
                            .registerSubtype(Morris.class, "Morris")
                            .registerSubtype(LeahNPC.class, "LeahNPC")
                            .registerSubtype(SebastianNPC.class, "SebastianNPC")
            )
            .registerTypeAdapterFactory(
                    RuntimeTypeAdapterFactory
                            .of(Building.class, "classType")
                            .registerSubtype(Cabin.class, "Cabin")
                            .registerSubtype(ShippingBin.class, "ShippingBin")
                            .registerSubtype(Well.class, "Well")
                            .registerSubtype(GreenHouse.class, "GreenHouse")

                            .registerSubtype(AnimalBuilding.class, "AnimalBuilding")

                            .registerSubtype(BlackSmithShop.class, "BlackSmithShop")
                            .registerSubtype(CarpentersShop.class, "CarpentersShop")
                            .registerSubtype(PierresGeneralStore.class, "PierresGeneralStore")
                            .registerSubtype(JojaMartShop.class, "JojaMartShop")
                            .registerSubtype(MarniesRanch.class, "MarniesRanch")
                            .registerSubtype(StardropSaloon.class, "StardropSaloon")
                            .registerSubtype(FishingShop.class, "FishingShop")
            )
            .setPrettyPrinting()
            .create();

    public static void saveApp(App app) {
        try (FileWriter writer = new FileWriter(APP_FILE_PATH)) {
            gson.toJson(app, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadApp() {
        ItemManager.loadItems();
        File file = new File(APP_FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            App app = App.getApp();
            initializeDefaults(app);
            saveApp(app);
        } else {
            try (FileReader reader = new FileReader(file)) {
                App loadedApp = gson.fromJson(reader, App.class);
                App.setInstance(loadedApp);
                for (User user : App.getApp().getUsers()) {
                    user.ensureInitialized();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initializeDefaults(App app) {
        app.setLoggedInUser(null);
        app.setRandomPassword(null);
        app.setPendingUser(null);
        app.setRegisterSuccessful(false);
        app.setCurrentGame(null);
        app.setLastGameId(101);
        app.setCurrentMenu(Menu.SIGNUP_MENU);
        app.setUsers(new ArrayList<>());
        app.setGames(new ArrayList<>());
    }
}