package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class Loom extends ArtisanMachine{
    public Loom(String name, int sellPrice) {
        super(name, sellPrice);

        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put("Wool", 1);
        recipes.add(new ArtisanRecipe("Cloth", "A bolt of fine wool cloth.",
                ingredients, 4, 0, 470));
    }

    public ArtisanRecipe getRecipeByName(String name) {
        for (ArtisanRecipe recipe : recipes) {
            if (name.equalsIgnoreCase(recipe.getName())) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public Result use(String itemName, String ingredient, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is currently busy!");
        }

        ArtisanRecipe recipe = getRecipeByName(itemName);
        if (recipe == null) {
            return new Result(false, "Recipe not found for: " + itemName);
        }

        if (!recipe.getIngredients().containsKey(ingredient)) {
            return new Result(false, ingredient + " isn't the product's ingredient");
        }

        int requiredAmount = recipe.getIngredients().get(ingredient);
        if (!player.getInventory().hasEnoughStack(ingredient, requiredAmount)) {
            return new Result(false, "Not enough " + ingredient);
        }

        player.getInventory().pickItem(ingredient, requiredAmount);
        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());

        return new Result(true, "Started processing " + itemName);
    }

    @Override
    public ArtisanGood getReadyGoods(String name, Time time) {
        if (processingRecipe == null || !processingRecipe.getName().equalsIgnoreCase(name)) {
            return null;
        }

        if (!time.isGreater(processTime)) {
            return null;
        }

        readyGood = processingRecipe.getGood();
        processingRecipe = null;
        processTime = null;

        return readyGood;
    }
}
