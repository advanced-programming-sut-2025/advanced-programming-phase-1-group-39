package com.StardewValley.models.services;


import com.StardewValley.models.*;
import com.StardewValley.models.Enums.DayOfWeek;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.Enums.WeatherStatus;
import com.StardewValley.models.NPC.*;
import com.StardewValley.models.PlayerInteraction.Friendship;
import com.StardewValley.models.Shops.*;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.animals.AnimalProduct;
import com.StardewValley.models.animals.Fish;
import com.StardewValley.models.artisan.*;
import com.StardewValley.models.buildings.*;
import com.StardewValley.models.cooking.Food;
import com.StardewValley.models.crafting.CraftingItem;
import com.StardewValley.models.cropsAndFarming.*;
import com.StardewValley.models.inventory.Inventory;
import com.StardewValley.models.inventory.InventoryType;
import com.StardewValley.models.inventory.TrashType;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.map.TileType;
import com.StardewValley.models.saveClasses.*;
import com.StardewValley.models.tools.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.Input;
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
    private static final Gson gson = registerSubtypes(new GsonBuilder().setPrettyPrinting())
            .create();

    private static GsonBuilder registerSubtypes(GsonBuilder builder) {
        return builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                        .of(Item.class, "classType")

                        .registerSubtype(CraftingItem .class, "CraftingItem")

                        .registerSubtype(ArtisanGood .class, "ArtisanGood")

                        .registerSubtype(Fish.class, "Fish")
                        .registerSubtype(Food .class, "Food")
                        .registerSubtype(ForagingMaterial .class, "ForagingMaterial")
                        .registerSubtype(ForagingMineral.class, "ForagingMineral")
                        .registerSubtype(OddItems.class, "OddItems")
                        .registerSubtype(AnimalProduct.class, "AnimalProduct")

                        // 🔹 ShopItem ← ForagingCrop, ForagingSeed, FarmingProduct, Crop, Seed
                        .registerSubtype(ForagingCrop.class, "ForagingCrop")
                        .registerSubtype(ForagingSeed.class, "ForagingSeed")
                        .registerSubtype(FarmingProduct.class, "FarmingProduct")
                        .registerSubtype(Seed.class, "Seed")

                        // 🔹 Tool ← Axe, Pickaxe, Hoe, Scythe, Shear, WateringCan, FishingPole, MilkPail
                        .registerSubtype(Axe .class, "Axe")
                        .registerSubtype(Pickaxe .class, "Pickaxe")
                        .registerSubtype(Hoe .class, "Hoe")
                        .registerSubtype(Scythe .class, "Scythe")
                        .registerSubtype(Shear .class, "Shear")
                        .registerSubtype(WateringCan.class, "WateringCan")
                        .registerSubtype(FishingPole.class, "FishingPole")
                        .registerSubtype(MilkPail.class, "MilkPail")

                        // 🔹 ArtisanMachine ← BeeHouse, Loom, Keg, CharcoalKiln, CheesePress, PreservesJar, OilMaker, Furnace, Dehydrator, FishSmoker, MayonnaiseMachine
                        .registerSubtype(BeeHouse .class, "BeeHouse")
                        .registerSubtype(Loom .class, "Loom")
                        .registerSubtype(Keg .class, "Keg")
                        .registerSubtype(CharcoalKiln .class, "CharcoalKiln")
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
                        .registerSubtype(ShopItem .class, "ShopItem")


        )

                .registerTypeAdapterFactory(
                        RuntimeTypeAdapterFactory
                                .of(NPC.class, "classType")
                                .registerSubtype(Pierre .class, "Pierre")
                                .registerSubtype(Gus .class, "Gus")
                                .registerSubtype(Marnie .class, "Marnie")
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
                                .registerSubtype(StardropSaloon .class, "StardropSaloon")
                                .registerSubtype(FishingShop .class, "FishingShop")
                );
    }

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
        kryo.register(Tree.class);
        kryo.register(Seed.class);
        kryo.register(Plant.class);

        // کلاس‌های درون Player و Inventory
        kryo.register(Inventory.class);
        kryo.register(ItemStack.class);

        kryo.register(ToolData.class);
        kryo.register(ToolType.class);
        kryo.register(FishingPoleType.class);

        kryo.register(Skill.class);
        kryo.register(Location.class);
        kryo.register(Animal.class);
        kryo.register(AnimalProduct.class); // NO ARG CONSTRUCTOR NOT ADDED
        kryo.register(PlayerNPCInteraction.class);


        // کلاس‌های ساختمان‌ها
        kryo.register(Building.class);
        kryo.register(AnimalBuilding.class);
        kryo.register(Cabin.class);
        kryo.register(Refrigerator.class);

        kryo.register(GreenHouse.class);
        kryo.register(ShippingBin.class);
        kryo.register(Well.class);

        // Enums
        kryo.register(Season.class);
        kryo.register(Season[].class);
        kryo.register(TileType.class);
        kryo.register(FertilizerType.class);
        kryo.register(TrashType.class);
        kryo.register(InventoryType.class);
        kryo.register(FarmType.class);
        kryo.register(DayOfWeek.class);
        kryo.register(WeatherStatus.class);

        // داده استاندارد جاوا
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        kryo.register(String.class);

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
    }

    // Users GSON
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

    // Players & Games KRYO
    public static void saveGame(Game game) {
        FileHandle file = Gdx.files.local(getGamePath(game.getId()));
        if (!file.exists()) {
            Gdx.app.log("GameManager", "Creating a new file for saving game"+game.getId()+".");
        }

        GameData gameData = new GameData(game);
        Gdx.app.log("GameData", "GameData saved and time " + gameData.time.getHourText() + gameData.time.getDayDetail());

        try (Output output = new Output(file.write(false))) {
            kryo.writeObject(output, gameData);
            Gdx.app.log("GameManager", "Game " + game.getId() + " saved successfully.");
        } catch(Exception e) {
            Gdx.app.error("GameManager", "Error saving game " + game.getId(), e);
        }
        /// Test
//        file.writeString(gson.toJson(gameData), false);
    }

    public static GameData loadGame(int gameId) {
        FileHandle file = Gdx.files.local(getGamePath(gameId));
        if (!file.exists()) {
            Gdx.app.log("Error", "Game File not found!");
            return null;
        }

        try (Input input = new Input(file.read())) {
            GameData gameData = kryo.readObject(input, GameData.class);

            gameData.settingOfTransients();
            Gdx.app.log("GameManager", "Game " + gameId + " loaded successfully.");
            return gameData;
        } catch (Exception e) {
            Gdx.app.error("GameManager", "Error loading game " + gameId, e);
            return null;
        }
        /// Test
//        GameData gameData = gson.fromJson(file.readString(), GameData.class);
//        return gameData;
    }

    public static String getGamePath(int gameId) {
        return GAMES_DATA_PATH + "game_" + gameId + ".dat";
    }
}


