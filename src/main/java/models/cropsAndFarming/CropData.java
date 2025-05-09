package models.cropsAndFarming;

import models.Enums.Season;

import java.util.ArrayList;

public class CropData {
    public String cropName;
    public String seedName;
    public ArrayList<Integer> stages;
    public int totalHarvestTime;
    public boolean oneTimeHarvest;
    public int regrowthTime;
    public int baseSellPrice;
    public boolean isEdible;
    public int baseEnergy;
    public int baseHealth;
    public Season season;
    public boolean canBecomeGiant;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + cropName + "\n");
        sb.append("Suorce: " + seedName + "\n");
        sb.append("Stages: " + stages + "\n");
        sb.append("Total Harvest Time: " + totalHarvestTime + "\n");
        sb.append("One Time Harvest: " + oneTimeHarvest + "\n");
        sb.append("Regrowth Time: " + regrowthTime + "\n");
        sb.append("Base Sell Price: " + baseSellPrice + "\n");
        sb.append("isEdible: " + isEdible + "\n");
        sb.append("Base Energy: " + baseEnergy + "\n");
        sb.append("Base Health: " + baseHealth + "\n");
        sb.append("Season: " + season + "\n");
        sb.append("Can Become Giant: " + canBecomeGiant + "\n");
        return sb.toString();
    }
}
