package models.artisan;

import models.App;
import models.Player;
import models.Result;
import models.Time;

import java.util.ArrayList;

public class BeeHouse extends ArtisanMachine{
    public BeeHouse(String name, int sellPrice) {
        super(name, sellPrice);
        recipes.add(new ArtisanRecipe("Honey",
                "It's a sweet syrup produced by bees.", null, 4, 75, 350));
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
    public Result use(String name, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is busy now!");
        }
        ArtisanRecipe recipe = getRecipeByName(name);
        if (recipe == null) {
            return new Result(false, name + " not found");
        }

        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());
        processingRecipe = recipe;

        return new Result(true, "Machine started to creating " + name);
    }

    @Override
    public ArtisanGood getReadyGoods(String name, Time time) {
        ArtisanRecipe recipe = getRecipeByName(name);
        if (recipe == null) {
            return null;
        }
        if (!processTime.isGreater(time)) {
            return null;
        }
        readyGood = recipe.getGood();
        processingRecipe = null;
        processTime = null;

        return readyGood;
    }
}
