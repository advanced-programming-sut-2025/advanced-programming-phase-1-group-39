package com.StardewValley.models.Shops;


import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.cooking.Food;
import com.StardewValley.models.crafting.CraftingRecipe;
import com.StardewValley.models.tools.FishingPole;
import com.StardewValley.models.tools.FishingPoleType;
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

public class FishingShop extends Shop {
    HashMap<String, ShopItem> items = new HashMap<>();

    public FishingShop(String name, Location location, int width, int height, String jsonPath,
                       int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);

        addToItemManager();
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            FishingShopData data = gson.fromJson(reader, FishingShopData.class);

            for (ShopItemData item : data.stock) {
                FishingShopItem itemToAdd = new FishingShopItem(item.name, item.description, item.price,
                        item.dailyLimit, item.fishingSkillRequired);
                itemToAdd.setTexture(item.path);
                items.put(item.name, itemToAdd);
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
        public String path;
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

    public void addToItemManager() {
        ItemManager.addShopItems(items.get("Trout Soup"));
    }

    @Override
    public Result purchase(String product, int quantity) {
        if (!items.containsKey(product)) {
            return new Result(false, "Item \"" + product + "\" not found in Fishing Shop.");
        }

        FishingShopItem item = (FishingShopItem) items.get(product);

        if (quantity > item.getAvailableQuantity()) {
            return new Result(false, "Limit exceeded for \"" + product + "\". Limit: " + item.getAvailableQuantity());
        }

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        if (product.equalsIgnoreCase("Fish Smoker (Recipe)")) {
            CraftingRecipe recipe = CraftingRecipe.FISH_SMOKER;
            player.learnCraftingRecipe(recipe);
            item.purchase(quantity);
        } else if (product.equalsIgnoreCase("Trout Soup")) {
            player.getInventory().addItem(new Food("Trout Soup", 50, 250, null), 1);
            item.purchase(quantity);
        } else {
            Skill skill = player.getSkills();
            if (skill.getFishingLevel() < item.getFishingSkillRequired()) {
                return new Result(false, "Your fishing skill is not high enough ):");
            }
            String[] str = product.split(" ");
            String typeStr = str[0];
            FishingPole pole = new FishingPole(product, FishingPoleType.getType(product), item.getTexture());
            player.getInventory().addItem(pole, 1);
            item.purchase(quantity);
        }
        return new Result(true, "Purchased " + quantity + " x " + product + " for " + (item.getPrice() * quantity) + "g.");
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

    @Override
    public String showAvailableProducts() {
        StringBuilder sb = new StringBuilder("=== Fishing Shop (Availables only) ===\n\n-- Items --\n");
        for (ShopItem item : items.values()) {
            if (item.getAvailableQuantity() <= 0) {
                continue;
            }
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

    @Override
    public void endDay() {
        for (ShopItem item : items.values()) {
            item.resetDailyLimit();
        }
    }

    @Override
    public void showShopMenu(Stage stage, Skin skin) {
        Window window = new Window("Fishing Shop", skin);
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

                for (ShopItem item : items.values()) {
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

