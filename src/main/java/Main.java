import models.map.FarmType;
import models.map.Map;

public class Main {
    public static void main(String[] args) {
        Map map = new Map();
        map.readFarmMapData(FarmType.LAKE_FARM);
    }
}