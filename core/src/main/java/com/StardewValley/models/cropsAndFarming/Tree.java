package com.StardewValley.models.cropsAndFarming;

import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.ItemManager;
import com.StardewValley.models.ItemStack;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Arrays;

public class Tree{
    private String name;

    private String seedName;
    private transient Seed source;

    private ArrayList<Integer> stages = new ArrayList<>();
    private int currentStage;
    private int lastStage;
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
        this.seedName = source.getName();

        this.stages = stages;
        this.currentStage = 0;
        this.lastStage = 0;
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

    public Tree() {}

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
            currentStage = stages.size() - 1;
            return new ItemStack(fruit, fruitStack);
        }
        return null;
    }
    public void updateDaily() {
        if (isBurnt) return;

        if (currentStage < stages.size() - 1) {
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
                currentStage = stages.size();
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

    public int getCurrentStage() {
        return currentStage;
    }
    public boolean stageChanged() {
        return currentStage != lastStage;
    }
    public void syncLastStage() {
        lastStage = currentStage;
    }

    public boolean hasFruit() {
        return hasFruit;
    }

    // Graphics
    public TextureRegion getFruitTexture() {
        TextureAtlas treesAtlas = GameAssetManager.treesAtlas;
        String fruitPath = fruitName.replaceAll(" ", "_");

        return new TextureRegion(treesAtlas.findRegion(fruitPath));
    }

    public TextureRegion getTexture() {
        Season season = App.getApp().getCurrentGame().getTime().getSeason();
        TextureAtlas treesAtlas = GameAssetManager.treesAtlas;

        String name = this.name.split(" ")[0];
        int stagesNum = stages.size();

        Array<TextureRegion> treeStageRegions = new Array<>(treesAtlas.findRegions( name+ "_Stage"));
        if (currentStage < stagesNum) {
            return new TextureRegion(treeStageRegions.get(currentStage));
        } else {
            if (hasFruit) {
                TextureRegion hasFruitTexture = treesAtlas.findRegion(name + "_Stage_5_Fruit");
                if (hasFruitTexture != null) {
                    return new TextureRegion(hasFruitTexture);
                }
            }
            if (treeStageRegions.size == stagesNum + 1) {
                TextureRegion seasonsTextureRegion = treeStageRegions.get(stagesNum);
                TextureRegion[][] seasonedTrees = seasonsTextureRegion.split(seasonsTextureRegion.getRegionWidth()/4, seasonsTextureRegion.getRegionHeight());
                return seasonedTrees[0][season.number];
            } else {
                return new TextureRegion(treesAtlas.findRegion(name + "_Stage_5_not4Season"));
            }
        }
    }

    public void resetSeed() {
        if (source == null) {
            source = (Seed) ItemManager.getItemByName(seedName);
        }
    }
}