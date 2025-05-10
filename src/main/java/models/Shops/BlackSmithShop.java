package models.Shops;

import models.Constants;
import models.NPC.NPC;
import models.Result;
import models.tools.Tool;

import java.util.HashMap;

public class BlackSmithShop extends Shop{

    HashMap<String, ShopItem> shopItems = new HashMap<>();
    HashMap<String, Boolean> toolUpgrade = new HashMap<>();

    public BlackSmithShop(String name, int openHour, int closeHour, NPC owner) {
        super(name, openHour, closeHour, owner);

        shopItems.put("Copper Ore", new ShopItem("Copper Ore", 75, Constants.INFINITY));
        shopItems.put("Iron Ore", new ShopItem("Iron Ore", 150, Constants.INFINITY));
        shopItems.put("Coal", new ShopItem("Coal", 150, Constants.INFINITY));
        shopItems.put("Gold Ore", new ShopItem("Gold Ore", 400, Constants.INFINITY));

        toolUpgrade.put("Copper Tool", true);
        toolUpgrade.put("Steel Tool", true);
        toolUpgrade.put("Gold Tool", true);
        toolUpgrade.put("Iridium Tool", true);
        toolUpgrade.put("Copper Trash Can", true);
        toolUpgrade.put("Steel Trash Can", true);
        toolUpgrade.put("Gold Trash Can", true);
        toolUpgrade.put("Iridium Trash Can", true);
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
