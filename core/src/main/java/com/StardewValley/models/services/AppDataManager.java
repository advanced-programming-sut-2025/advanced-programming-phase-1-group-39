package com.StardewValley.models.services;


import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.PlayerInteraction.Friendship;
import com.StardewValley.models.buildings.Building;
import com.StardewValley.models.inventory.Inventory;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.saveClasses.GameData;
import com.StardewValley.models.saveClasses.MapChanges;
import com.StardewValley.models.saveClasses.UserData;
import com.StardewValley.models.saveClasses.UsersData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class AppDataManager {
    private static final String USERS_DATA_PATH = "projectData/users.json";
    public static final String GAMES_DATA_PATH = "projectData/games/";

    private static UsersData usersData;
    private static final Kryo kryo = new Kryo();
    static {
        kryo.setReferences(true);
        registerKryoClasses();
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

    private static void registerKryoClasses() {
        // کلاس‌های اصلی
        kryo.register(GameData.class);
        kryo.register(Player.class);
        kryo.register(Time.class);
        kryo.register(Weather.class);
        kryo.register(Friendship.class);

        // کلاس‌های مربوط به نقشه
        kryo.register(MapChanges.class);
        kryo.register(Tile.class);

        // کلاس‌های درون Player و Inventory
        kryo.register(Inventory.class);
        kryo.register(ItemStack.class);
        kryo.register(Skill.class);
        kryo.register(Location.class);
        // ... هر کلاس دیگری که در Player دارید

        // کلاس‌های ساختمان‌ها (باید تمام زیرکلاس‌های Building را ثبت کنید)
        kryo.register(Building.class);
        // مثال: kryo.register(Cabin.class);
        // مثال: kryo.register(GreenHouse.class);
        // ...

        // Enum ها (در صورت نیاز)
        kryo.register(Season.class);
        // مثال: kryo.register(WeatherType.class);

        // انواع داده استاندارد جاوا
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
    }

    public static void saveApp() {
        App app = App.getApp();
        // save users (json)
        if (app.isStayLoggedIn())
            saveUsers(app.getUsers(), app.getLoggedInUser());
        else
            saveUsers(app.getUsers(), null);
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

        String loggedInUserData = null;
        if (loggedInUser != null) {
            loggedInUserData = loggedInUser.getUserName();
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

        String loggedInUserName = usersData.loggedInUserName;
        if (loggedInUserName != null) {
            User loggedInUser = app.getUserByUsername(loggedInUserName);
            app.setLoggedInUser(loggedInUser);
            app.setCurrentMenu(Menu.MAIN_MENU);
            app.setStayLoggedIn(true);
        }
        app.resetLastGameId();
    }

    // Players & Games
    public static void saveGame(Game game) {
        FileHandle file = Gdx.files.local(getGamePath(game.getId()));
        if (!file.exists()) {
            Gdx.app.log("GameManager", "Game Save file not found. Creating a new one.");
        }

        GameData gameData = new GameData(game);

        file.writeString(new GsonBuilder().create().toJson(gameData), false);
    }

    public static GameData loadGame(int gameId) {
        FileHandle file = Gdx.files.local(getGamePath(gameId));
        if (!file.exists()) {
            Gdx.app.log("Error", "Game File not found!");
            return null;
        }

        GameData gameData = gson.fromJson(file.readString(), GameData.class);
        return gameData;
    }

    public static String getGamePath(int gameId) {
        return GAMES_DATA_PATH + "game_" + gameId + ".json";
    }
}


