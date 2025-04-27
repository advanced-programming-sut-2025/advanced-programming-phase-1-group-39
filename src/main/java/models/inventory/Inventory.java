package models.inventory;

import models.Item;

import java.util.HashMap;


public class Inventory {
    InventoryType type;
    //ToDO: change
    HashMap<Item, Integer> inventoryItems;
    TrashType trashType;

    Item inHand;

    public String showInventory() {return null;}

    public void trashItem(Item item) {}
}
