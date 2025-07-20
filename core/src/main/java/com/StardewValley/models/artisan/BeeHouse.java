package com.StardewValley.models.artisan;

import com.StardewValley.models.ItemManager;
import com.StardewValley.models.Player;
import com.StardewValley.models.Result;
import com.StardewValley.models.Time;
import com.StardewValley.models.artisan.ArtisanMachine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BeeHouse extends ArtisanMachine {
    public BeeHouse(String name, int sellPrice, Texture texture) {
        super(name, sellPrice, texture);
        recipes.add(new ArtisanRecipe("Honey",
                "It's a sweet syrup produced by bees.", null, 4, 75, 350,
                new Texture(Gdx.files.internal("artisanGoods/Honey.png"))));

        for (ArtisanRecipe recipe : recipes) {
            ItemManager.addArtisanGood(recipe.getGood(), name);
        }
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
    public Result use(String itemName, String[] ingredients, Time time, Player player) {
        if (processingRecipe != null) {
            return new Result(false, "Machine is busy now!");
        }
        ArtisanRecipe recipe = getRecipeByName(itemName);
        if (recipe == null) {
            return new Result(false, itemName + " not found");
        }

        processTime = time.clone();
        processTime.addToHour(recipe.getProcessingTime());
        processingRecipe = recipe;

        return new Result(true, "Machine started to creating " + itemName);
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
