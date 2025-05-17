package models.cooking;

import models.App;
import models.ItemStack;
import models.Player;
import models.Result;
import models.buildings.Cabin;
import models.inventory.Inventory;

import java.util.Map;

public class FoodManager {

    public static Result cook(String foodName, Player player) {

        Cabin cabin = (Cabin) player.getBuildingByName("cabin");
        if (!App.getApp().getCurrentGame().getMap().isInBuilding(cabin, player)) {
            return new Result(false, "You need to be at home to cook.");
        }

        FoodRecipe recipe = getRecipeByName(foodName);
        if (recipe == null) {
            return new Result(false, "This recipe does not exist.");
        }

        if (!player.hasLearnedFoodRecipe(recipe)) {
            return new Result(false, "You haven't learned this recipe yet.");
        }

        if (!hasAllIngredients(player, recipe)) {
            return new Result(false, "Missing required ingredients.");
        }

        if (!player.getInventory().hasSpace(new ItemStack(recipe.data, 1))) {
            return new Result(false, "Inventory full.");
        }

        for (Map.Entry<String, Integer> entry : recipe.ingredients.entrySet()) {
            player.getInventory().pickItem(entry.getKey(), entry.getValue());
        }

        player.getInventory().addItem(recipe.data, 1);
        player.changeEnergy(-3);
        return new Result(true, "Cooked: " + recipe.data.getName());
    }

    public static boolean hasAllIngredients(Player player, FoodRecipe recipe) {
        Inventory inv = player.getInventory();
        for (Map.Entry<String, Integer> entry : recipe.ingredients.entrySet()) {
            String itemName = entry.getKey();
            int count = entry.getValue();
            Cabin cabin = (Cabin) player.getBuildingByName("cabin");

            if (!inv.hasEnoughStack(itemName, count)
                    && !cabin.getRefrigerator().getInventory().hasEnoughStack(itemName, count)) {
                return false;
            }
        }
        return true;
    }

    public static FoodRecipe getRecipeByName(String name) {
        for (FoodRecipe recipe : FoodRecipe.values()) {
            if (recipe.data.getName().equalsIgnoreCase(name)) return recipe;
        }
        return null;
    }
    public static Food getFoodByName(String name) {
        for (FoodRecipe recipe : FoodRecipe.values()) {
            if (recipe.data.getName().equalsIgnoreCase(name)) return recipe.data;
        }
        return null;
    }

    public static Result eat(String foodName, Player player) {
        Inventory inv = player.getInventory();
        FoodRecipe recipe = getRecipeByName(foodName);

        if (recipe == null || !inv.hasItem(foodName)) {
            return new Result(false, "You don't have " + foodName + " in your inventory.");
        }

        inv.pickItem(foodName, 1);
        player.changeEnergy(recipe.data.energy);

        if (recipe.data.buff != null) {
            player.applyBuff(recipe.data.buff);
        } // Todo: handle buff effects

        return new Result(true, "You ate " + foodName + " and gained " + recipe.data.energy + " energy.");
    }
}

