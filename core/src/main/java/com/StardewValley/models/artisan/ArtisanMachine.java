package com.StardewValley.models.artisan;

import com.StardewValley.models.Player;
import com.StardewValley.models.Result;
import com.StardewValley.models.Time;
import com.StardewValley.models.crafting.CraftingItem;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public abstract class ArtisanMachine extends CraftingItem {
    protected ArrayList<ArtisanRecipe> recipes = new ArrayList<>();

    protected ArtisanRecipe processingRecipe;
    protected ArtisanGood readyGood;
    protected boolean isWorking;
    protected Time processTime;

    public ArtisanMachine(String name, int sellPrice, Texture texture) {
        super(name, sellPrice, texture);
    }

    public Result use(String itemName, String[] ingredients, Time time, Player player) {return null;}
    public ArtisanGood getReadyGoods(String name, Time time) {return null;}
}
