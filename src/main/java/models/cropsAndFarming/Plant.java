package models.cropsAndFarming;

import models.Item;

import java.util.ArrayList;

class Plant {
    Crop product;
    Seed seed;

    ArrayList<Integer> stages;
    int currentStage = 0;

    int daysWithoutWater = 0;
    boolean isWateredToday = false;

    boolean hasFertilizer = false;

    boolean oneTimeHarvest;
    Integer regrowthTime;
    boolean canBecomeGiant;
}

