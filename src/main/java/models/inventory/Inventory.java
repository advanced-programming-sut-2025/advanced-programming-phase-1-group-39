package models.inventory;

import models.Item;
import models.ItemStack;
import models.map.AnsiColors;
import models.map.Tile;

import java.util.ArrayList;
import java.util.HashMap;


public class Inventory {
    InventoryType type = InventoryType.BASIC;
    ArrayList<ItemStack> inventoryItems = new ArrayList<>();
    TrashType trashType = TrashType.BASIC;

    // TODO : set item in hand to first
    ItemStack inHand = null;
//
//    public Inventory(ArrayList<ItemStack>) {
//
//    }

    public String showInventory() {
        StringBuilder text = new StringBuilder();
        text.append("Your inventory:\n--------------\n");
        for (ItemStack item : inventoryItems) {
            text.append(AnsiColors.ANSI_ORANGE_BOLD + "\"" + item.getItem().getName() +"\""+AnsiColors.ANSI_RESET);
            text.append(" : " + item.getAmount());
            text.append("\n");
        }

        return text.toString();
    }

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
        if (itemStack != null) {
            itemStack.addStack(-amount);
            if(!hasEnoughStack(item.getName(), 1)){
                inventoryItems.remove(itemStack);
            }
        } else {
            inventoryItems.add(new ItemStack(item, amount));
        }
    }
    public ItemStack pickItem(String name, int amount) {
        ItemStack item = getItemByName(name);
        if (item != null) {
            item.addStack(-amount); // Todo: it's not complete
            return new ItemStack(item.getItem(), amount);
        }
        return null;
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
            if (item.getItem().getName().equalsIgnoreCase(name) && item.getAmount() >= amount) {
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
        tile.placeItem(item);
    }
}