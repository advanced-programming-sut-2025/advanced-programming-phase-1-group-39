package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree{
    private String name;
    private Seed source; //seed
    private ArrayList<Integer> stages = new ArrayList<>();
    private int currentStage;
    private int totalHarvestTime;
    private String fruitName;
    private int fruitHarvestCycle;
    private int fruitBaseSellPrice;
    private boolean isFruitEdible;
    private Integer fruitEnergy;
    private Integer fruitHealth;
    private Season[] seasons; // season
    //int maxGrowthStage;

    boolean isBurnt = false;
    boolean hasFruit = false;


    public Tree(String name, Seed source, ArrayList<Integer> stages, int currentStage, int totalHarvestTime,
                String fruitName, int fruitHarvestCycle, int fruitBaseSellPrice, boolean isFruitEdible,
                Integer fruitEnergy, Integer fruitHealth, Season[] seasons) {
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.currentStage = 0;
        this.totalHarvestTime = totalHarvestTime;
        this.fruitName = fruitName;
        this.fruitHarvestCycle = fruitHarvestCycle;
        this.fruitBaseSellPrice = fruitBaseSellPrice;
        this.isFruitEdible = isFruitEdible;
        this.fruitEnergy = fruitEnergy;
        this.fruitHealth = fruitHealth;
        this.seasons = seasons;
    }

    public String getName() {
        return name;
    }

    public Seed getSource() {
        return source;
    }

    public void grow() {}
    public void updateDaily() {}
    public void hitByLightning() {isBurnt = true;}
    public ArrayList<Item> cutDown() {return null;}

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(name);
        str.append("\n");
        str.append(stages);
        str.append("\n");
        str.append(currentStage);
        str.append("\n");
        str.append(totalHarvestTime);
        str.append("\n");
        str.append(fruitName);
        str.append("\n");
        str.append(fruitHarvestCycle);
        str.append("\n");
        str.append(fruitBaseSellPrice);
        str.append("\n");
        str.append(isFruitEdible);
        str.append("\n");
        str.append(fruitEnergy);
        str.append("\n");
        str.append(fruitHealth);
        str.append("\n");
        str.append(Arrays.toString(seasons));
        str.append("\n");
        return str.toString();


    }
}
