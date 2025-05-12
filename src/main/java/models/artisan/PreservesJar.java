package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class PreservesJar extends ArtisanMachine{
    public PreservesJar(String name, int sellPrice) {
        super(name, sellPrice);
        addPicklesRecipe("Broccoli", 70, 63);
        addPicklesRecipe("Carrot", 35, 75);
        addPicklesRecipe("Corn", 50, 25);


        addJellyRecipe("Apple", 100, 38);
        addJellyRecipe("Banana", 150, 75);
        addJellyRecipe("Orange", 100, 38);
    }

    private void addPicklesRecipe(String mushroomName, int basePrice, int baseEnergy) {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put(mushroomName, 5);
        int sellPrice = (int) (basePrice * 2 + 50);
        int energy = (int) (baseEnergy * 1.75);
        recipes.add(new ArtisanRecipe("Pickles", "A jar of your home-made pickles.",
                ingredients, 6, energy, sellPrice));
    }

    private void addJellyRecipe(String fruitName, int basePrice, int baseEnergy) {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put(fruitName, 5);
        int sellPrice = (int) (basePrice * 2 + 50);
        int energy = (int) (baseEnergy * 2);
        recipes.add(new ArtisanRecipe("Jelly", "Gooey.",
                ingredients, 24, energy, sellPrice));
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
    public Result use(String productName, String ingredientName, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is currently busy!");
        }

        ArtisanRecipe recipe = getRecipeByNameAndIngredient(productName, ingredientName);
        if (recipe == null) {
            return new Result(false, "No recipe found for: " + productName);
        }

        if (!recipe.getIngredients().containsKey(ingredientName)) {
            return new Result(false, ingredientName + " isn't the product's ingredient");
        }

        int required = recipe.getIngredients().get(ingredientName);
        if (!player.getInventory().hasEnoughStack(ingredientName, required)) {
            return new Result(false, "Not enough " + ingredientName);
        }

        player.getInventory().pickItem(ingredientName, required);
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
