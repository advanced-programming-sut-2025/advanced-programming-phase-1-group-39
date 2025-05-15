package models.map;

public enum FarmType {
    LAKE_FARM, MINE_FARM;

    int id;
    FarmType() {
        this.id = ordinal() + 1;
    }

    public static FarmType getFarmTypeById(int id) {
        for (FarmType farmType : FarmType.values()) {
            if (farmType.id == id) {
                return farmType;
            }
        }
        return null;
    }
}