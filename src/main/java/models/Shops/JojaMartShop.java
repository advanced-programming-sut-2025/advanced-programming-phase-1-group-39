package models.Shops;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.*;
import models.Enums.Season;
import models.NPC.NPC;
import models.buildings.AnimalBuilding;
import models.buildings.Building;
import models.buildings.Well;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JojaMartShop extends Shop {

    private HashMap<String, ShopItem> permanentStock = new HashMap<>();
    private Map<Season, HashMap<String, ShopItem>> seasonalStockBySeason = new HashMap<>();
    private HashMap<String, ShopItem> seasonalStock = new HashMap<>();

    private Season currentSeason;

    public JojaMartShop(String name, int openHour, int closeHour, NPC owner, Season currentSeason) {
        super(name, new Location(0, 0), 0, 0, openHour, closeHour, owner);
        this.currentSeason = currentSeason;
        loadFromJson("src/main/resources/data/JojaMartShop.json");
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);

            Type type = new TypeToken<JojaMartData>() {}.getType();
            JojaMartData data = gson.fromJson(reader, type);

            // Load permanent stock
            for (ShopItemData item : data.permanentStock) {
                permanentStock.put(item.name, new ShopItem(item.name, item.price, item.limit));
            }

            // Load seasonal stock for all seasons
            for (Map.Entry<String, List<ShopItemData>> entry : data.seasonalStock.entrySet()) {
                Season season = Season.valueOf(entry.getKey());
                HashMap<String, ShopItem> seasonMap = new HashMap<>();
                for (ShopItemData item : entry.getValue()) {
                    seasonMap.put(item.name, new ShopItem(item.name, item.price, item.limit));
                }
                seasonalStockBySeason.put(season, seasonMap);
            }

            // Set initial seasonal stock
            setupSeasonalStock(currentSeason);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Data holder classes for JSON parsing
    public static class JojaMartData {
        List<ShopItemData> permanentStock;
        Map<String, List<ShopItemData>> seasonalStock;
    }

    public static class ShopItemData {
        String name;
        int price;
        int limit;
    }


    private void setupSeasonalStock(Season season) {
        seasonalStock = seasonalStockBySeason.getOrDefault(season, new HashMap<>());
    }

    public void changeSeason(Season newSeason) {
        this.currentSeason = newSeason;
        setupSeasonalStock(newSeason);
    }

    public Result purchaseItem(String itemName, int quantity) {
        ShopItem item = permanentStock.getOrDefault(itemName, seasonalStock.get(itemName));
        if (item == null)
            return new Result(false, "Item not found.");

        if (quantity > item.getAvailableQuantity())
            return new Result(false, "You can't buy that many today.");

        Item itemToAdd = ItemManager.getItemByName(itemName);
        App.getApp().getCurrentGame().getPlayerInTurn().getInventory().addItem(itemToAdd, 1);
        return new Result(true, "Successfully bought " + quantity + " x " + itemName + " for " +
                (item.getPrice() * quantity) + "g.");
    }

    public void endDay() {
        for (ShopItem item : permanentStock.values()) {
            item.resetDailyLimit();
        }
        for (ShopItem item : seasonalStock.values()) {
            item.resetDailyLimit();
        }
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("Permanent Stock:\n");
        for (ShopItem item : permanentStock.values())
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");

        sb.append("\n").append(currentSeason).append(" Stock:\n");
        for (ShopItem item : seasonalStock.values())
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");

        return sb.toString();
    }

    @Override
    public String showAvailableProducts() {
        return showAllProducts();
    }

    @Override
    public void handleCommand(String command) {
        // TODO: parse commands like: "buy Parsnip Seeds 3"
    }

}
