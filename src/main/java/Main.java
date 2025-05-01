import models.map.FarmType;
import models.map.Map;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        map.addRandomFarm(FarmType.LAKE_FARM, 2);
        System.out.println(map.printWholeMap());
    }
}