package models.inventory;

import models.Item;
import models.ItemStack;

import java.util.HashMap;


public class Inventory {
    InventoryType type;
    ItemStack inventoryItems;
    TrashType trashType;

    ItemStack inHand;

    public String showInventory() {return null;}

    public void trashItem(Item item) {}
}
