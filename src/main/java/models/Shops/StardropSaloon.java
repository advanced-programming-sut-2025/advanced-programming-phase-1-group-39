package models.Shops;

import models.Constants;
import models.NPC.NPC;

import java.util.HashMap;

public class StardropSaloon extends Shop {
    HashMap<String, ShopItem> foodItems = new HashMap<>();
    HashMap<String, ShopItem> recipeItems = new HashMap<>();

    public StardropSaloon(String name, int openHour, int closeHour, NPC owner) {
        super(name, openHour, closeHour, owner);
        loadFoods();
        loadRecipes();
    }

    private void loadFoods() {
        foodItems.put("Beer", new ShopItem("Beer", 400, Constants.INFINITY));
        foodItems.put("Salad", new ShopItem("Salad", 220, Constants.INFINITY));
        foodItems.put("Bread", new ShopItem("Bread", 120, Constants.INFINITY));
        foodItems.put("Spaghetti", new ShopItem("Spaghetti", 240, Constants.INFINITY));
        foodItems.put("Pizza", new ShopItem("Pizza", 600, Constants.INFINITY));
        foodItems.put("Coffee", new ShopItem("Coffee", 300, Constants.INFINITY));
    }

    private void loadRecipes() {
        recipeItems.put("Hashbrowns Recipe", new ShopItem("Hashbrowns Recipe", 50, 1));
        recipeItems.put("Omelet Recipe", new ShopItem("Omelet Recipe", 100, 1));
        recipeItems.put("Pancakes Recipe", new ShopItem("Pancakes Recipe", 100, 1));
        recipeItems.put("Bread Recipe", new ShopItem("Bread Recipe", 100, 1));
        recipeItems.put("Tortilla Recipe", new ShopItem("Tortilla Recipe", 100, 1));
        recipeItems.put("Pizza Recipe", new ShopItem("Pizza Recipe", 150, 1));
        recipeItems.put("Maki Roll Recipe", new ShopItem("Maki Roll Recipe", 300, 1));
        recipeItems.put("Triple Shot Espresso Recipe", new ShopItem("Triple Shot Espresso Recipe", 5000, 1));
        recipeItems.put("Cookie Recipe", new ShopItem("Cookie Recipe", 300, 1));
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("=== Stardrop Saloon ===\n\n-- Foods --\n");
        for (ShopItem item : foodItems.values()) {
            sb.append(item.name()).append(" - ").append(item.price()).append("g\n");
        }
        sb.append("\n-- Recipes --\n");
        for (ShopItem item : recipeItems.values()) {
            sb.append(item.name()).append(" - ").append(item.price()).append("g (Limit: ").append(item.limit()).append(" per day)\n");
        }
        return sb.toString();
    }

    @Override
    public void purchase(String product, int quantity) {
        // باید بررسی بشه که در foodItems یا recipeItems وجود داره یا نه
    }

    @Override
    public void endDay() {
        // پاک کردن محدودیت روزانه برای recipe ها
    }

    @Override
    public void handleCommand(String command) {
        // پیاده‌سازی فرمان‌هایی مثل buy Salad 2
    }

    @Override
    public void addProduct(String product) {
        // اختیاری
    }
}

