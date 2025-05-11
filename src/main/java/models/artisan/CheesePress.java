package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class CheesePress extends ArtisanMachine {

    public CheesePress(String name, int sellPrice) {
        super(name, sellPrice);

        HashMap<String, Integer> milkCheeseIngredients = new HashMap<>();
        milkCheeseIngredients.put("Milk", 1);
        recipes.add(new ArtisanRecipe("Cheese",
                "It's your basic cheese.",
                milkCheeseIngredients, 3, 100, 230));

        HashMap<String, Integer> largeMilkCheeseIngredients = new HashMap<>();
        largeMilkCheeseIngredients.put("Large Milk", 1);
        recipes.add(new ArtisanRecipe("Cheese",
                "It's your basic cheese (from large milk).",
                largeMilkCheeseIngredients, 3, 100, 345));


        // Goat Cheese
        HashMap<String, Integer> goatCheeseIngredients = new HashMap<>();
        goatCheeseIngredients.put("Goat Milk", 1);
        recipes.add(new ArtisanRecipe("Goat Cheese",
                "Soft cheese made from goat's milk.",
                goatCheeseIngredients, 3, 100, 400));

        HashMap<String, Integer> largeGoatCheeseIngredients = new HashMap<>();
        largeGoatCheeseIngredients.put("Large Goat Milk", 1);
        recipes.add(new ArtisanRecipe("Goat Cheese",
                "Soft cheese made from goat's milk. (large milk)",
                largeGoatCheeseIngredients, 3, 100, 600));
    }

    public ArtisanRecipe getRecipeByNameAndIngredient(String name, String ingredient) {
        for (ArtisanRecipe recipe : recipes) {
            if (name.equalsIgnoreCase(recipe.getName()) && recipe.getIngredients().containsKey(ingredient)) {
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

        ArtisanRecipe recipe = getRecipeByNameAndIngredient(itemName, ingredient);
        if (recipe == null) {
            return new Result(false, itemName + " not found.");
        }
        if (!recipe.getIngredients().containsKey(ingredient)) {
            return new Result(false, ingredient + " isn't the product's ingredient");
        }
        if (!player.getInventory().hasEnoughStack(ingredient, recipe.getIngredients().get(ingredient))) {
            return new Result(false, "You doesn't have enough ingredients ):");
        }

        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());
        player.getInventory().pickItem(ingredient, recipe.getIngredients().get(ingredient));

        return new Result(true, "Started processing: " + itemName);
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
