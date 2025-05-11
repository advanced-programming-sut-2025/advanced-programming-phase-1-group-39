package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class Furnace extends ArtisanMachine {
    public Furnace(String name, int sellPrice) {
        super(name, sellPrice);

        // Copper Bar
        HashMap<String, Integer> copperIngredients = new HashMap<>();
        copperIngredients.put("Copper", 5);
        copperIngredients.put("Coal", 1);
        recipes.add(new ArtisanRecipe("Copper Bar", "Turns Copper Ore and Coal into a bar.",
                copperIngredients, 4, 0, 50));

        HashMap<String, Integer> ironIngredients = new HashMap<>();
        ironIngredients.put("Iron", 5);
        ironIngredients.put("Coal", 1);
        recipes.add(new ArtisanRecipe("Iron Bar", "Turns Iron Ore and Coal into a bar.",
                ironIngredients, 4, 0, 100));

        HashMap<String, Integer> goldIngredients = new HashMap<>();
        goldIngredients.put("Gold", 5);
        goldIngredients.put("Coal", 1);
        recipes.add(new ArtisanRecipe("Gold Bar", "Turns Gold Ore and Coal into a bar.",
                goldIngredients, 4, 0, 250));

        HashMap<String, Integer> iridiumIngredients = new HashMap<>();
        goldIngredients.put("Gold", 5);
        goldIngredients.put("Coal", 1);
        recipes.add(new ArtisanRecipe("Gold Bar", "Turns Gold Ore and Coal into a bar.",
                goldIngredients, 4, 0, 250));
    }

    public ArtisanRecipe getRecipeByOre(String oreName) {
        for (ArtisanRecipe recipe : recipes) {
            if (recipe.getIngredients().containsKey(oreName)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public Result use(String oreName, String ingredient, Time time, Player player) {
        ArtisanRecipe recipe = getRecipeByOre(oreName);
        if (recipe == null) {
            return new Result(false, "No recipe found for ore: " + oreName);
        }
        if (!recipe.getIngredients().containsKey(ingredient)) {
            return new Result(false, ingredient + " isn't product's ingredient");
        }

        for (String ing : recipe.getIngredients().keySet()) {
            int required = recipe.getIngredients().get(ing);
            if (!player.getInventory().hasEnoughStack(ing, required)) {
                return new Result(false, "Not enough " + ing);
            }
        }

        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());

        for (String ing : recipe.getIngredients().keySet()) {
            player.getInventory().pickItem(ing, recipe.getIngredients().get(ing));
        }

        return new Result(true, "Processing " + recipe.getName());
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
