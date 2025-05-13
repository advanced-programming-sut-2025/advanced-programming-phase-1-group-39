package models.inventory;

import models.Item;
import models.ItemStack;
import models.animals.AnimalProduct;
import models.artisan.ArtisanGood;
import models.artisan.ArtisanMachine;
import models.crafting.CraftingItem;
import models.cropsAndFarming.*;
import models.map.AnsiColors;
import models.map.Tile;
import models.tools.Tool;

import java.util.ArrayList;
import java.util.List;


public class Inventory {
    InventoryType type = InventoryType.BASIC;
    ArrayList<ItemStack> inventoryItems = new ArrayList<>();
    TrashType trashType = TrashType.BASIC;

    ItemStack inHand = null;

    public Inventory(List<ItemStack> firstItems) {
        inventoryItems.addAll(firstItems);
        inHand = firstItems.get(0);
    }

    public String getItemAndColor(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) {
            return AnsiColors.ANSI_RESET + "Empty Slot" + AnsiColors.ANSI_RESET;
        }

        Item item = itemStack.getItem();
        String itemName = item.getName();
        String colorCode;

        if (item instanceof Tool) {
            colorCode = AnsiColors.ANSI_CYAN_BOLD;
        } else if (item instanceof Seed) {
            colorCode = AnsiColors.ANSI_LIGHT_YELLOW_BOLD;
        } else if (item instanceof FarmingProduct) {
            colorCode = AnsiColors.ANSI_GREEN_BOLD; // Using the custom bold green
        } else if (item instanceof ForagingCrop) {
            colorCode = AnsiColors.ANSI_ORANGE_BOLD;
        } else if (item instanceof ForagingMaterial) {
            colorCode = AnsiColors.ANSI_BROWN_BOLD;
        } else if (item instanceof ForagingMineral) {
            colorCode = AnsiColors.ANSI_DARK_GRAY_BOLD;
        } else if (item instanceof AnimalProduct) {
            colorCode = AnsiColors.ANSI_PURPLE_BOLD;
        } else if (item instanceof ArtisanGood) {
            colorCode = AnsiColors.ANSI_RED_BOLD; // Using the new golden text color
        } else if (item instanceof ArtisanMachine) {
            colorCode = AnsiColors.ANSI_CYAN_BOLD; // Using the new medium gray text color
        } else if (item instanceof CraftingItem) {
            colorCode = AnsiColors.ANSI_WHITE;
        } else if (item instanceof Crop) {
            colorCode = AnsiColors.ANSI_GREEN; // Using standard green for the plant itself
        } else {
            colorCode = AnsiColors.ANSI_RESET;
        }

        return colorCode + itemName + AnsiColors.ANSI_RESET;
    }

    public String showInventory() {
        StringBuilder text = new StringBuilder();
        text.append(AnsiColors.ANSI_GREEN_BOLD + "Your inventory:\n--------------\n" + AnsiColors.ANSI_RESET);
        for (ItemStack item : inventoryItems) {
            text.append(getItemAndColor(item));
            text.append(" : " + item.getAmount());
            text.append("\n");
        }

        return text.toString();
    }

    public String getTrashTypeName() {
        return trashType.name();
    }

    public TrashType getTrashType() {
        return trashType;
    }

    public int trashItem(ItemStack item, int number) {
        int itemPrice = 0;
//      TODO :
//        itemPrice = item.getPrice();
        return (int)(trashType.quantifier * itemPrice);
    }

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

    public void setInHand(ItemStack itemStack) {
        if (inventoryItems.contains(itemStack))
            inHand = itemStack;
    }
}