package models.crafting;

import models.App;
import models.ItemStack;
import models.Player;
import models.Result;
import models.buildings.Cabin;
import models.inventory.Inventory;

import java.util.*;

public class CraftingManager {
    private final Set<CraftingRecipe> learnedRecipes = new HashSet<>();

    public static void learnRecipe(CraftingRecipe recipe, Player player) {
        player.learnCraftingRecipe(recipe);
    }

    public static Result craft(String itemName, Player player) {
        Cabin cabin = (Cabin) App.getApp().getCurrentGame().getPlayerInTurn().getBuildingByName("cabin");
        if (!App.getApp().getCurrentGame().getMap().isInBuilding(cabin, player)) {
            return new Result(false, "You should be at your home");
        }

        if (player.getEnergy() < 2) {
            return new Result(false, "Not enough energy to craft.");
        }

        CraftingRecipe recipe = getRecipeByName(itemName);
        if (recipe == null || !player.hasLearnedCraftingRecipe(recipe)) {
            return new Result(false, "You haven't learned the recipe for: " + itemName);
        }

        Inventory inv = player.getInventory();
        if (!hasAllIngredients(inv, recipe)) {
            return new Result(false, "You don't have the required ingredients.");
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            inv.pickItem(entry.getKey(), entry.getValue());
        }

        if (!inv.hasSpace(new ItemStack(recipe.getItem(), 1))) {
            return new Result(false, "Your inventory has not space anymore!");
        }
        inv.addItem(recipe.getItem(), 1);
        player.changeEnergy(-2);
        return new Result(true, "Crafted: " + recipe.getItem().getName());
    }

    public static boolean hasAllIngredients(Inventory inv, CraftingRecipe recipe) {
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!inv.hasEnoughStack(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static CraftingRecipe getRecipeByName(String name) {
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            if (recipe.getItem().getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }
}
