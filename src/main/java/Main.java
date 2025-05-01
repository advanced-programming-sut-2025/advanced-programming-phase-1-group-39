import models.cropsAndFarming.CropManager;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ForagingManager.loadMinerals("src/main/java/models/cropsAndFarming/ForagingMinerals.json");
        ForagingManager.getRandomMineral();
    }
}