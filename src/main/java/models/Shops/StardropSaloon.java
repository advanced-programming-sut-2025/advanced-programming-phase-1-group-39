package models.Shops;

import com.google.gson.Gson;
import models.*;
import models.NPC.NPC;
import models.cooking.Food;
import models.cooking.FoodRecipe;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class StardropSaloon extends Shop {
    HashMap<String, ShopItem> foods = new HashMap<>();
    HashMap<String, ShopItem> recipes = new HashMap<>();

    public StardropSaloon(String name, Location location, int width, int height, String jsonPath,
                          int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);
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

        if (count > item.getAvailableQuantity()) {
            return new Result(false, "Purchase limit exceeded for \"" + product +
                    "\". Limit: " + item.getAvailableQuantity());
        }

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        int totalPrice = item.getPrice() * count;

        if (!player.hasEnoughMoney(totalPrice)) {
            return new Result(false, "You don't have enough money to buy " + count + " x " + product + ".");
        }

        if (recipes.containsValue(item)) {
            player.learnFoodRecipe(FoodRecipe.valueOf(product.toUpperCase()));
        } else {
            if (!player.getInventory().hasSpace(new ItemStack(item, count))) {
                return new Result(false, "Your Inventory does not have enough space.");
            }
            if (ItemManager.getItemByName(product) != null) {
                player.getInventory().addItem(ItemManager.getItemByName(product), count);
            } else {
                player.getInventory().addItem(new Food(product, 50, 100, null), count);
            }
        }

        player.changeMoney(-totalPrice);
        item.purchase(count);

        return new Result(true, "Purchased " + count + " x " + product + " for " + totalPrice + "g.");
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

    @Override
    public String showAvailableProducts() {
        StringBuilder sb = new StringBuilder("=== Stardrop Saloon (Availables only) ===\n\n-- Foods --\n");
        for (ShopItem item : foods.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }
        sb.append("\n-- Recipes --\n");
        for (ShopItem item : recipes.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g (Limit: ").
                    append(item.getDailyLimit()).append(" per day)\n");
        }
        return sb.toString();
    }

    @Override
    public void endDay() {
        for (ShopItem item : foods.values()) {
            item.resetDailyLimit();
        }
        for (ShopItem item : recipes.values()) {
            item.resetDailyLimit();
        }
    }

}

