package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class Dehydrator extends ArtisanMachine {
    public Dehydrator(String name, int sellPrice) {
        super(name, sellPrice);


        addDriedMushroomRecipe("Red Mushroom", 75);
        addDriedMushroomRecipe("Common Mushroom", 40);
        addDriedMushroomRecipe("Purple Mushroom", 90);


        addDriedFruitRecipe("Apple", 100);
        addDriedFruitRecipe("Banana", 150);
        addDriedFruitRecipe("Orange", 100);


        HashMap<String, Integer> raisinsIngredients = new HashMap<>();
        raisinsIngredients.put("Grapes", 5);
        recipes.add(new ArtisanRecipe("Raisins", "It's said to be the Junimos' favorite food.",
                raisinsIngredients, 24, 125, 600));
    }

    private void addDriedMushroomRecipe(String mushroomName, int basePrice) {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put(mushroomName, 5);
        int sellPrice = (int) (basePrice * 7.5 + 25);
        recipes.add(new ArtisanRecipe("Dried Mushrooms", "A package of gourmet mushrooms.",
                ingredients, 24, 50, sellPrice));
    }

    private void addDriedFruitRecipe(String fruitName, int basePrice) {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put(fruitName, 5);
        int sellPrice = (int) (basePrice * 7.5 + 25);
        recipes.add(new ArtisanRecipe("Dried Fruit", "Chewy pieces of dried fruit.",
                ingredients, 24, 75, sellPrice));
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
            return new Result(false, "Recipe not found for: " + productName);
        }

        int requiredAmount = recipe.getIngredients().get(ingredientName);
        if (!player.getInventory().hasEnoughStack(ingredientName, requiredAmount)) {
            return new Result(false, "Not enough " + ingredientName);
        }

        player.getInventory().pickItem(ingredientName, requiredAmount);
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
