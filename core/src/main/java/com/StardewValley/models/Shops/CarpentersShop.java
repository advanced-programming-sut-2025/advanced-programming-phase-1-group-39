package com.StardewValley.models.Shops;


import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.animals.LivingPlace;
import com.StardewValley.models.buildings.AnimalBuilding;
import com.StardewValley.models.buildings.ShippingBin;
import com.StardewValley.models.buildings.Well;
import com.StardewValley.models.inventory.Inventory;
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

public class CarpentersShop extends Shop {
    private HashMap<String, ShopItem> shopItems = new HashMap<>();
    private HashMap<String, BuildingData> buildings = new HashMap<>();

    public CarpentersShop(String name, Location location, int width, int height, String jsonPath,
                          int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            CarpenterShopData data = gson.fromJson(reader, CarpenterShopData.class);

            for (ShopItemData item : data.items) {
                shopItems.put(item.name, new ShopItem(item.name, item.price, item.limit));
            }

            for (BuildingData item : data.buildings) {
                buildings.put(item.name, new BuildingData(
                        item.name, item.description, item.goldCost,
                        item.woodCost, item.stoneCost, item.width, item.height, item.dailyLimit
                ));
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class CarpenterShopData {
        List<ShopItemData> items;
        List<BuildingData> buildings;
    }
    public class ShopItemData {
        public String name;
        public int price;
        public int limit;
    }
    public class BuildingData {
        public String name;
        public String description;
        public int goldCost;
        public int woodCost;
        public int stoneCost;
        public int width;
        public int height;
        public int dailyLimit;

        public BuildingData(String name, String description, int goldCost, int woodCost, int stoneCost,
                            int width, int height, int dailyLimit) {
            this.name = name;
            this.description = description;
            this.goldCost = goldCost;
            this.woodCost = woodCost;
            this.stoneCost = stoneCost;
            this.width = width;
            this.height = height;
            this.dailyLimit = dailyLimit;
        }
    }

    public Result purchase(String product, int count) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        ShopItem item = shopItems.get(product);
        if (item == null) {
            return new Result(false, "We don't have any " + product + " in our shop ):");
        }
        if (!player.hasEnoughMoney(item.getPrice() * count)) {
            return new Result(false, "Sorry you don't have enough money ):");
        }

        player.changeMoney(-(item.getPrice() * count));
        Inventory inv = player.getInventory();
        inv.addItem(ItemManager.getItemByName(product), count);
        return new Result(true, "You bought " + product + " x" + count + " successfully. Thank You (:");
    }
    public Result buildFarmBuilding(String name, int x, int y) {
        Game game = App.getApp().getCurrentGame();
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        Inventory inv = player.getInventory();


        BuildingData data = buildings.get(name);
        if (data == null) {
            return new Result(false, name + " isn't in our services!");
        }
        if (!player.hasEnoughMoney(data.goldCost)) {
            return new Result(false, "Sorry you doesn't have enough money ):");
        }
        if (!inv.hasEnoughStack("Wood", data.woodCost) || !inv.hasEnoughStack("Stone", data.stoneCost)) {
            return new Result(false, "Sorry you don't have enough ingredients ):");
        }


        if (name.equalsIgnoreCase("Well")) {
            if (!game.getMap().canAddBuilding(new Location(x, y), 3, 2)) {
                return new Result(false, "You can't build " + name + " in this tile");
            }
            Well newWell = new Well("Well", new Location(x,y), data.width, data.height);
            game.addBuilding(newWell);
            player.addToBuildings(newWell);
            newWell.updateMap(game.getMap());
        } else if (name.equalsIgnoreCase("Shipping Bin")) {
            if (!game.getMap().canAddBuilding(new Location(x, y), 1, 1)) {
                return new Result(false, "You can't build " + name + " in this tile");
            }
            ShippingBin bin = new ShippingBin("Shipping Bin", new Location(x,y), 2, 1);
            game.addBuilding(bin);
            player.addToBuildings(bin);
            bin.updateMap(game.getMap());
        } else {
            LivingPlace type = LivingPlace.fromString(name);
            if (!game.getMap().canAddBuilding(new Location(x, y), data.width, data.height)) {
                return new Result(false, "You can't build " + name + " in this tile");
            }
            AnimalBuilding building = new AnimalBuilding(name, new Location(x, y), data.width, data.height, type);
            game.addBuilding(building);
            player.addToBuildings(building);
            building.updateMap(game.getMap());
        }

        player.changeMoney(data.goldCost);
        inv.pickItem("Wood", data.woodCost);
        inv.pickItem("Stone", data.stoneCost);

        return new Result(true, name + " successfully created!");
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder();
        for (ShopItem item : shopItems.values()) {
            sb.append(item.getName() + " : " + item.getPrice() + "\n");
        }
        for (BuildingData data : buildings.values()) {
            sb.append(data.name + " : " + data.description + "\n");
        }
        return sb.toString();
    }

    @Override
    public void endDay() {
        for (ShopItem item : shopItems.values()) {
            item.resetDailyLimit();
        }
    }

    @Override
    public void showShopMenu(Stage stage, Skin skin) {
        Window window = new Window("Carpenter's Shop", skin);
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

                for (ShopItem item : shopItems.values()) {
                    if (onlyAvailable.isChecked() && item.getAvailableQuantity() <= 0) continue;

                    Table row = new Table();

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
                for (BuildingData data : buildings.values()) {
                    if (onlyAvailable.isChecked() && data.dailyLimit > 0) continue;

                    Table row = new Table();

                    Label info = new Label(data.name + " - " + data.goldCost + "g"
                            + data.stoneCost + "stone" + data.woodCost + "wood", skin);
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
                            if (count[0] < data.dailyLimit) count[0]++;
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
                            TextField xField = new TextField("", skin);
                            TextField yField = new TextField("", skin);
                            Dialog inputDialog = new Dialog("Enter Build Location", skin) {
                                @Override
                                protected void result(Object obj) {
                                    if ((Boolean) obj) {
                                        try {
                                            int buildX = Integer.parseInt(xField.getText());
                                            int buildY = Integer.parseInt(yField.getText());

                                            Result result = buildFarmBuilding(data.name, buildX, buildY);
                                            Dialog resultDialog = new Dialog("Result", skin);
                                            resultDialog.text(result.message()).button("OK");
                                            resultDialog.show(stage);

                                            if (result.success()) {
                                                refreshItems();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Dialog errorDialog = new Dialog("Error", skin);
                                            errorDialog.text("Invalid coordinates: " + e.getMessage()).button("OK");
                                            errorDialog.show(stage);
                                        }
                                    }
                                }
                            };


                            Table inputTable = new Table();
                            inputTable.add(new Label("X:", skin)).padRight(10);
                            inputTable.add(xField).width(60).row();
                            inputTable.add(new Label("Y:", skin)).padRight(10);
                            inputTable.add(yField).width(60).row();

                            inputDialog.getContentTable().add(inputTable).pad(10);
                            inputDialog.button("Build", true);
                            inputDialog.button("Cancel", false);

                            inputDialog.show(stage);
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
        content.add(onlyAvailable).right().pad(5).row();
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
