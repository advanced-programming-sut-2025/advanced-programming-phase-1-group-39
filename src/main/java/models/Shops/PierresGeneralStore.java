package models.Shops;

import com.google.gson.Gson;
import models.Enums.Season;
import models.Location;
import models.NPC.NPC;
import models.Result;

import java.io.FileReader;
import java.util.*;

public class PierresGeneralStore extends Shop {
    HashMap<String, ShopItem> yearRound = new HashMap<>();
    HashMap<String, ShopItem> backpacks = new HashMap<>();
    HashMap<Season, List<SeasonalItem>> seasonal = new HashMap<>();
    HashMap<String, String> descriptions = new HashMap<>();
    private Season currentSeason = Season.SPRING;

    public PierresGeneralStore(String name, int openHour, int closeHour, Location location, int width, int height, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson("src/main/resources/data/PierresGeneralStore.json");
    }

    public void setCurrentSeason(Season season) {
        this.currentSeason = season;
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            PierresStoreData data = gson.fromJson(reader, PierresStoreData.class);

            for (ShopItemData item : data.yearRound) {
                yearRound.put(item.name, new ShopItem(item.name, item.price, item.dailyLimit));
                descriptions.put(item.name, item.description);
            }

            for (ShopItemData item : data.backpacks) {
                backpacks.put(item.name, new ShopItem(item.name, item.price, item.dailyLimit));
                descriptions.put(item.name, item.description);
            }

            if (data.seasonal != null) {
                for (String seasonKey : data.seasonal.keySet()) {
                    Season season = Season.getSeason(seasonKey);
                    List<SeasonalItemData> items = data.seasonal.get(seasonKey);
                    List<SeasonalItem> shopItems = new ArrayList<>();
                    for (SeasonalItemData item : items) {
                        shopItems.add(new SeasonalItem(item.name, item.price, item.offSeasonPrice, item.dailyLimit, season));
                        descriptions.put(item.name, item.description);
                    }
                    seasonal.put(season, shopItems);
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("=== Pierre's General Store ===\n\n-- Year-Round Items --\n");
        for (ShopItem item : yearRound.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append(descriptions.get(item.getName())).append("g\n");
        }

        sb.append("\n-- Backpacks --\n");
        for (ShopItem item : backpacks.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append(descriptions.get(item.getName())).append("g (Limit: ")
                    .append(item.getDailyLimit()).append(")\n");
        }

        sb.append("\n-- Seasonal Items (" + currentSeason.name() + ") --\n");
        for (Season season : seasonal.keySet()) {
            for (SeasonalItem item : seasonal.get(season)) {
                int price = (season == currentSeason) ? item.getPrice() : item.getOffSeasonPrice();
                sb.append(item.getName()).append(" - ").append(price).append("g (Limit: ")
                        .append(item.getDailyLimit()).append(") [").append(season).append(descriptions.get(item.getName())).append("]\n");
            }
        }

        return sb.toString();
    }

    public Result purchase(String product, int quantity) {
        ShopItem item = null;

        if (yearRound.containsKey(product)) item = yearRound.get(product);
        else if (backpacks.containsKey(product)) item = backpacks.get(product);
        else {
            for (List<SeasonalItem> items : seasonal.values()) {
                for (SeasonalItem i : items) {
                    if (i.getName().equals(product)) {
                        item = i;
                        break;
                    }
                }
                if (item != null) break;
            }
        }

        if (item == null)
            return new Result(false, "Item \"" + product + "\" not found in Pierre's store.");

        if (quantity > item.getAvailableQuantity()) {
            return new Result(false, "Limit exceeded for \"" + product + "\". Limit: " + item.getAvailableQuantity());
        }

        int finalPrice;
        if (item instanceof SeasonalItem seasonalItem) {
            finalPrice = (seasonalItem.getSeason() == currentSeason) ? seasonalItem.getPrice() : seasonalItem.getOffSeasonPrice();
        } else {
            finalPrice = item.getPrice();
        }

        item.purchase(quantity);
        return new Result(true, "Purchased " + quantity + " x " + product + " for " + (finalPrice * quantity) + "g.");
    }

    @Override
    public void endDay() {
        for (ShopItem item : backpacks.values()) item.resetDailyLimit();
        for (ShopItem item : yearRound.values()) item.resetDailyLimit();
        for (List<SeasonalItem> items : seasonal.values())
            for (ShopItem item : items)
                item.resetDailyLimit();
    }

    @Override
    public void handleCommand(String command) {
        // TODO: implement
    }

    // ---------------- Internal Data Structures ---------------- //

    public static class PierresStoreData {
        public List<ShopItemData> yearRound;
        public List<ShopItemData> backpacks;
        public Map<String, List<SeasonalItemData>> seasonal;
    }

    public static class ShopItemData {
        public String name;
        public String description;
        public int price;
        public int dailyLimit;
    }

    public static class SeasonalItemData {
        public String name;
        public String description;
        public int price;
        public int offSeasonPrice;
        public int dailyLimit;
    }

    public static class SeasonalItem extends ShopItem {
        private final int offSeasonPrice;
        private final Season season;

        public SeasonalItem(String name, int price, int offSeasonPrice, int dailyLimit, Season season) {
            super(name, price, dailyLimit);
            this.offSeasonPrice = offSeasonPrice;
            this.season = season;
        }

        public int getOffSeasonPrice() {
            return offSeasonPrice;
        }

        public Season getSeason() {
            return season;
        }
    }
}
