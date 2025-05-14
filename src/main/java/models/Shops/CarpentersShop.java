package models.Shops;

import com.google.gson.Gson;
import models.*;
import models.NPC.NPC;
import models.animals.LivingPlace;
import models.buildings.AnimalBuilding;
import models.buildings.ShippingBin;
import models.buildings.Well;
import models.inventory.Inventory;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class CarpentersShop extends Shop{
    private HashMap<String, ShopItem> shopItems = new HashMap<>();
    private HashMap<String, BuildingData> buildings = new HashMap<>();

    public CarpentersShop(String name, Location location, int width, int height, String jsonPath,
                          int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            CarpenterShopData data = gson.fromJson(reader, CarpenterShopData.class);

            for (ShopItemData item : data.items) {
                shopItems.put(item.name, new ShopItem(item.name, item.price, item.limit));
            }

            for (BuildingData item : data.buildings) {
                buildings.put(item.name, new BuildingData(
                        item.name, item.description, item.goldCost,
                        item.woodCost, item.stoneCost, item.width, item.height, item.dailyLimit
                ));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class CarpenterShopData {
        List<ShopItemData> items;
        List<BuildingData> buildings;
    }
    public class ShopItemData {
        public String name;
        public int price;
        public int limit;
    }
    public class BuildingData {
        public String name;
        public String description;
        public int goldCost;
        public int woodCost;
        public int stoneCost;
        public int width;
        public int height;
        public int dailyLimit;

        public BuildingData(String name, String description, int goldCost, int woodCost, int stoneCost,
                            int width, int height, int dailyLimit) {
            this.name = name;
            this.description = description;
            this.goldCost = goldCost;
            this.woodCost = woodCost;
            this.stoneCost = stoneCost;
            this.width = width;
            this.height = height;
            this.dailyLimit = dailyLimit;
        }
    }

    public Result purchase(String product, int count) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        ShopItem item = shopItems.get(product);
        if (item == null) {
            return new Result(false, "We don't have any " + product + " in our shop ):");
        }
        if (!player.hasEnoughMoney(item.getPrice() * count)) {
            return new Result(false, "Sorry you don't have enough money ):");
        }

        player.changeMoney(-(item.getPrice() * count));
        Inventory inv = player.getInventory();
        inv.addItem(ItemManager.getItemByName(product), count);
        return new Result(true, "You bought " + product + " x" + count + " successfully. Thank You (:");
    }
    public Result buildFarmBuilding(String name, int x, int y) {
        Game game = App.getApp().getCurrentGame();
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        Inventory inv = player.getInventory();

        //Todo: check validate build building for player
        BuildingData data = buildings.get(name);
        if (data == null) {
            return new Result(false, name + " isn't in our services!");
        }
        if (!player.hasEnoughMoney(data.goldCost)) {
            return new Result(false, "Sorry you doesn't have enough money ):");
        }
        if (!inv.hasEnoughStack("Wood", data.woodCost) || !inv.hasEnoughStack("Stone", data.stoneCost)) {
            return new Result(false, "Sorry you don't have enough ingredients ):");
        }

        player.changeMoney(data.goldCost);
        inv.pickItem("Wood", data.woodCost);
        inv.pickItem("Stone", data.stoneCost);

        if (name.equalsIgnoreCase("Well")) {
            Well newWell = new Well("Well", new Location(x,y), data.width, data.height);
            game.addBuilding(newWell);
        } else if (name.equalsIgnoreCase("Shipping Bin")) {
            ShippingBin bin = new ShippingBin("Shipping Bin", new Location(x,y), data.width, data.height);
            game.addBuilding(bin);
        } else {
            LivingPlace type = LivingPlace.fromString(name);
            AnimalBuilding building = new AnimalBuilding(name, new Location(x, y), data.width, data.height, type);
            game.addBuilding(building);
        } // Todo: add to player

        return new Result(true, name + " successfully created!");
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder();
        for (ShopItem item : shopItems.values()) {
            sb.append(item.getName() + " : " + item.getPrice() + "\n");
        }
        for (BuildingData data : buildings.values()) {
            sb.append(data.name + " : " + data.description + "\n");
        }
        return sb.toString();
    }

    @Override
    public void endDay() {

    }
}
