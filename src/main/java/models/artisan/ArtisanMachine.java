package models.artisan;

import java.util.ArrayList;

public class ArtisanMachine {
    private ArrayList<ArtisanRecipe> recipes;

    private ArrayList<ArtisanGood> processingGoods;
    private ArrayList<ArtisanGood> readyGoods;
    private boolean isWorking;

    public void use() {}
    public ArrayList<ArtisanGood> getReadyGoods() {return null;}
}
