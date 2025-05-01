package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;
import models.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree{
    private String name;
    private Seed source;
    private ArrayList<Integer> stages = new ArrayList<>();
    private int currentStage;
    private int daysOfCurrentStage;
    private int totalHarvestTime;
    private String fruitName;
    private FarmingProduct fruit;
    private int fruitStack;
    private int fruitHarvestCycle;
    private int fruitIntervalDays;
    private int fruitBaseSellPrice;
    private boolean isFruitEdible;
    private Integer fruitEnergy;
    private Integer fruitHealth;
    private Season[] seasons;

    boolean isBurnt = false;
    boolean hasFruit = false;


    public Tree(String name, Seed source, ArrayList<Integer> stages, int totalHarvestTime,
                String fruitName, int fruitHarvestCycle, int fruitBaseSellPrice, boolean isFruitEdible,
                Integer fruitEnergy, Integer fruitHealth, Season[] seasons) {
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.currentStage = 0;
        this.daysOfCurrentStage = stages.get(currentStage);
        this.totalHarvestTime = totalHarvestTime;
        this.fruitName = fruitName;
        this.fruitHarvestCycle = fruitHarvestCycle;
        this.fruitIntervalDays = 0;
        this.fruitStack = 0;
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

    public ItemStack harvest() {
        if (hasFruit) {
            return new ItemStack(fruit, fruitStack);
        }
        return null;
    }
    public void updateDaily() {
        if (isBurnt) {return;}

        if (currentStage < stages.size()) {
            daysOfCurrentStage --;
            if (daysOfCurrentStage <= 0) {
                daysOfCurrentStage = 0;
                currentStage++;
            }
        } else {
            fruitIntervalDays++;
            if (fruitIntervalDays >= fruitHarvestCycle) {
                fruitIntervalDays = 0;
                hasFruit = true;
                fruitStack++;
            }
        }
    }
    public void burn() {isBurnt = true;}
    public ItemStack cutDown() {
        if (!isBurnt) {
            return new ItemStack(source, 1);
        } else {
            //Todo: return coal
        }
        return null;
    }
}