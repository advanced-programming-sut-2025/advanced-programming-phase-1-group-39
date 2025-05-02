package models.crafting;

import models.Player;
import models.Result;
import models.inventory.Inventory;

import java.util.*;

public class CraftingManager {
    private final Set<CraftingRecipe> learnedRecipes = new HashSet<>();

    public void learnRecipe(CraftingRecipe recipe, Player player) {
        player.learnCraftingRecipe(recipe);
    }

    public Result craft(String itemName, Player player) {
        if (player.getEnergy() < 2) {
            return new Result(false, "Not enough energy to craft."); // Todo: must add concious
        }

        CraftingRecipe recipe = getRecipeByName(itemName);
        if (recipe == null || !player.hasLearnedCraftingRecipe(recipe)) {
            return new Result(false, "You haven't learned the recipe for: " + itemName);
        }

        Inventory inv = player.getInventory();
        if (!hasAllIngredients(inv, recipe)) {
            return new Result(false, "You don't have the required ingredients.");
        }

        if (!inv.hasSpace()) {
            return new Result(false, "Inventory is full.");
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            inv.removeItem(entry.getKey(), entry.getValue());
        }

        inv.addItem(recipe.getItem(), 1);
        player.changeEnergy(-2);
        return new Result(true, "Crafted: " + recipe.getItem().getName());
    }

    private boolean hasAllIngredients(Inventory inv, CraftingRecipe recipe) {
        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!inv.hasEnoughStack(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private CraftingRecipe getRecipeByName(String name) {
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            if (recipe.getItem().getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }
}