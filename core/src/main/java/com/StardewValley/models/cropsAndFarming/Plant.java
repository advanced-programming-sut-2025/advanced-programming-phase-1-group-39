package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.ItemManager;
import com.StardewValley.models.ItemStack;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Plant {
    private Tile tile;
    private String seedName;
    private transient Seed seed;
    private String cropName;
    private transient Crop product;

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
        this.cropName = product.getName();

        this.productStack = 0;
        this.seed = seed;
        this.seedName = seed.getName();

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

    public Plant() {}

    public void updateDaily() {
        FertilizerType type = tile.getFertilizer();

        boolean needsWater = (type != FertilizerType.QUALITY);
        boolean isWateredEnough = !needsWater || isWateredToday;

        if (!isWateredEnough) {
            daysWithoutWater++;
            return;
        }

        if (currentStage < stages.size() - 1) {
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

//        if (needsWater) {
//            isWateredToday = false;
//        } // cheat: comment this
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
            return new ItemStack(product, 1);
        } else {
            hasCrop = false;
            ItemStack stack = new ItemStack(product, productStack);
            productStack = 0;
            return stack;
        }
    }

    public boolean hasCrop() {
        return hasCrop;
    }

    // Graphics
    // TODO : check
    public TextureRegion getTexture() {
        TextureAtlas cropsAtlas = GameAssetManager.getCropsAtlas();

        String name = product.getName().replaceAll(" " , "_");
        int stagesNum = stages.size();

        Array<TextureRegion> plantStageRegions = new Array<>(cropsAtlas.findRegions( name+ "_Stage"));
        if (!isAlive) {
            return new TextureRegion(new Texture(Gdx.files.internal("crops/dead_plant.png")));
        }
        if (hasCrop) return plantStageRegions.get(stagesNum);
        else if (oneTimeHarvest && currentStage == stagesNum - 1) return plantStageRegions.get(stagesNum + 1);
        else if (currentStage < stagesNum) {
            return plantStageRegions.get(currentStage);
        }

        else return null;
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


    public void resetSeedAndCrop() {
        if (seed == null) {
            seed = (Seed) ItemManager.getItemByName(seedName);
        }
        if (product == null) {
            product = (Crop) ItemManager.getItemByName(cropName);
        }
    }
}