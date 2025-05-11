package models.Enums;

import java.util.Objects;

public enum Season {
    SPRING,
    SUMMER,
    FALL,
    WINTER;

    public static Season getSeason(String name) {
        if (name.equalsIgnoreCase("spring")) {
            return SPRING;
        } else if (name.equalsIgnoreCase("summer")) {
            return SUMMER;
        } else if (name.equalsIgnoreCase("fall")) {
            return FALL;
        } else if (name.equalsIgnoreCase("winter")) {
            return WINTER;
        }

        return null;
    }
}
