import models.Enums.Season;
import models.ItemManager;
import models.ItemStack;
import models.cropsAndFarming.ForagingManager;
import models.cropsAndFarming.TreeManager;
import models.map.FarmType;
import models.map.Map;
import models.map.Tile;
import views.AppView;

public class Main {
//    public static void main(String[] args) {
//        (new AppView()).run();
//    }

    public static void main(String[] args) {
        Map map = new Map();
        ItemManager.loadItems();
        map.addRandomFarm(FarmType.LAKE_FARM, 2);
        map.addRandomFarm(FarmType.MINE_FARM, 1);

        System.out.println(map.printColorMap());
    }
}