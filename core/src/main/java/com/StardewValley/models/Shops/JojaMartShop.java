package com.StardewValley.models.Shops;


import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.NPC.NPC;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JojaMartShop extends Shop {

    private HashMap<String, ShopItem> permanentStock = new HashMap<>();
    private Map<Season, HashMap<String, ShopItem>> seasonalStockBySeason = new HashMap<>();
    private HashMap<String, ShopItem> seasonalStock = new HashMap<>();

    private Season currentSeason = Season.SPRING; // initialize

    public JojaMartShop(String name, Location location, int width, int height, String jsonPath,
                        int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);

        addToItemManager();
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);

            Type type = new TypeToken<JojaMartData>() {}.getType();
            JojaMartData data = gson.fromJson(reader, type);

            for (PermShopItemData item : data.permanentStock) {
                ShopItem itemToAdd = new ShopItem(item.name, item.price, item.limit);
                itemToAdd.setTexture(item.path);
                permanentStock.put(item.name, itemToAdd);
            }

            for (Map.Entry<String, List<ShopItemData>> entry : data.seasonalStock.entrySet()) {
                Season season = Season.valueOf(entry.getKey());
                HashMap<String, ShopItem> seasonMap = new HashMap<>();
                for (ShopItemData item : entry.getValue()) {
                    seasonMap.put(item.name, new ShopItem(item.name, item.price, item.limit));
                }
                seasonalStockBySeason.put(season, seasonMap);
            }

            setupSeasonalStock(currentSeason);

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class JojaMartData {
        List<PermShopItemData> permanentStock;
        Map<String, List<ShopItemData>> seasonalStock;
    }

    public static class PermShopItemData {
        String name;
        int price;
        int limit;
        String path;
    }
    public static class ShopItemData {
        String name;
        int price;
        int limit;
    }

    public void setCurrentSeason(Season season) {
        this.currentSeason = season;
    }

    private void setupSeasonalStock(Season season) {
        seasonalStock = seasonalStockBySeason.getOrDefault(season, new HashMap<>());
    }

    public void changeSeason(Season newSeason) {
        this.currentSeason = newSeason;
        setupSeasonalStock(newSeason);
    }

    public void addToItemManager() {
        for (ShopItem item : permanentStock.values()) {
            if (ItemManager.getItemByName(item.getName()) == null) {
                ItemManager.addShopItems(item);
            }
        }
    }

    @Override
    public Result purchase(String itemName, int quantity) {
        ShopItem item = permanentStock.getOrDefault(itemName, seasonalStock.get(itemName));
        if (item == null)
            return new Result(false, "Item not found.");

        if (quantity > item.getAvailableQuantity())
            return new Result(false, "You can't buy that many today.");

        item.purchase(quantity);
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
        StringBuilder sb = new StringBuilder("Availables:\nPermanent Stock:\n");
        for (ShopItem item : permanentStock.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }

        sb.append("\n").append(currentSeason).append(" Stock:\n");
        for (ShopItem item : seasonalStock.values()) {
            if (item.getAvailableQuantity() <= 0) continue;
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }

        return sb.toString();
    }

    @Override
    public void showShopMenu(Stage stage, Skin skin) {
        Window window = new Window("BlackSmith Shop", skin);
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

                for (ShopItem item : permanentStock.values()) {
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
