package models.cropsAndFarming;

import models.ItemStack;
import models.map.Tile;

import java.util.ArrayList;

public class Plant {
    private Tile tile;

    private Seed seed;
    private Crop product;
    private int productStack;

    private ArrayList<Integer> stages;
    private int currentStage;
    private int daysOfCurrentStage;

    private int daysWithoutWater;
    private boolean isWateredToday;
    private boolean hasFertilizer;

    private boolean oneTimeHarvest;
    private Integer regrowthTime;
    private int cropIntervalDays;
    private boolean canBecomeGiant;

    private boolean hasCrop;

    private boolean isAlive = true;

    public Plant(Tile tile, Crop product, Seed seed, ArrayList<Integer> stages,
                 boolean oneTimeHarvest, int regrowthTime, boolean canBecomeGiant) {
        this.tile = tile;
        this.product = product;
        this.productStack = 0;
        this.seed = seed;
        this.stages = stages;
        this.currentStage = 0;
        this.daysOfCurrentStage = stages.get(currentStage);
        this.daysWithoutWater = 0;
        this.isWateredToday = false;
        this.hasFertilizer = false;
        this.oneTimeHarvest = oneTimeHarvest;
        this.regrowthTime = regrowthTime;
        this.cropIntervalDays = 0;
        this.canBecomeGiant = canBecomeGiant;
        this.hasCrop = false;
    }


    public void updateDaily() {
        FertilizerType type = tile.getFertilizer();

        boolean needsWater = (type != FertilizerType.QUALITY);
        boolean isWateredEnough = !needsWater || isWateredToday;

        if (!isWateredEnough) {
            daysWithoutWater++;
            return;
        }

        if (currentStage < stages.size()) {
            int speedBonus = (type == FertilizerType.SPEED) ? 2 : 1;

            daysOfCurrentStage -= speedBonus;

            if (daysOfCurrentStage <= 0) {
                if (currentStage + 1 < stages.size()) {
                    currentStage++;
                    daysOfCurrentStage = stages.get(currentStage);
                } else {
                    daysOfCurrentStage = 0;
                }
            }
        } else if (!oneTimeHarvest && regrowthTime != null) {
            cropIntervalDays++;
            if (cropIntervalDays >= regrowthTime) {
                cropIntervalDays = 0;
                hasCrop = true;
                productStack++;
            }
        } else {
            hasCrop = true;
            productStack = 1;
        }

        if (needsWater) {
            isWateredToday = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
    public void die() {
        isAlive = false;
    }

    public void setIsWateredToday() {
        isWateredToday = true;
    }

    public ItemStack harvest() {
        if (!hasCrop || !isAlive) {return null;}

        if (oneTimeHarvest) {
            productStack = 0;
            hasCrop = false;
            tile.removePlant();
            return new ItemStack(product, productStack);
        } else {
            productStack = 0;
            hasCrop = false;
            return new ItemStack(product, productStack);
        }
    }

    public boolean hasCrop() {
        return hasCrop;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "tile=" + tile +
                ", seed=" + seed +
                ", product=" + product +
                ", productStack=" + productStack +
                ", stages=" + stages +
                ", currentStage=" + currentStage +
                ", daysOfCurrentStage=" + daysOfCurrentStage +
                ", daysWithoutWater=" + daysWithoutWater +
                ", isWateredToday=" + isWateredToday +
                ", hasFertilizer=" + hasFertilizer +
                ", oneTimeHarvest=" + oneTimeHarvest +
                ", regrowthTime=" + regrowthTime +
                ", cropIntervalDays=" + cropIntervalDays +
                ", canBecomeGiant=" + canBecomeGiant +
                ", hasCrop=" + hasCrop +
                ", isAlive=" + isAlive +
                '}';
    }
}