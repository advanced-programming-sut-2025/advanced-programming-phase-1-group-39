import models.Enums.Season;
import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;
import models.map.Tile;

import java.io.IOException;

public class Main {
    public static void main(String[] args){
        TreeManager.loadTrees("src/main/java/models/cropsAndFarming/trees.json");
        ForagingManager.loadTreeSeeds("src/main/java/models/cropsAndFarming/ForagingTrees.json");
        ForagingManager.getRandomTree(Season.SPRING, new Tile(2, 3));
    }
}