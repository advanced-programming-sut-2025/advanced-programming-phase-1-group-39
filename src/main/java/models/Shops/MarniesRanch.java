package models.Shops;

import com.google.gson.Gson;
import models.*;
import models.NPC.NPC;
import models.tools.MilkPail;
import models.tools.Shear;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class MarniesRanch extends Shop {
    HashMap<String, ShopItem> supplies = new HashMap<>();
    HashMap<String, LivestockItem> livestock = new HashMap<>();

    public MarniesRanch(String name, Location location, int width, int height, String jsonPath,
                        int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            RanchData data = gson.fromJson(reader, RanchData.class);

            for (ShopItemData item : data.supplies) {
                supplies.put(item.name, new ShopItem(item.name, item.price, item.dailyLimit));
            }

            for (LivestockData animal : data.livestock) {
                livestock.put(animal.name, new LivestockItem(animal.name, animal.description,
                        animal.price, animal.buildingRequired, animal.dailyLimit));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class RanchData {
        public List<ShopItemData> supplies;
        public List<LivestockData> livestock;
    }

    public class ShopItemData {
        public String name;
        public String description;
        public int price;
        public int dailyLimit; // Allow "unlimited"
    }

    public class LivestockData {
        public String name;
        public String description;
        public int price;
        public String buildingRequired;
        public int dailyLimit;
    }

    public class LivestockItem extends ShopItem {
        private final String buildingRequired;
        private final String description;

        public LivestockItem(String name, String description, int price, String buildingRequired, int dailyLimit) {
            super(name, price, dailyLimit);
            this.buildingRequired = buildingRequired;
            this.description = description;
        }

        public String getBuildingRequired() {
            return buildingRequired;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public Result purchase(String product, int quantity) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        ShopItem item = supplies.get(product);
        if (item == null) {
            item = livestock.get(product);
        }

        if (item == null) {
            return new Result(false, "Item \"" + product + "\" not found at Marnie's Ranch.");
        }

        if (quantity > item.getAvailableQuantity()) {
            return new Result(false, "Limit exceeded for \""
                    + product + "\". Limit: " + item.getAvailableQuantity());
        }

        item.purchase(quantity);
        if (supplies.containsValue(item)) {
            if (product.equalsIgnoreCase("Hay")) {
                player.getInventory().addItem(new OddItems(item.getName()), quantity);
            } else if (product.equalsIgnoreCase("Milk Pail")) {
                //player.getInventory().addItem(new MilkPail(), 1);
            } else {
                //player.getInventory().addItem(new Shear(), 1);
            }
        } else {
            // Todo: add new animal to where?
        }
        return new Result(true, "Purchased " + quantity + " x "
                + product + " for " + (item.getPrice() * quantity) + "g.");
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("=== Marnie's Ranch ===\n\n-- Supplies --\n");
        for (ShopItem item : supplies.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g");
            String limit = item.getDailyLimit() == 1000000 ? "unlimited" : String.valueOf(item.getDailyLimit());
            sb.append(" (Limit: ").append(limit).append(" per day)\n");
        }

        sb.append("\n-- Livestock --\n");
        for (LivestockItem animal : livestock.values()) {
            sb.append(animal.getName()).append(" - ").append(animal.getPrice()).append("g");
            sb.append(" (Building: ").append(animal.getBuildingRequired()).append(", Limit: ")
                    .append(animal.getDailyLimit()).append(" per day)\n");
        }

        return sb.toString();
    }

    @Override
    public String showAvailableProducts() {
        StringBuilder sb = new StringBuilder("=== Marnie's Ranch (Availables only) ===\n\n-- Supplies --\n");
        for (ShopItem item : supplies.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g");
            String limit = item.getDailyLimit() == 1000000  ? "unlimited" : String.valueOf(item.getDailyLimit());
            sb.append(" (Limit: ").append(limit).append(" per day)\n");
        }

        sb.append("\n-- Livestock --\n");
        for (LivestockItem animal : livestock.values()) {
            if (animal.getAvailableQuantity() <= 0) continue;
            sb.append(animal.getName()).append(" - ").append(animal.getPrice()).append("g");
            sb.append(" (Building: ").append(animal.getBuildingRequired()).append(", Limit: ")
                    .append(animal.getDailyLimit()).append(" per day)\n");
        }

        return sb.toString();
    }

    @Override
    public void endDay() {
        for (ShopItem item : supplies.values()) {
            item.resetDailyLimit();
        }
        for (ShopItem item : livestock.values()) {
            item.resetDailyLimit();
        }
    }

}
