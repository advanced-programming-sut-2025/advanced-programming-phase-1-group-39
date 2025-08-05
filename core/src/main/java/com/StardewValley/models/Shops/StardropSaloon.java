package com.StardewValley.models.Shops;


import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.cooking.Food;
import com.StardewValley.models.cooking.FoodRecipe;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.google.gson.Gson;

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
                ShopItem itemToAdd = new ShopItem(item.name, item.price, item.limit);
                itemToAdd.setTexture(item.path);
                foods.put(item.name, itemToAdd);
            }

            for (ShopItemData item : data.recipes) {
                ShopItem itemToAdd = new ShopItem(item.name, item.price, item.limit);
                itemToAdd.setTexture(item.path);
                recipes.put(item.name, itemToAdd);
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
        public String path;
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

    @Override
    public void showShopMenu(Stage stage, Skin skin) {
        Window window = new Window("Stardrop Saloon", skin);
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

                for (ShopItem item : foods.values()) {
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
                for (ShopItem item : recipes.values()) {
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

