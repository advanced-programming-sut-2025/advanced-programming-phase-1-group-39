package models.artisan;

import models.crafting.CraftingItem;

import java.util.ArrayList;

public class ArtisanMachine extends CraftingItem {
    private ArtisanMachineType type;
    private ArrayList<ArtisanRecipe> recipes;

    private ArrayList<ArtisanGood> processingGoods;
    private ArrayList<ArtisanGood> readyGoods;
    private boolean isWorking;

    public ArtisanMachine(String name, int sellPrice, ArtisanMachineType type) {
        super(name, sellPrice);
        this.type = type;
    }

    public void use() {}
    public ArrayList<ArtisanGood> getReadyGoods() {return null;}
}
