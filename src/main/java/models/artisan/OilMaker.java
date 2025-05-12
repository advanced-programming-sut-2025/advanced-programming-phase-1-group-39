package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class OilMaker extends ArtisanMachine {
    public OilMaker(String name, int sellPrice) {
        super(name, sellPrice);


        HashMap<String, Integer> truffleIngredients = new HashMap<>();
        truffleIngredients.put("Truffle", 1);
        recipes.add(new ArtisanRecipe("Truffle Oil", "A gourmet cooking ingredient.",
                truffleIngredients, 6, 38, 1065));


        HashMap<String, Integer> cornIngredients = new HashMap<>();
        cornIngredients.put("Corn", 1);
        recipes.add(new ArtisanRecipe("Oil", "All purpose cooking oil.",
                cornIngredients, 6, 13, 100));


        HashMap<String, Integer> seedIngredients = new HashMap<>();
        seedIngredients.put("Sunflower Seeds", 1);
        recipes.add(new ArtisanRecipe("Oil", "All purpose cooking oil.",
                seedIngredients, 48, 13, 100));


        HashMap<String, Integer> flowerIngredients = new HashMap<>();
        flowerIngredients.put("Sunflower", 1);
        recipes.add(new ArtisanRecipe("Oil", "All purpose cooking oil.",
                flowerIngredients, 1, 13, 100));
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
