package com.StardewValley.models.Shops;


import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.animals.AnimalType;
import com.StardewValley.models.animals.LivingPlace;
import com.StardewValley.models.buildings.AnimalBuilding;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.models.tools.MilkPail;
import com.StardewValley.models.tools.Shear;
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
import java.util.HashMap;
import java.util.List;

public class MarniesRanch extends Shop {
    HashMap<String, ShopItem> supplies = new HashMap<>();
    HashMap<String, LivestockItem> livestock = new HashMap<>();

    public MarniesRanch(String name, Location location, int width, int height, String jsonPath,
                        int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);

        addToItemManager();
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            RanchData data = gson.fromJson(reader, RanchData.class);

            for (ShopItemData item : data.supplies) {
                ShopItem itemToAdd = new ShopItem(item.name, item.price, item.dailyLimit);
                itemToAdd.setTexture(item.path);
                supplies.put(item.name, itemToAdd);
            }

            for (LivestockData animal : data.livestock) {
                LivestockItem itemToAdd = new LivestockItem(animal.name, animal.description,
                        animal.price, animal.buildingRequired, animal.dailyLimit);
                itemToAdd.setTexture(animal.path);
                livestock.put(animal.name, itemToAdd);
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
        public String path;
    }

    public class LivestockData {
        public String name;
        public String description;
        public int price;
        public String buildingRequired;
        public int dailyLimit;
        public String path;
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

    public void addToItemManager() {
        for (ShopItem item : supplies.values()) {
            if (ItemManager.getItemByName(item.getName()) == null) {
                ItemManager.addShopItems(item);
            }
        }
    }

    @Override
    public Result purchase(String product, int quantity) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        ShopItem item = supplies.get(product);
        if (item == null) {
            return new Result(false, "Item \"" + product + "\" not found in ranch supplies.");
        }

        if (quantity > item.getAvailableQuantity()) {
            return new Result(false, "Limit exceeded for \"" + product + "\". Limit: " + item.getAvailableQuantity());
        }

        int totalPrice = item.getPrice() * quantity;
        if (!player.hasEnoughMoney(totalPrice)) {
            return new Result(false, "You don't have enough money ):");
        }

        item.purchase(quantity);
        player.getInventory().addItem(item, quantity);
        player.changeMoney(-totalPrice);

        switch (product.toLowerCase()) {
            case "Hay" -> player.getInventory().addItem(new OddItems(item.getName(), new TextureRegion(new Texture(Gdx.files.internal("shops/MarniesRanch/Hay.png")))), quantity);
            case "Milk Pail" -> player.getInventory().addItem(new MilkPail(), 1);
            case "Shears" -> player.getInventory().addItem(new Shear(), 1);
            default -> {}
        }

        return new Result(true, "Purchased " + quantity + " x " + product + " for " + totalPrice + "g.");
    }


    public Result buyAnimal(String animalStr, String name) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        ShopItem item = livestock.get(animalStr);
        if (item == null) {
            return new Result(false, "Animal \"" + animalStr + "\" not available.");
        }

        if (item.getAvailableQuantity() < 1) {
            return new Result(false, "Only " + item.getAvailableQuantity() + " " + animalStr + "(s) available.");
        }

        int totalPrice = item.getPrice();
        if (!player.hasEnoughMoney(totalPrice)) {
            return new Result(false, "You can't afford " + 1 + " x " + animalStr);
        }

        AnimalType type = AnimalType.getType(animalStr);
        if (type == null) {
            return new Result(false, "Invalid animal type");
        }
        LivingPlace buildingType = type.livingPlace;

        AnimalBuilding building = player.getAnimalBuilding(buildingType);
        if (building == null) {
            return new Result(false, "There is no building for you to put this animal");
        }

        Animal animal = type.create(name);
        player.addAnimal(animal);
        building.addAnimal(animal);

        item.purchase(1);
        player.changeMoney(-totalPrice);

        return new Result(true, "You purchased " + " 1x " + animalStr + " successfully.");
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

    @Override
    public void showShopMenu(Stage stage, Skin skin) {
        Window window = new Window("Marnie's Ranch", skin);
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

        CheckBox onlyAvailable = new CheckBox("Availables Only", skin);

        class Refresher {
            void refreshItems() {
                itemTable.clear();

                for (ShopItem item : supplies.values()) {
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
                    itemTable.add(row).padBottom(10).left().row();
                }
                for (LivestockItem item : livestock.values()) {
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

                    final TextField nameField = new TextField("", skin);
                    TextButton buy = new TextButton("Buy", skin);
                    buy.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Dialog nameDialog = new Dialog("Name?", skin) {
                                @Override
                                protected void result(Object object) {
                                    if ((Boolean) object) {
                                        String animalName = nameField.getText().trim();
                                        if (animalName.isEmpty()) {
                                            Dialog warn = new Dialog("Error", skin);
                                            warn.text("Enter a name").button("Ok");
                                            warn.show(stage);
                                            return;
                                        }

                                        Result result = buyAnimal(item.getName(), animalName);
                                        Dialog d = new Dialog("Result", skin);
                                        d.text(result.message()).button("OK");
                                        d.show(stage);

                                        if (result.success()) {
                                            refreshItems();
                                        }
                                    }
                                }
                            };

                            nameDialog.text("Choose a name for your animal");
                            nameDialog.getContentTable().add(nameField).width(200).pad(10).row();
                            nameDialog.button("Ok", true);
                            nameDialog.button("Cancel", false);
                            nameDialog.show(stage);
                        }
                    });


                    row.add(buy).padLeft(10);
                    itemTable.add(row).padBottom(10).left().row();
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
        content.add(onlyAvailable).left().padBottom(10).row();
        content.add(scrollPane).expand().fill().row();

        window.add(content).expand().fill().pad(10);
        stage.addActor(window);
    }

}
