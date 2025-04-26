package models.cropsAndFarming;

import java.util.ArrayList;

class Plant {
    Crop product;
    Seed seed;

    ArrayList<Integer> stages;
    int currentStage;

    int daysWithoutWater;
    boolean isWateredToday;

    boolean hasFertilizer;

    boolean oneTimeHarvest;
    Integer regrowthTime;
    boolean canBecomeGiant;

    public Plant(Crop product, Seed seed, ArrayList<Integer> stages,
                 boolean oneTimeHarvest, int regrowthTime, boolean canBecomeGiant) {
        this.product = product;
        this.seed = seed;
        this.stages = stages;
        this.currentStage = 0;
        this.daysWithoutWater = 0;
        this.isWateredToday = false;
        this.hasFertilizer = false;
        this.oneTimeHarvest = oneTimeHarvest;
        this.regrowthTime = regrowthTime;
        this.canBecomeGiant = canBecomeGiant;
    }
}

