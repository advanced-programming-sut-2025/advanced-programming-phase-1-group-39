package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;

import java.util.ArrayList;

public class Tree{
    String name;

    ArrayList<Integer> growthStages = new ArrayList<>();
    int currentStage = 0;
    int maxGrowthStage;

    boolean isBurnt = false;
    boolean hasFruit = false;

    Season[] fruitSeasons;
    int fruitIntervalDays;


    public void grow() {}
    public void updateDaily() {}
    public void hitByLightning() {isBurnt = true;}
    public ArrayList<Item> cutDown() {return null;}
}
