package models.Shops;

import com.google.gson.Gson;
import models.Constants;
import models.NPC.NPC;
import models.Result;
import models.tools.Tool;

import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

public class BlackSmithShop extends Shop{

    HashMap<String, ShopItem> shopItems = new HashMap<>();
    HashMap<String, Boolean> toolUpgrade = new HashMap<>();

    public BlackSmithShop(String name, int openHour, int closeHour, NPC owner) {
        super(name, openHour, closeHour, owner);
        loadFromJson("src/main/resources/data/BlackSmithShop.json");
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
                toolUpgrade.put(upgrade, true);
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



    public Result upgradeTool(Tool tool, String type) {
        Boolean available = toolUpgrade.get(type);
        if (available == null) {
            return new Result(false, "No such upgrade available.");
        } else if (!available) {
            return new Result (false, "You already upgraded To " + type + " today.");
        } else {
            switch (type) {
                // TODO: handle upgrade logic (remove money, etc.)
                case "Copper Tool" : {

                }
                case "Steel Tool" : {

                }
            }
        }

        toolUpgrade.put(type, false);
        return new Result (true, "Successfully upgraded to" + type + ".");
    }
    public Result upgradeTrashCan(String type) {
        Boolean available = toolUpgrade.get(type);
        if (available == null) {
            return new Result(false, "No such upgrade available.");
        } else if (!available) {
            return new Result (false, "You already upgraded To " + type + " today.");
        } else {
            switch (type) {
                // TODO: handle upgrade logic (remove money, etc.)
                case "Copper Tool" : {

                }
                case "Steel Tool" : {

                }
            }
        }

        toolUpgrade.put(type, false);
        return new Result (true, "Successfully upgraded to" + type + ".");
    }

    public void endDay() { // Todo: must be called each day
        for (String key : toolUpgrade.keySet()) {
            toolUpgrade.put(key, true);
        }
    }


    @Override
    public void handleCommand(String command) {
        // TODO: parse and route commands like:
        // "buy Copper Ore 5", "upgrade Copper Tool"
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

    public void purchase(String product, int quantity) {

    }

}
