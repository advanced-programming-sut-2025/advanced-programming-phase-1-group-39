package models.Shops;

import com.google.gson.Gson;
import models.Constants;
import models.Location;
import models.NPC.NPC;
import models.Result;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class StardropSaloon extends Shop {
    HashMap<String, ShopItem> foods = new HashMap<>();
    HashMap<String, ShopItem> recipes = new HashMap<>();

    public StardropSaloon(String name, int openHour, int closeHour, NPC owner) {
        super(name, new Location(0,0), 0,0, openHour, closeHour, owner);
        loadFromJson("src/main/resources/data/StardropSaloon.json");
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            StardropSaloonData data = gson.fromJson(reader, StardropSaloonData.class);

            for (ShopItemData item : data.foods) {
                foods.put(item.name, new ShopItem(item.name, item.price, item.limit));
            }

            for (ShopItemData item : data.recipes) {
                recipes.put(item.name, new ShopItem(item.name, item.price, item.limit));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class StardropSaloonData {
        public List<ShopItemData> foods;
        public List<ShopItemData> recipes;
    }
    public class ShopItemData {
        public String name;
        public int price;
        public int limit;
    }



    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("=== Stardrop Saloon ===\n\n-- Foods --\n");
        for (ShopItem item : foods.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }
        sb.append("\n-- Recipes --\n");
        for (ShopItem item : recipes.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g (Limit: ").
                    append(item.getDailyLimit()).append(" per day)\n");
        }
        return sb.toString();
    }


    public Result purchase(String product, int count) {
        ShopItem item = null;

        if (foods.containsKey(product)) {
            item = foods.get(product);
        } else if (recipes.containsKey(product)) {
            item = recipes.get(product);
        }

        if (item == null) {
            return new Result(false, "Item \"" + product + "\" not found in this shop.");
        }

        if (count > item.getDailyLimit()) {
            return new Result(false, "Purchase limit exceeded for \""
                    + product + "\". Limit: " + item.getDailyLimit());
        }

        item.purchase(count);
        return new Result(false,"Purchased " + count + " x " + product + " for " + (item.getPrice() * count) + "g.");
    }


    @Override
    public void endDay() {
    }

    @Override
    public void handleCommand(String command) {
    }

}

