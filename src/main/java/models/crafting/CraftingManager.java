package models.crafting;

import models.Player;
import models.inventory.Inventory;

import java.util.*;

public class CraftingManager {
    private final Set<CraftingRecipe> learnedRecipes = new HashSet<>();

    public void learnRecipe(CraftingRecipe recipe) {
        learnedRecipes.add(recipe);
    }

    public void showLearnedRecipes(Player player) {
        System.out.println("Crafting Recipes:");
        for (CraftingRecipe recipe : learnedRecipes) {
            boolean canCraft = hasAllIngredients(player.getInventory(), recipe);
            String status = canCraft ? "[✓]" : "[✗]";
            System.out.println(status + " " + recipe.data.getName() + " - " + recipe.data.getDescription());
        }
    }

    public boolean craft(String itemName, Player player) {
        if (player.getEnergy() < 2) {
            System.out.println("Not enough energy to craft.");
            return false;
        }

        CraftingRecipe recipe = getRecipeByName(itemName);
        if (recipe == null || !learnedRecipes.contains(recipe)) {
            System.out.println("You haven't learned the recipe for: " + itemName);
            return false;
        }

        Inventory inv = player.getInventory();
        if (!hasAllIngredients(inv, recipe)) {
            System.out.println("You don't have the required ingredients.");
            return false;
        }

        if (!inv.hasSpace()) {
            System.out.println("Inventory is full.");
            return false;
        }

        // consume ingredients
        for (Map.Entry<String, Integer> entry : recipe.ingredients.entrySet()) {
            inv.removeItem(entry.getKey(), entry.getValue());
        }

        inv.addItem(recipe.data.getName(), 1); // assuming 1 crafted item
        player.reduceEnergy(2);
        System.out.println("Crafted: " + recipe.data.getName());
        return true;
    }

    private boolean hasAllIngredients(Inventory inv, CraftingRecipe recipe) {
        for (Map.Entry<String, Integer> entry : recipe.ingredients.entrySet()) {
            if (!inv.hasItem(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private CraftingRecipe getRecipeByName(String name) {
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            if (recipe.data.getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    public Set<CraftingRecipe> getLearnedRecipes() {
        return learnedRecipes;
    }
}
