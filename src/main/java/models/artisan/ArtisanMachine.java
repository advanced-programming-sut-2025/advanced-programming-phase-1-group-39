package models.artisan;

import models.Player;
import models.Result;
import models.Time;
import models.crafting.CraftingItem;

import java.util.ArrayList;

public abstract class ArtisanMachine extends CraftingItem {
    protected ArrayList<ArtisanRecipe> recipes = new ArrayList<>();

    protected ArtisanRecipe processingRecipe;
    protected ArtisanGood readyGood;
    protected boolean isWorking;
    protected Time processTime;

    public ArtisanMachine(String name, int sellPrice) {
        super(name, sellPrice);
    }

    public Result use(String itemName, String ingredient, Time time, Player player) {return null;}
    public ArtisanGood getReadyGoods(String name, Time time) {return null;}
}