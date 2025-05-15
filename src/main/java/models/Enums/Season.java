package models.Enums;

public enum Season {
    SPRING (0),
    SUMMER (1),
    FALL (2),
    WINTER (3);

    public int number;

    Season(int number) {
        this.number = number;
    }

    public static Season getSeasonByNumber(int number) {
        for (Season season : Season.values()) {
            if (season.number == number) {
                return season;
            }
        }
        return null;
    }
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
