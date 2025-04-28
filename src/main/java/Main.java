import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.TreeManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TreeManager.loadTrees("src/main/java/models/cropsAndFarming/trees.json");
        System.out.println(TreeManager.getTreeBySeedName("Cherry Sapling"));
    }
}