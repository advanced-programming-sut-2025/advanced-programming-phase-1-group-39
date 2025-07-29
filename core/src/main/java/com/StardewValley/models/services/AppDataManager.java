package com.StardewValley.models.services;


import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.saveClasses.UserData;
import com.StardewValley.models.saveClasses.UsersData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class AppDataManager {
    private static final String USERS_DATA_PATH = "projectData/users.json";
    private static final String GAMES_DATA_PATH = "projectData/games/";

    private static UsersData usersData;
    private static final Kryo kryo = new Kryo();
    static {
        kryo.setReferences(true);

    }
    private static final Gson gson = new GsonBuilder().setPrettyPrinting()
            // ITEM Abstract class
//            .registerTypeAdapterFactory(
//                RuntimeTypeAdapterFactory
//                    .of(Item.class, "classType")
//
//                    .registerSubtype(CraftingItem.class, "CraftingItem")
//
//                    .registerSubtype(ArtisanGood.class, "ArtisanGood")
//
//                    .registerSubtype(Fish.class, "Fish")
//                    .registerSubtype(Food.class, "Food")
//                    .registerSubtype(ForagingMaterial.class, "ForagingMaterial")
//                    .registerSubtype(ForagingMineral.class, "ForagingMineral")
//                    .registerSubtype(OddItems.class, "OddItems")
//                    .registerSubtype(AnimalProduct.class, "AnimalProduct")
//
//                    // 🔹 ShopItem ← ForagingCrop, ForagingSeed, FarmingProduct, Crop, Seed
//                    .registerSubtype(ForagingCrop.class, "ForagingCrop")
//                    .registerSubtype(ForagingSeed.class, "ForagingSeed")
//                    .registerSubtype(FarmingProduct.class, "FarmingProduct")
//                    .registerSubtype(Seed.class, "Seed")
//
//                    // 🔹 Tool ← Axe, Pickaxe, Hoe, Scythe, Shear, WateringCan, FishingPole, MilkPail
//                    .registerSubtype(Axe.class, "Axe")
//                    .registerSubtype(Pickaxe.class, "Pickaxe")
//                    .registerSubtype(Hoe.class, "Hoe")
//                    .registerSubtype(Scythe.class, "Scythe")
//                    .registerSubtype(Shear.class, "Shear")
//                    .registerSubtype(WateringCan.class, "WateringCan")
//                    .registerSubtype(FishingPole.class, "FishingPole")
//                    .registerSubtype(MilkPail.class, "MilkPail")
//
//                    // 🔹 ArtisanMachine ← BeeHouse, Loom, Keg, CharcoalKiln, CheesePress, PreservesJar, OilMaker, Furnace, Dehydrator, FishSmoker, MayonnaiseMachine
//                    .registerSubtype(BeeHouse.class, "BeeHouse")
//                    .registerSubtype(Loom.class, "Loom")
//                    .registerSubtype(Keg.class, "Keg")
//                    .registerSubtype(CharcoalKiln.class, "CharcoalKiln")
//                    .registerSubtype(CheesePress.class, "CheesePress")
//                    .registerSubtype(PreservesJar.class, "PreservesJar")
//                    .registerSubtype(OilMaker.class, "OilMaker")
//                    .registerSubtype(Furnace.class, "Furnace")
//                    .registerSubtype(Dehydrator.class, "Dehydrator")
//                    .registerSubtype(FishSmoker.class, "FishSmoker")
//                    .registerSubtype(MayonnaiseMachine.class, "MayonnaiseMachine")
//
//
//                    .registerSubtype(FishingShop.FishingShopItem.class, "FishingShopItem")
//                    .registerSubtype(MarniesRanch.LivestockItem.class, "LivestockItem")
//                    .registerSubtype(PierresGeneralStore.SeasonalItem.class, "SeasonalItem")
//                    .registerSubtype(ShopItem.class, "ShopItem")
//
//
//            )
//
//            .registerTypeAdapterFactory(
//                    RuntimeTypeAdapterFactory
//                            .of(NPC.class, "classType")
//                            .registerSubtype(Pierre.class, "Pierre")
//                            .registerSubtype(Gus.class, "Gus")
//                            .registerSubtype(Marnie.class, "Marnie")
//                            .registerSubtype(RobinNPC.class, "RobinNPC")
//                            .registerSubtype(AbigailNPC.class, "AbigailNPC")
//                            .registerSubtype(HarveyNPC.class, "HarveyNPC")
//                            .registerSubtype(Willy.class, "Willy")
//                            .registerSubtype(Clint.class, "Clint")
//                            .registerSubtype(Morris.class, "Morris")
//                            .registerSubtype(LeahNPC.class, "LeahNPC")
//                            .registerSubtype(SebastianNPC.class, "SebastianNPC")
//            )
//            .registerTypeAdapterFactory(
//                    RuntimeTypeAdapterFactory
//                            .of(Building.class, "classType")
//                            .registerSubtype(Cabin.class, "Cabin")
//                            .registerSubtype(ShippingBin.class, "ShippingBin")
//                            .registerSubtype(Well.class, "Well")
//                            .registerSubtype(GreenHouse.class, "GreenHouse")
//
//                            .registerSubtype(AnimalBuilding.class, "AnimalBuilding")
//
//                            .registerSubtype(BlackSmithShop.class, "BlackSmithShop")
//                            .registerSubtype(CarpentersShop.class, "CarpentersShop")
//                            .registerSubtype(PierresGeneralStore.class, "PierresGeneralStore")
//                            .registerSubtype(JojaMartShop.class, "JojaMartShop")
//                            .registerSubtype(MarniesRanch.class, "MarniesRanch")
//                            .registerSubtype(StardropSaloon.class, "StardropSaloon")
//                            .registerSubtype(FishingShop.class, "FishingShop")
//            )
            .create();

    public static void saveApp() {
        App app = App.getApp();
        // save users (json)
        if (app.isStayLoggedIn())
            saveUsers(app.getUsers(), app.getLoggedInUser());
        else
            saveUsers(app.getUsers(), null);

        // save current Game in app

    }

    public static void loadApp() {
        ItemManager.loadItems();

        App app = App.getApp();

        loadUsersFromFile();
        loadAppDetails(app);

        // loading user game -> after he clicked on load game
        // TODO : map?!


// old
//        File file = new File(APP_FILE_PATH);
//        if (!file.exists() || file.length() == 0) {
//            App app = App.getApp();
//        } else {
//            try (FileReader reader = new FileReader(file)) {
//                App loadedApp = gson.fromJson(reader, App.class);
//                App.setInstance(loadedApp);
//                for (User user : App.getApp().getUsers()) {
//                    user.ensureInitialized();
//                }
//
//                //Game game = App.getApp().getCurrentGame();
//                //game.setGameMapRandom();
//                //game.startGame();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // added
//            GameAssetManager.initializeAssets();
//        }
    }

    // Users
    public static void saveUsers(ArrayList<User> users, User loggedInUser) {
        ArrayList<UserData> usersData = new ArrayList<>();
        for (User user : users) {
            usersData.add(new UserData(user));
        }

        UserData loggedInUserData = null;
        if (loggedInUser != null) {
            loggedInUserData = new UserData(loggedInUser);
        }

        FileHandle file = Gdx.files.local(USERS_DATA_PATH);
        file.writeString(gson.toJson(new UsersData(usersData, loggedInUserData)), false);
    }

    public static void loadUsersFromFile() {
        FileHandle file = Gdx.files.local(USERS_DATA_PATH);

        if (!file.exists()) {
            Gdx.app.log("UserManager", "Save file not found. Creating a new one.");
            usersData = new UsersData(new ArrayList<>(), null);
            return;
        }

        usersData = gson.fromJson(file.readString(), UsersData.class);
    }

    public static void loadAppDetails(App app) {
        ArrayList<User> users = new ArrayList<>();
        for (UserData userData : usersData.users) {
            users.add(userData.getUser());
        }
        app.setUsers(users);

        UserData loggedInUserData = usersData.loggedInUser;
        if (loggedInUserData != null) {
            app.setLoggedInUser(loggedInUserData.getUser());
            app.setCurrentMenu(Menu.MAIN_MENU);
            app.setStayLoggedIn(true);
//           TODO:  app.setLastGameId(); -> num of games
        }
    }


    // Players & Games
    public static void saveGame(Game game) {
        FileHandle file = Gdx.files.local(GAMES_DATA_PATH + "game_" + game.getId() + ".dat");
        if (!file.exists()) {
            Gdx.app.log("GameManager", "Game Save file not found. Creating a new one.");
        }
    }

}


