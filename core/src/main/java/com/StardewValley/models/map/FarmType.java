package com.StardewValley.models.map;

import java.io.Serializable;

public enum FarmType implements Serializable {
    LAKE_FARM, MINE_FARM;

    private static final long serialVersionUID = 1L;
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