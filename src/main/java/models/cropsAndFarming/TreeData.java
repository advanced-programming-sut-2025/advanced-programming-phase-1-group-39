package models.cropsAndFarming;

import models.Enums.Season;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeData {
    public String name;
    public Seed source;
    public ArrayList<Integer> stages;
    public int totalHarvestTime;
    public String fruitName;
    public int fruitHarvestCycle;
    public int fruitBaseSellPrice;
    public boolean isFruitEdible;
    public Integer fruitEnergy;
    public Integer fruitHealth;
    public Season[] seasons;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(name);
        str.append("\n");
        str.append(stages);
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
