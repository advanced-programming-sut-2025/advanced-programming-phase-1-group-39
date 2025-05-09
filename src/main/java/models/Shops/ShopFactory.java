package models.Shops;

import models.Item;
import models.ItemStack;
import models.NPC.NPC;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopFactory {
    public static Shop createBlacksmith() {
        NPC clint = new NPC("Clint"); // Todo: must use available NPC

        HashMap<String, ShopItem> items = new HashMap<>();
        items.put("Copper Ore", new ShopItem("Copper Ore", 75, 9999));
        items.put("Iron Ore", new ShopItem("Iron Ore", 150, 9999));
        items.put("Coal", new ShopItem("Coal", 150, 9999));
        items.put("Gold Ore", new ShopItem("Gold Ore", 400, 9999));
        items.put("Copper Tool", new ShopItem("Copper Tool", 2000, 1));

        return new Shop("Blacksmith", 9, 16, clint, items);
    }
}
