package models.cropsAndFarming;

import models.Enums.Season;
import models.Item;
import models.ItemStack;
import models.map.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tree{
    private String name;
    private Tile tile;
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


    public Tree(String name, Tile tile, Seed source, ArrayList<Integer> stages, int totalHarvestTime,
                String fruitName, int fruitHarvestCycle, int fruitBaseSellPrice, boolean isFruitEdible,
                Integer fruitEnergy, Integer fruitHealth, Season[] seasons) {
        this.name = name;
        this.tile = tile;
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

    public void setStageToLast() {
        currentStage = stages.size() - 1;
        daysOfCurrentStage = 0;
    }

    public ItemStack harvest() {
        if (hasFruit) {
            fruitStack = 0;
            hasFruit = false;
            return new ItemStack(fruit, fruitStack);
        }
        return null;
    }
    public void updateDaily() {
        if (isBurnt) return;

        if (currentStage < stages.size()) {
            daysOfCurrentStage--;
            if (daysOfCurrentStage <= 0) {
                currentStage++;
                if (currentStage < stages.size()) {
                    daysOfCurrentStage = stages.get(currentStage);
                } else {
                    daysOfCurrentStage = 0;
                    fruitIntervalDays = 0;
                }
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
            return new ItemStack(ForagingManager.getMineralByName("Coal"), 5);
        }
    }

    @Override
    public String toString() {
        return "Tree{" +
                "name='" + name + '\'' +
                ", tile=" + tile +
                ", source=" + source +
                ", stages=" + stages +
                ", currentStage=" + currentStage +
                ", daysOfCurrentStage=" + daysOfCurrentStage +
                ", totalHarvestTime=" + totalHarvestTime +
                ", fruitName='" + fruitName + '\'' +
                ", fruit=" + fruit +
                ", fruitStack=" + fruitStack +
                ", fruitHarvestCycle=" + fruitHarvestCycle +
                ", fruitIntervalDays=" + fruitIntervalDays +
                ", fruitBaseSellPrice=" + fruitBaseSellPrice +
                ", isFruitEdible=" + isFruitEdible +
                ", fruitEnergy=" + fruitEnergy +
                ", fruitHealth=" + fruitHealth +
                ", seasons=" + Arrays.toString(seasons) +
                ", isBurnt=" + isBurnt +
                ", hasFruit=" + hasFruit +
                '}';
    }
}
