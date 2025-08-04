package com.StardewValley.models.Shops;


import com.StardewValley.models.*;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.tools.Tool;
import com.StardewValley.models.tools.ToolType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.google.gson.Gson;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class BlackSmithShop extends Shop {

    HashMap<String, ShopItem> shopItems = new HashMap<>();
    HashMap<ToolType, Boolean> toolUpgrade = new HashMap<>();

    public BlackSmithShop(String name, Location location, int width, int height, String jsonPath,
                          int openHour, int closeHour, NPC owner) {
        super(name, location, width, height, openHour, closeHour, owner);
        loadFromJson(jsonPath);
    }

    public void loadFromJson(String path) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(path);
            BlackSmithShopData data = gson.fromJson(reader, BlackSmithShopData.class);

            for (ShopItemData item : data.items) {
                ShopItem itemToAdd = new ShopItem(item.name, item.price, item.limit);
                itemToAdd.setTexture(item.path);
                shopItems.put(item.name, itemToAdd);
            }

            for (String upgrade : data.toolUpgrades) {
                toolUpgrade.put(ToolType.fromString(upgrade), true);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class BlackSmithShopData {
        List<ShopItemData> items;
        List<String> toolUpgrades;
    }
    public class ShopItemData {
        public String name;
        public int price;
        public int limit;
        public String path;
    }

    public Result purchase(String product, int quantity) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();

        ShopItem item = shopItems.get(product);
        if (item == null) {
            return new Result(false, "We don't have any " + product + " ):");
        }

        if (item.getAvailableQuantity() < quantity) {
            return new Result(false, "We don't have enough stock of " + product);
        }

        if (!player.getInventory().hasSpace(new ItemStack(item, quantity))) {
            return new Result(false, "Your Inventory does not have enough space.");
        }

        int totalPrice = item.getPrice() * quantity;
        if (!player.hasEnoughMoney(totalPrice)) {
            return new Result(false, "Oops, you don't have enough money ):");
        }

        player.changeMoney(-totalPrice);
        player.getInventory().addItem(ItemManager.getItemByName(item.getName()), quantity);
        item.purchase(quantity);

        return new Result(true, "You have successfully bought " + quantity + "x of " + product + " for " + totalPrice + "g.");
    }



    public Result upgradeTool(Tool tool) {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        if (tool == null) {
            return new Result(false, "You don't have " + tool.getName());
        }
        ToolType type = ToolType.getNext(tool.getType());
        int price  = 0;
        Boolean available = toolUpgrade.get(type);
        if (available == null) {
            return new Result(false, "No such upgrade available.");
        } else if (!available) {
            return new Result (false, "You already upgraded To " + type + " today.");
        } else {
            switch (type) {
                case COPPER -> {
                    if (!player.getInventory().hasEnoughStack("Copper Bar", 5)
                            || !player.hasEnoughMoney(2000)) {
                        return new Result(false, "You can't upgrade your tool");
                    }
                    price = 2000;
                }
                case IRON -> {
                    if (!player.getInventory().hasEnoughStack("Iron Bar", 5)
                            || !player.hasEnoughMoney(5000)) {
                        return new Result(false, "You can't upgrade your tool");
                    }
                    price = 5000;
                }
                case GOLD -> {
                    if (!player.getInventory().hasEnoughStack("Gold Bar", 5)
                            || !player.hasEnoughMoney(10000)) {
                        return new Result(false, "You can't upgrade your tool");
                    }
                    price = 10000;
                }
                case IRIDIUM -> {
                    if (!player.getInventory().hasEnoughStack("Iridium Bar", 5)
                            || !player.hasEnoughMoney(25000)) {
                        return new Result(false, "You can't upgrade your tool");
                    }
                    price = 25000;
                }
            }
            tool.upgradeType();
        }

        player.changeMoney(-price);
        toolUpgrade.put(type, false);
        return new Result (true, "Successfully upgraded to " + type + ".");
    }

    @Override
    public void endDay() {
        for (ToolType key : toolUpgrade.keySet()) {
            toolUpgrade.put(key, true);
        }
        for (ShopItem item : shopItems.values()) {
            item.resetDailyLimit();
        }
    }

    @Override
    public String showAllProducts() {
        StringBuilder sb = new StringBuilder();
        for (ShopItem item : shopItems.values()) {
            sb.append(item.getName()).append(" - ").append(item.getPrice()).append("g\n");
        }
        return sb.toString();
    }

    @Override
    public String showAvailableProducts() {
        return "";
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

                for (ShopItem item : shopItems.values()) {
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
        content.add(onlyAvailable).left().pad(5).row();
        content.add(scrollPane).expand().fill().row();

        window.add(content).expand().fill().pad(10);
        stage.addActor(window);
    }
}
