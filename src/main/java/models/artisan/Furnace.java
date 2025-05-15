package models.artisan;

import models.ItemManager;
import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class Furnace extends ArtisanMachine {
    public Furnace(String name, int sellPrice) {
        super(name, sellPrice);


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
        iridiumIngredients.put("Iridium", 5);
        iridiumIngredients.put("Coal", 1);
        recipes.add(new ArtisanRecipe("Iridium Bar", "Turns Iridium Ore and Coal into a bar.",
                iridiumIngredients, 4, 0, 1000));

        for (ArtisanRecipe recipe : recipes) {
            ItemManager.addArtisanGood(recipe.getGood(), name);
        }
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
    public Result use(String oreName, String[] inputIngredients, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is currently busy!");
        }

        ArtisanRecipe recipe = getRecipeByOre(oreName);
        if (recipe == null) {
            return new Result(false, "No recipe found for ore: " + oreName);
        }

        HashMap<String, Integer> requiredIngredients = recipe.getIngredients();
        HashMap<String, Integer> inputMap = new HashMap<>();

        for (String ing : inputIngredients) {
            inputMap.put(ing, inputMap.getOrDefault(ing, 0) + 1);
        }

        for (String required : requiredIngredients.keySet()) {
            int requiredAmount = requiredIngredients.get(required);
            int providedAmount = inputMap.getOrDefault(required, 0);

            if (providedAmount < requiredAmount) {
                return new Result(false, "Missing or insufficient ingredient: " + required);
            }

            if (!player.getInventory().hasEnoughStack(required, requiredAmount)) {
                return new Result(false, "Not enough " + required + " in inventory");
            }
        }

        for (String required : requiredIngredients.keySet()) {
            player.getInventory().pickItem(required, requiredIngredients.get(required));
        }

        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());

        return new Result(true, "Started processing " + recipe.getName());
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
