package models.cropsAndFarming;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.map.Tile;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TreeManager {
    private static final HashMap<String, TreeData> trees = new HashMap<>();

    public static void loadTrees(String pathToJson) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<TreeData>>(){}.getType();
            ArrayList<TreeData> treeList = gson.fromJson(reader, listType);

            for (TreeData treeData : treeList) {
                trees.put(treeData.source.getName(), treeData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Tree getTreeBySeedName(String seedName, Tile tile) {
        TreeData data = trees.get(seedName);
        if (data == null) {
            return null;
        }

        Tree tree = new Tree(data.name, tile, data.source, data.stages, data.totalHarvestTime, data.fruitName, data.fruitHarvestCycle,
                data.fruitBaseSellPrice, data.isFruitEdible, data.fruitEnergy, data.fruitHealth, data.seasons);

        return tree;
    }

    public static String getTreeInfo(String seedName) {
        TreeData treeData = trees.get(seedName);
        if (treeData == null) {
            System.err.println("Warning: Tree with seed: " + seedName + " not found!");
            return null;
        }
        return treeData.toString();
    }
}
