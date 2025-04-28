package models.Enums;

import java.util.Objects;

public enum Season {
    SPRING,
    SUMMER,
    FALL,
    WINTER;

    public static Season getSeason(String name) {
        if (Objects.equals(name, "Spring")) {
            return SPRING;
        } else if (Objects.equals(name, "Summer")) {
            return SUMMER;
        } else if (Objects.equals(name, "Fall")) {
            return FALL;
        } else if (Objects.equals(name, "Winter")) {
            return WINTER;
        }

        return null;
    }
}
