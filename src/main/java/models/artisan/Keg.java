package models.artisan;

import models.ItemManager;
import models.Player;
import models.Result;
import models.Time;

import java.util.HashMap;

public class Keg extends ArtisanMachine {
    public Keg(String name, int sellPrice) {
        super(name, sellPrice);

        HashMap<String, Integer> beerIngredients = new HashMap<>();
        beerIngredients.put("Wheat", 1);
        recipes.add(new ArtisanRecipe("Beer", "Drink in moderation.", beerIngredients,
                24, 50, 200));


        HashMap<String, Integer> vinegarIngredients = new HashMap<>();
        vinegarIngredients.put("Rice", 1);
        recipes.add(new ArtisanRecipe("Vinegar", "An aged fermented liquid used in many cooking recipes.",
                vinegarIngredients, 10, 13, 100));


        HashMap<String, Integer> coffeeIngredients = new HashMap<>();
        coffeeIngredients.put("Coffee Bean", 5);
        recipes.add(new ArtisanRecipe("Coffee", "It smells delicious. This is sure to give you a boost.",
                coffeeIngredients, 2, 75, 150));


        HashMap<String, Integer> CarrotJuiceIngredients = new HashMap<>();
        CarrotJuiceIngredients.put("Carrot", 1);
        recipes.add(new ArtisanRecipe("Carrot Juice", "A sweet, nutritious beverage.",
                CarrotJuiceIngredients, 96, 150, 80));
        HashMap<String, Integer> CornJuiceIngredients = new HashMap<>();
        CornJuiceIngredients.put("Corn", 1);
        recipes.add(new ArtisanRecipe("Corn Juice", "A sweet, nutritious beverage.",
                CornJuiceIngredients, 96, 40, 120));
        HashMap<String, Integer> EggplantJuiceIngredients = new HashMap<>();
        EggplantJuiceIngredients.put("Eggplant", 1);
        recipes.add(new ArtisanRecipe("Eggplant Juice", "A sweet, nutritious beverage.",
                EggplantJuiceIngredients, 96, 150, 80));


        HashMap<String, Integer> meadIngredients = new HashMap<>();
        meadIngredients.put("Honey", 1);
        recipes.add(new ArtisanRecipe("Mead", "A fermented beverage made from honey.\nDrink in moderation.",
                meadIngredients, 10, 100, 300));


        HashMap<String, Integer> paleAleIngredients = new HashMap<>();
        paleAleIngredients.put("Hops", 1);
        recipes.add(new ArtisanRecipe("Pale Ale", "Drink in moderation.", paleAleIngredients,
                72, 50, 300));


        HashMap<String, Integer> AppleWineIngredients = new HashMap<>();
        AppleWineIngredients.put("Apple", 1);
        recipes.add(new ArtisanRecipe("Apple Wine", "Drink in moderation.", AppleWineIngredients,
                168, 80, 300));
        HashMap<String, Integer> BananaWineIngredients = new HashMap<>();
        BananaWineIngredients.put("Banana", 1);
        recipes.add(new ArtisanRecipe("Banana Wine", "Drink in moderation.", BananaWineIngredients,
                168, 150, 450));
        HashMap<String, Integer> OrangeWineIngredients = new HashMap<>();
        OrangeWineIngredients.put("Orange", 1);
        recipes.add(new ArtisanRecipe("Orange Wine", "Drink in moderation.", OrangeWineIngredients,
                168, 80, 300));

        for (ArtisanRecipe recipe : recipes) {
            ItemManager.addArtisanGood(recipe.getGood(), name);
        }
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
    public Result use(String productName, String[] ingredients, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is currently busy!");
        }

        ArtisanRecipe recipe = getRecipeByNameAndIngredient(productName, ingredients[0]);
        if (recipe == null) {
            return new Result(false, "Recipe not found for: " + productName);
        }
        if (!recipe.getIngredients().containsKey(ingredients[0])) {
            return new Result(false, ingredients[0] + " isn't the product's ingredient");
        }

        if (!player.getInventory().hasEnoughStack(ingredients[0], recipe.getIngredients().get(ingredients[0]))) {
            return new Result(false, "Not enough " + ingredients[0]);
        }

        processingRecipe = recipe;
        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());
        player.getInventory().pickItem(ingredients[0], recipe.getIngredients().get(ingredients[0]));

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
