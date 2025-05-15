package models.Shops;

import com.google.gson.Gson;
import models.*;
import models.NPC.NPC;
import models.tools.Tool;
import models.tools.ToolType;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class BlackSmithShop extends Shop{

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
                shopItems.put(item.name, new ShopItem(item.name, item.price, item.limit));
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
                    if (!player.getInventory().hasEnoughStack("Steel Bar", 5)
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
        return new Result (true, "Successfully upgraded to" + type + ".");
    }

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

}
