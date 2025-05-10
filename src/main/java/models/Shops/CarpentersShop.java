package models.Shops;

import com.google.gson.Gson;
import models.ItemManager;
import models.Location;
import models.NPC.NPC;
import models.Player;
import models.Result;
import models.inventory.Inventory;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class CarpentersShop extends Shop{
    private HashMap<String, ShopItem> shopItems = new HashMap<>();
    private HashMap<String, BuildingData> buildings = new HashMap<>();

    public CarpentersShop(String name, Location location, int width, int height, int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson("src/main/resources/data/CarpentersShop.json");
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

    public Result purchase(String product, int count, Player player) {
        ShopItem item = shopItems.get(product);
        if (item == null) {
            return new Result(false, "We don't have any " + product + " in our shop ):");
        }
        //if () {} Todo: check enough money

        //Todo: decrease money
        Inventory inv = player.getInventory();
        inv.addItem(ItemManager.getItemByName(product), count);
        return new Result(true, "You bought " + product + " x1 successfully. Thank You (:");
    }
    public Result buildFarmBuilding(String name, int x, int y, Player player) {
        //Todo: check validate build building for player
        BuildingData data = buildings.get(name);
        if (data == null) {
            return new Result(false, name + " isn't in our services!");
        }
        //Todo: check ingredients and complete method

        return null;
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
}
