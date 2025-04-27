package models.buildings;

import models.Item;
import models.NPC.NPC;

import java.util.*;

public class Shop extends Building {
    private HashMap<Item, Integer> shopItems;

    int openHour;
    int closeHour;

    NPC owner;

    public Shop(String name) {
        super(name);
    }

    public HashMap<Item, Integer> getAllProducts() {
        return shopItems;
    }

    public HashMap<Item, Integer> getAvailableProducts() {
        HashMap<Item, Integer> availableItems = new HashMap<>();
        for (Item item : shopItems.keySet()) {
            if (shopItems.get(item) != 0) {
                availableItems.put(item, shopItems.get(item));
            }
        }
        return availableItems;
    }
}

