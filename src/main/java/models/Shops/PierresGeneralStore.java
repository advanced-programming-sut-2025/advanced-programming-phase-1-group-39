package models.Shops;

import models.NPC.NPC;
import models.Result;

import java.util.*;

public class PierresGeneralStore extends Shop {

    private Map<String, ShopItem> permanentStock = new HashMap<>();
    private HashMap<String, ShopItem> backpacks = new HashMap<>();
    private Map<String, List<ShopItem>> seasonalStock = new HashMap<>();
    private Map<String, Integer> purchaseTracker = new HashMap<>(); // track daily limits

    public PierresGeneralStore(String name, int openHour, int closeHour, NPC owner) {
        super(name, openHour, closeHour, owner);
        loadPermanentStock();
        //loadSeasonalStock();
    }

    private void loadPermanentStock() {
        permanentStock.put("Large Pack", new ShopItem("Large Pack", 2000, 1));
        permanentStock.put("Deluxe Pack", new ShopItem("Deluxe Pack", 10000, 1));
    }

    /*private void loadSeasonalStock() {
        seasonalStock.put("Spring", new ArrayList<>());
        seasonalStock.put("Summer", new ArrayList<>());
        seasonalStock.put("Fall", new ArrayList<>());

        seasonalStock.get("Spring").add(new ShopItem("Parsnip Seeds", 20, 30, 5));
        seasonalStock.get("Spring").add(new ShopItem("Bean Starter", 60, 90, 5));
        // Add the rest...

        seasonalStock.get("Summer").add(new ShopItem("Melon Seeds", "Plant these in the summer...", 80, 120, 5));
        // Add rest...

        seasonalStock.get("Fall").add(new ShopItem("Pumpkin Seeds", "Plant these in the fall...", 100, 150, 5));
        // Add rest...
    }*/

    @Override
    public void handleCommand(String command) {
        // TODO: parse commands like "buy Parsnip Seeds 3"
    }

    @Override
    public void endDay() {
        purchaseTracker.clear();
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("Permanent Stock:\n");
        for (ShopItem item : permanentStock.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }

        String season = getCurrentSeason(); // Needs to be implemented
        sb.append("\n").append(season).append(" Stock:\n");
        List<ShopItem> seasonalItems = seasonalStock.get(season);
        for (ShopItem item : seasonalItems) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }
        return sb.toString();
    }

    @Override
    public String showAvailableProducts() {
        return showAllProducts(); // You could filter based on purchaseTracker if needed
    }

    public Result purchase(String product, int quantity) {
        // TODO: implement quantity check, season check, and daily limit logic
        return null;
    }


    private String getCurrentSeason() {
        // TODO: replace with actual game logic
        return "Spring"; // Placeholder
    }
}

