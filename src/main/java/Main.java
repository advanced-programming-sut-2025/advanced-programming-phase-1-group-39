import models.map.FarmType;
import models.map.Map;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        map.addRandomFarm(FarmType.LAKE_FARM, 2);
        map.addRandomFarm(FarmType.MINE_FARM, 1);
        System.out.println(map.printWholeMap());
        System.out.println(map.printCharMapBySize(0, 5, 10));
    }
}