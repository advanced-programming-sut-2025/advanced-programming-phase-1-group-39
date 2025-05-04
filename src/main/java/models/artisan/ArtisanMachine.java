package models.artisan;

import models.Item;

import java.util.ArrayList;

public class ArtisanMachine extends Item {
    private ArrayList<ArtisanRecipe> recipes;

    private ArrayList<ArtisanGood> processingGoods;
    private ArrayList<ArtisanGood> readyGoods;
    private boolean isWorking;

    public ArtisanMachine(String name) {
        super(name);
    }

    public void use() {}
    public ArrayList<ArtisanGood> getReadyGoods() {return null;}
}
