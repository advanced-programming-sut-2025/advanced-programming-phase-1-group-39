package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class MayonnaiseMachine extends ArtisanMachine {
    public MayonnaiseMachine(String name, int sellPrice) {
        super(name, sellPrice);


        HashMap<String, Integer> eggIngredients = new HashMap<>();
        eggIngredients.put("Egg", 1);
        recipes.add(new ArtisanRecipe("Mayonnaise", "It looks spreadable.",
                eggIngredients, 3, 50, 190));


        HashMap<String, Integer> largeEggIngredients = new HashMap<>();
        largeEggIngredients.put("Large Egg", 1);
        recipes.add(new ArtisanRecipe("Mayonnaise", "It looks spreadable.",
                largeEggIngredients, 3, 50, 237));


        HashMap<String, Integer> duckIngredients = new HashMap<>();
        duckIngredients.put("Duck Egg", 1);
        recipes.add(new ArtisanRecipe("Duck Mayonnaise", "It's a rich, yellow mayonnaise.",
                duckIngredients, 3, 75, 375));


        HashMap<String, Integer> dinoIngredients = new HashMap<>();
        dinoIngredients.put("Dinosaur Egg", 1);
        recipes.add(new ArtisanRecipe("Dinosaur Mayonnaise",
                "It's thick and creamy, with a vivid green hue.\nIt smells like grass and leather.",
                dinoIngredients, 3, 125, 800));
    }

    public ArtisanRecipe getRecipeByNameAndIngredient(String name, String ingredient) {
        for (ArtisanRecipe recipe : recipes) {
            if (name.equalsIgnoreCase(recipe.getName()) &&
                    recipe.getIngredients().containsKey(ingredient)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public Result use(String productName, String[] ingredients, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is currently busy!");
        }

        ArtisanRecipe recipe = getRecipeByNameAndIngredient(productName, ingredients[0]);
        if (recipe == null) {
            return new Result(false, "No recipe found for: " + productName);
        }

        if (!recipe.getIngredients().containsKey(ingredients[0])) {
            return new Result(false, ingredients[0] + " isn't the product's ingredient");
        }

        int required = recipe.getIngredients().get(ingredients[0]);
        if (!player.getInventory().hasEnoughStack(ingredients[0], required)) {
            return new Result(false, "Not enough " + ingredients[0]);
        }

        player.getInventory().pickItem(ingredients[0], required);
        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());

        return new Result(true, "Started processing " + productName);
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
