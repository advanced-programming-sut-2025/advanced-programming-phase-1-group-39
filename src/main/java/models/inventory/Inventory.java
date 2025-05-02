package models.inventory;

import models.Item;
import models.ItemStack;
import models.map.Tile;

import java.util.ArrayList;
import java.util.HashMap;


public class Inventory {
    InventoryType type;
    //ToDO: change
    ArrayList<ItemStack> inventoryItems;
    TrashType trashType;

    ItemStack inHand;

    public String showInventory() {return null;}

    public void trashItem(Item item) {}

    public boolean hasSpace() {
        return inventoryItems.size() < type.getCapacity();
    }

    public ItemStack getItemByName(String name) {
        for (ItemStack item : inventoryItems) {
            if (item.getItem().getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void addItem(Item item, int amount) {
        ItemStack itemStack = getItemByName(item.getName());
        if (item != null) {
            itemStack.addStack(-amount); // Todo: it's not complete
        } else {
            inventoryItems.add(new ItemStack(item, amount));
        }
    }
    public void removeItem(String name, int amount) {
        ItemStack item = getItemByName(name);
        if (item != null) {
            item.addStack(-amount); // Todo: it's not complete
        }
    }

    public boolean hasItem(String name) {
        for (ItemStack item : inventoryItems) {
            if (item.getItem().getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public boolean hasEnoughStack(String name, int amount) {
        for (ItemStack item : inventoryItems) {
            if (item.getItem().getName().equalsIgnoreCase(name) && item.getAmount() == amount) {
                return true;
            }
        }
        return false;
    }

    public void placeItem(String name, Tile tile) {
        ItemStack item = getItemByName(name);
        if (item == null) {
            return;
        }

    }
}
