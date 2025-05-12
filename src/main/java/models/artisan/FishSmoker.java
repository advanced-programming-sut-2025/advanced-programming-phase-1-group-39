package models.artisan;

import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class FishSmoker extends ArtisanMachine {
    public FishSmoker(String name, int sellPrice) {
        super(name, sellPrice);

        addSmokedFish("Salmon", 75);
        addSmokedFish("Sardine", 40);
        addSmokedFish("Shad", 60);
        addSmokedFish("Blue Discus", 120);
        addSmokedFish("Midnight Carp", 150);
        addSmokedFish("Squid", 80);
        addSmokedFish("Tuna", 100);
        addSmokedFish("Perch", 55);
        addSmokedFish("Flounder", 100);
        addSmokedFish("Lionfish", 100);
        addSmokedFish("Herring", 30);
        addSmokedFish("Ghostfish", 45);
        addSmokedFish("Tilapia", 75);
        addSmokedFish("Dorado", 100);
        addSmokedFish("Sunfish", 30);
        addSmokedFish("Rainbow Trout", 65);

        // Legendary fish (optional - expensive smoked versions)
        addSmokedFish("Legend", 5000);
        addSmokedFish("Glacierfish", 1000);
        addSmokedFish("Angler", 900);
        addSmokedFish("Crimsonfish", 1500);
    }

    private void addSmokedFish(String fishName, int basePrice) {
        HashMap<String, Integer> ingredients = new HashMap<>();
        ingredients.put(fishName, 1);
        ingredients.put("Coal", 1);

        int energy = basePrice; // از رابطه بالا
        int sellPrice = basePrice * 2;

        recipes.add(new ArtisanRecipe("Smoked Fish", "A whole fish, smoked to perfection.",
                ingredients, 1, energy, sellPrice));
    }

    public ArtisanRecipe getRecipeByFish(String fishName) {
        for (ArtisanRecipe recipe : recipes) {
            if (recipe.getIngredients().containsKey(fishName)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public Result use(String fishName, String ingredient, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is currently busy!");
        }

        ArtisanRecipe recipe = getRecipeByFish(fishName);
        if (recipe == null || !recipe.getIngredients().containsKey(ingredient)) {
            return new Result(false, "Invalid ingredient: " + ingredient);
        }

        for (String ing : recipe.getIngredients().keySet()) {
            int amount = recipe.getIngredients().get(ing);
            if (!player.getInventory().hasEnoughStack(ing, amount)) {
                return new Result(false, "Not enough " + ing);
            }
        }

        for (String ing : recipe.getIngredients().keySet()) {
            player.getInventory().pickItem(ing, recipe.getIngredients().get(ing));
        }

        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());

        return new Result(true, "Started smoking " + fishName);
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

    public String showAll() {
        StringBuilder sb = new StringBuilder();
        for (ArtisanRecipe recipe : recipes) {
            sb.append(recipe.getName() + "/" + recipe.getEnergy() + "\n");
        }
        return sb.toString();
    }
}