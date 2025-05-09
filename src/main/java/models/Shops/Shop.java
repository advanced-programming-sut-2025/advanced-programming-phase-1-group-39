package models.Shops;

import models.ItemStack;
import models.NPC.NPC;
import models.buildings.Building;

import java.util.*;

public class Shop extends Building {
    private String name;
    private int openHour;
    private int closeHour;
    private NPC owner;
    private HashMap<String, ShopItem> shopItems = new HashMap<>();

    public Shop(String name, int openHour, int closeHour, NPC owner, HashMap<String, ShopItem> shopItems) {
        super(name);
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.owner = owner;
        this.shopItems = shopItems;
    }

    public ArrayList<ShopItem> getAllProducts() {
        return new ArrayList<ShopItem>(shopItems.values());
    }
}

