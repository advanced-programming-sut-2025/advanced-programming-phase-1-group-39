package models.cropsAndFarming;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class TreeManager {
    private static final HashMap<String, Tree> trees = new HashMap<>();

    public static void loadTrees(String pathToJson) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(pathToJson)) {
            Type listType = new TypeToken<ArrayList<Tree>>(){}.getType();
            ArrayList<Tree> treeList = gson.fromJson(reader, listType);

            for (Tree tree : treeList) {
                if (tree.getSource() != null && tree.getSource().getName() != null) {
                    trees.put(tree.getSource().getName(), tree);
                } else {
                    System.err.println("Warning: Tree has no source seed! Skipping: " + tree.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Tree getTreeBySeedName(String seedName) {
        return trees.get(seedName);
    }

    public static String getTreeInfo(String seedName) {
        Tree tree = trees.get(seedName);
        if (tree == null) {
            System.err.println("Warning: Tree with seed: " + seedName + " not found!");
            return null;
        }
        return tree.toString();
    }
}
