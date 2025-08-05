package com.StardewValley.models.Shops;

import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.crafting.CraftingRecipe;
import com.StardewValley.models.inventory.InventoryType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PierresGeneralStore extends Shop {
    HashMap<String, ShopItem> yearRound = new HashMap<>();
    HashMap<String, ShopItem> backpacks = new HashMap<>();
    HashMap<Season, List<SeasonalItem>> seasonal = new HashMap<>();
    HashMap<String, String> descriptions = new HashMap<>();
    private Season currentSeason = Season.SPRING;

    public PierresGeneralStore(String name, Location location, int width, int height, String jsonPath,
                               int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);

        addToItemManager();
    }

    public void setCurrentSeason(Season season) {
        this.currentSeason = season;
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            PierresStoreData data = gson.fromJson(reader, PierresStoreData.class);

            for (YRShopItemData item : data.yearRound) {
                ShopItem itemToAdd = new ShopItem(item.name, item.price, item.dailyLimit);
                itemToAdd.setTexture(item.path);
                yearRound.put(item.name, itemToAdd);
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

    public void addToItemManager() {
        for (ShopItem item : yearRound.values()) {
            if (ItemManager.getItemByName(item.getName()) == null
                    && !item.getName().equalsIgnoreCase("Dehydrator")
                    && !item.getName().equalsIgnoreCase("Grass Starter")) {
                ItemManager.addShopItems(item);
            }
        }
    }


    @Override
    public Result purchase(String product, int quantity) {
        ShopItem item = null;

        if (yearRound.containsKey(product)) {
            item = yearRound.get(product);
        } else if (backpacks.containsKey(product)) {
            item = backpacks.get(product);
        } else {
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

        if (item == null) {
            return new Result(false, "Item \"" + product + "\" not found in Pierre's store.");
        }

        if (quantity > item.getAvailableQuantity()) {
            return new Result(false, "Limit exceeded for \"" + product + "\". Limit: " + item.getAvailableQuantity());
        }

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        boolean isBackpack = backpacks.containsValue(item);
        int finalPrice;

        if (item instanceof SeasonalItem seasonalItem) {
            finalPrice = (seasonalItem.getSeason() == currentSeason)
                    ? seasonalItem.getPrice()
                    : seasonalItem.getOffSeasonPrice();
        } else {
            finalPrice = item.getPrice();
        }

        int totalPrice = finalPrice * quantity;

        if (isBackpack) {
            if (quantity > 1) {
                return new Result(false, "You can only buy one backpack at a time.");
            }
            if (!player.hasEnoughMoney(totalPrice)) {
                return new Result(false, "You don't have enough money for backpack upgrade.");
            }

            player.getInventory().setInventoryType(InventoryType.getType(item.getName()));
        } else {
            if (!player.getInventory().hasSpace(new ItemStack(item, quantity))) {
                return new Result(false, "Your Inventory does not have enough space.");
            }
            if (!player.hasEnoughMoney(totalPrice)) {
                return new Result(false, "You don't have enough money.");
            }

            if (product.equalsIgnoreCase("Dehydrator")) {
                player.learnCraftingRecipe(CraftingRecipe.DEHYDRATOR);
            } else if (product.equalsIgnoreCase("Grass Starter")) {
                player.learnCraftingRecipe(CraftingRecipe.GRASS_STARTER);
            } else if (ItemManager.getItemByName(product) != null) {
                player.getInventory().addItem(ItemManager.getItemByName(product), quantity);
            } else {
                player.getInventory().addItem(new OddItems(product, new TextureRegion(new Texture(Gdx.files.internal("map/tiles/soil.png")))), quantity);
            }
        }

        player.changeMoney(-totalPrice);
        item.purchase(quantity);

        return new Result(true, "Purchased " + quantity + " x " + product + " for " + totalPrice + "g.");
    }


    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder("=== Pierre's General Store (Availables only) ===\n\n-- Year-Round Items --\n");
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

    @Override
    public String showAvailableProducts() {
        StringBuilder sb = new StringBuilder("=== Pierre's General Store ===\n\n-- Year-Round Items --\n");
        for (ShopItem item : yearRound.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append(descriptions.get(item.getName())).append("g\n");
        }

        sb.append("\n-- Backpacks --\n");
        for (ShopItem item : backpacks.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append(descriptions.get(item.getName())).append("g (Limit: ")
                    .append(item.getDailyLimit()).append(")\n");
        }

        sb.append("\n-- Seasonal Items (" + currentSeason.name() + ") --\n");
        for (Season season : seasonal.keySet()) {
            for (SeasonalItem item : seasonal.get(season)) {
                if (item.getAvailableQuantity() <= 0) continue;
                int price = (season == currentSeason) ? item.getPrice() : item.getOffSeasonPrice();
                sb.append(item.getName()).append(" - ").append(price).append("g (Limit: ")
                        .append(item.getDailyLimit()).append(") [").append(season).append(descriptions.get(item.getName())).append("]\n");
            }
        }

        return sb.toString();
    }

    @Override
    public void endDay() {
        for (ShopItem item : backpacks.values()) item.resetDailyLimit();
        for (ShopItem item : yearRound.values()) item.resetDailyLimit();
        for (List<SeasonalItem> items : seasonal.values())
            for (ShopItem item : items)
                item.resetDailyLimit();
    }

    // ---------------- Internal Data Structures ---------------- //

    public static class PierresStoreData {
        public List<YRShopItemData> yearRound;
        public List<ShopItemData> backpacks;
        public Map<String, List<SeasonalItemData>> seasonal;
    }

    public static class YRShopItemData {
        public String name;
        public String description;
        public int price;
        public int dailyLimit;
        public String path;
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

    @Override
    public void showShopMenu(Stage stage, Skin skin) {
        Window window = new Window("Pierre's General Store", skin);
        window.setSize(1280, 720);
        window.setPosition(stage.getWidth()/2, stage.getHeight()/2, Align.center);
        window.setMovable(true);
        window.setModal(true);

        Table itemTable = new Table();
        itemTable.top().left();

        ScrollPane scrollPane = new ScrollPane(itemTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);

        CheckBox onlyAvailable = new CheckBox("Available Only", skin);

        class Refresher {
            void refreshItems() {
                itemTable.clear();

                for (ShopItem item : yearRound.values()) {
                    if (onlyAvailable.isChecked() && item.getAvailableQuantity() <= 0) continue;

                    Table row = new Table();

                    Image img = new Image(item.getTexture());
                    row.add(img).size(48).padRight(10);

                    Label info = new Label(item.getName() + " - " + item.getPrice() + "g", skin);
                    row.add(info).padRight(15).width(200).left();

                    final int[] count = {1};
                    TextButton minus = new TextButton("-", skin);
                    TextButton plus = new TextButton("+", skin);
                    Label countLabel = new Label("1", skin);

                    minus.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (count[0] > 1) count[0]--;
                            countLabel.setText(String.valueOf(count[0]));
                        }
                    });

                    plus.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (count[0] < item.getAvailableQuantity()) count[0]++;
                            countLabel.setText(String.valueOf(count[0]));
                        }
                    });

                    row.add(minus).padLeft(10);
                    row.add(countLabel).width(30).center();
                    row.add(plus);

                    TextButton buy = new TextButton("Buy", skin);
                    buy.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Result result = purchase(item.getName(), count[0]);
                            Dialog d = new Dialog("Result", skin);
                            d.text(result.message()).button("OK");
                            d.show(stage);

                            if (result.success()) {
                                refreshItems();
                            }
                        }
                    });

                    row.add(buy).padLeft(10);
                    itemTable.add(row).padTop(10).padBottom(10).left().row();
                }
            }
        }

        Refresher refresh = new Refresher();

        onlyAvailable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refresh.refreshItems();
            }
        });

        refresh.refreshItems();

        Table content = new Table();
        content.setFillParent(true);
        content.padTop(20);
        content.add(onlyAvailable).right().pad(20).row();
        content.add(scrollPane).expand().fill().row();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.setVisible(false);
                Gdx.input.setInputProcessor(GameScreen.getScreen().getGameMenuInputAdapter());
            }
        });
        content.add(backButton).padTop(10).center();

        window.add(content).expand().fill().pad(10);
        stage.addActor(window);
    }
}
