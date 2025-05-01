package models.cropsAndFarming;

import models.ItemStack;

import java.util.ArrayList;

public class Plant {
    Seed seed;
    Crop product;
    int productStack;

    ArrayList<Integer> stages;
    int currentStage;
    int daysOfCurrentStage;

    int daysWithoutWater;
    boolean isWateredToday;
    boolean hasFertilizer;

    boolean oneTimeHarvest;
    Integer regrowthTime;
    int cropIntervalDays;
    boolean canBecomeGiant;

    boolean hasCrop;

    public Plant(Crop product, Seed seed, ArrayList<Integer> stages,
                 boolean oneTimeHarvest, int regrowthTime, boolean canBecomeGiant) {
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
        if (!isWateredToday) {
            daysWithoutWater++;
            return;
        }

        if (currentStage < stages.size()) {
            daysOfCurrentStage--;
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

        isWateredToday = false;
    }

    public ItemStack harvest() {
        if (!hasCrop) {return null;}

        if (oneTimeHarvest) {
            // Todo: remove plant
            productStack = 0;
            hasCrop = false;
            return new ItemStack(product, productStack);
        } else {
            productStack = 0;
            hasCrop = false;
            return new ItemStack(product, productStack);
        }
    }

}

