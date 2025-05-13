package models.Shops;

import models.App;
import models.Location;
import models.NPC.NPC;

import com.google.gson.Gson;
import models.Player;
import models.Result;
import models.crafting.CraftingRecipe;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class FishingShop extends Shop {
    HashMap<String, ShopItem> items = new HashMap<>();

    public FishingShop(String name, int openHour, int closeHour, NPC owner) {
        super(name, new Location(0, 0), 0, 0, openHour, closeHour, owner);
        loadFromJson("src/main/resources/data/FishingShop.json");
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            FishingShopData data = gson.fromJson(reader, FishingShopData.class);

            for (ShopItemData item : data.stock) {
                items.put(item.name, new FishingShopItem(item.name, item.description, item.price,
                        item.dailyLimit, item.fishingSkillRequired));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class FishingShopData {
        public List<ShopItemData> stock;
    }

    public class ShopItemData {
        public String name;
        public String description;
        public int price;
        public int dailyLimit;
        public int fishingSkillRequired; // use 0 if N/A
    }

    public class FishingShopItem extends ShopItem {
        private final String description;
        private final int fishingSkillRequired;

        public FishingShopItem(String name, String description, int price, int dailyLimit, int fishingSkillRequired) {
            super(name, price, dailyLimit);
            this.description = description;
            this.fishingSkillRequired = fishingSkillRequired;
        }

        public String getDescription() {
            return description;
        }

        public int getFishingSkillRequired() {
            return fishingSkillRequired;
        }
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("=== Fishing Shop ===\n\n-- Items --\n");
        for (ShopItem item : items.values()) {
            FishingShopItem fishItem = (FishingShopItem) item;
            sb.append(fishItem.getName()).append(" - ").append(fishItem.getPrice()).append("g");
            sb.append(" (Limit: ").append(fishItem.getDailyLimit()).append(" per day)");
            if (fishItem.getFishingSkillRequired() > 0) {
                sb.append(" [Fishing Skill: ").append(fishItem.getFishingSkillRequired()).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Result purchase(String product, int quantity) {
        if (!items.containsKey(product)) {
            return new Result(false, "Item \"" + product + "\" not found in Fishing Shop.");
        }

        FishingShopItem item = (FishingShopItem) items.get(product);

        if (quantity > item.getAvailableQuantity()) {
            return new Result(false, "Limit exceeded for \"" + product + "\". Limit: " + item.getAvailableQuantity());
        }

        item.purchase(quantity);
        if (product.equalsIgnoreCase("Fish Smoker (Recipe)")) {
            CraftingRecipe recipe = CraftingRecipe.FISH_SMOKER;
            App.getApp().getCurrentGame().getPlayerInTurn().learnCraftingRecipe(recipe);
        } else {
            Player player = App.getApp().getCurrentGame().getPlayerInTurn();
            // Todo: check skill and return pole
        }
        return new Result(true, "Purchased " + quantity + " x " + product + " for " + (item.getPrice() * quantity) + "g.");
    }

    @Override
    public void endDay() {
        for (ShopItem item : items.values()) {
            item.resetDailyLimit();
        }
    }

    @Override
    public void handleCommand(String command) {
        // Optional commands
    }
}

