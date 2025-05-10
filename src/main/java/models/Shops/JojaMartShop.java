package models.Shops;

import models.Constants;
import models.Enums.Season;
import models.Location;
import models.NPC.NPC;
import models.Result;

import java.util.HashMap;

public class JojaMartShop extends Shop {

    private HashMap<String, ShopItem> permanentStock = new HashMap<>();
    private HashMap<String, ShopItem> seasonalStock = new HashMap<>();
    private HashMap<String, Integer> dailyPurchases = new HashMap<>();

    private Season currentSeason;

    public JojaMartShop(String name, int openHour, int closeHour, NPC owner, Season currentSeason) {
        super(name, new Location(0,0), 0, 0, openHour, closeHour, owner);
        this.currentSeason = currentSeason;

        setupPermanentStock();
        setupSeasonalStock(currentSeason);
    }

    private void setupPermanentStock() {
        permanentStock.put("Joja Cola", new ShopItem("Joja Cola", 75, Constants.INFINITY));
        permanentStock.put("Ancient Seed", new ShopItem("Ancient Seed", 500, 1));
        permanentStock.put("Grass Starter", new ShopItem("Grass Starter", 125, Constants.INFINITY));
        permanentStock.put("Sugar", new ShopItem("Sugar", 125, Constants.INFINITY));
        permanentStock.put("Wheat Flour", new ShopItem("Wheat Flour", 125, Constants.INFINITY));
        permanentStock.put("Rice", new ShopItem("Rice", 250, Constants.INFINITY));
    }

    private void setupSeasonalStock(Season season) { // Todo: must be called but where?
        seasonalStock.clear();

        switch (season) {
            case SPRING:
                seasonalStock.put("Parsnip Seeds", new ShopItem("Parsnip Seeds", 25, 5));
                seasonalStock.put("Bean Starter", new ShopItem("Bean Starter", 75, 5));
                seasonalStock.put("Cauliflower Seeds", new ShopItem("Cauliflower Seeds", 100, 5));
                seasonalStock.put("Potato Seeds", new ShopItem("Potato Seeds", 62, 5));
                seasonalStock.put("Strawberry Seeds", new ShopItem("Strawberry Seeds", 100, 5));
                seasonalStock.put("Tulip Bulb", new ShopItem("Tulip Bulb", 25, 5));
                seasonalStock.put("Kale Seeds", new ShopItem("Kale Seeds", 87, 5));
                seasonalStock.put("Coffee Beans", new ShopItem("Coffee Beans", 200, 1));
                seasonalStock.put("Carrot Seeds", new ShopItem("Carrot Seeds", 5, 10));
                seasonalStock.put("Rhubarb Seeds", new ShopItem("Rhubarb Seeds", 100, 5));
                seasonalStock.put("Jazz Seeds", new ShopItem("Jazz Seeds", 37, 5));
                break;

            case SUMMER:
                seasonalStock.put("Tomato Seeds", new ShopItem("Tomato Seeds", 62, 5));
                seasonalStock.put("Pepper Seeds", new ShopItem("Pepper Seeds", 50, 5));
                seasonalStock.put("Wheat Seeds", new ShopItem("Wheat Seeds", 12, 10));
                seasonalStock.put("Summer Squash Seeds", new ShopItem("Summer Squash Seeds", 10, 10));
                seasonalStock.put("Radish Seeds", new ShopItem("Radish Seeds", 50, 5));
                seasonalStock.put("Melon Seeds", new ShopItem("Melon Seeds", 100, 5));
                seasonalStock.put("Hops Starter", new ShopItem("Hops Starter", 75, 5));
                seasonalStock.put("Poppy Seeds", new ShopItem("Poppy Seeds", 125, 5));
                seasonalStock.put("Spangle Seeds", new ShopItem("Spangle Seeds", 62, 5));
                seasonalStock.put("Starfruit Seeds", new ShopItem("Starfruit Seeds", 400, 5));
                seasonalStock.put("Coffee Beans", new ShopItem("Coffee Beans", 200, 1));
                seasonalStock.put("Sunflower Seeds", new ShopItem("Sunflower Seeds", 125, 5));
                break;

            case FALL:
                seasonalStock.put("Corn Seeds", new ShopItem("Corn Seeds", 187, 5));
                seasonalStock.put("Eggplant Seeds", new ShopItem("Eggplant Seeds", 25, 5));
                seasonalStock.put("Pumpkin Seeds", new ShopItem("Pumpkin Seeds", 125, 5));
                seasonalStock.put("Broccoli Seeds", new ShopItem("Broccoli Seeds", 15, 5));
                seasonalStock.put("Amaranth Seeds", new ShopItem("Amaranth Seeds", 87, 5));
                seasonalStock.put("Grape Starter", new ShopItem("Grape Starter", 75, 5));
                seasonalStock.put("Beet Seeds", new ShopItem("Beet Seeds", 20, 5));
                seasonalStock.put("Yam Seeds", new ShopItem("Yam Seeds", 75, 5));
                seasonalStock.put("Bok Choy Seeds", new ShopItem("Bok Choy Seeds", 62, 5));
                seasonalStock.put("Cranberry Seeds", new ShopItem("Cranberry Seeds", 300, 5));
                seasonalStock.put("Sunflower Seeds", new ShopItem("Sunflower Seeds", 125, 5));
                seasonalStock.put("Fairy Seeds", new ShopItem("Fairy Seeds", 250, 5));
                seasonalStock.put("Rare Seed", new ShopItem("Rare Seed", 1000, 1));
                seasonalStock.put("Wheat Seeds", new ShopItem("Wheat Seeds", 12, 5));
                break;

            case WINTER:
                seasonalStock.put("Powdermelon Seeds", new ShopItem("Powdermelon Seeds", 20, 10));
                break;
        }
    }

    public void changeSeason(Season newSeason) {
        this.currentSeason = newSeason;
        setupSeasonalStock(newSeason);
    }

    public Result purchaseItem(String itemName, int quantity) {
        ShopItem item = permanentStock.getOrDefault(itemName, seasonalStock.get(itemName));
        if (item == null)
            return new Result(false, "Item not found.");

        int alreadyBought = dailyPurchases.getOrDefault(itemName, 0);
        if (item.getDailyLimit() != Constants.INFINITY && alreadyBought + quantity > item.getDailyLimit())
            return new Result(false, "You can't buy that many today.");

        dailyPurchases.put(itemName, alreadyBought + quantity);
        return new Result(true, "Successfully bought " + quantity + " x " + itemName + " for " +
                (item.getPrice() * quantity) + "g.");
    }

    public void endDay() {
        dailyPurchases.clear();
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

