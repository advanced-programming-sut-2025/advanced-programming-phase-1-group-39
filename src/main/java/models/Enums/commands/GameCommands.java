package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameCommands {
    NEXT_TURN("next\\s+turn"),

    TIME("time"),
    DATE("date"),
    DATE_AND_TIME("datetime"),
    DAY_OF_WEEK("day\\s+of\\s+the\\s+week"),
    CHEAT_ADVANCE_TIME("cheat\\s+advance\\s+time\\s+(?<h>-?\\d+)h"),
    CHEAT_ADVANCE_DATE("cheat\\s+advance\\s+date\\s+(?<d>-?\\d+)d"),

    SHOW_SEASON("season"),

    PRINT_MAP("print\\s+map\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)\\s+-s\\s+(?<size>\\d+)"),
    HELP_READING_MAP("help\\s+reading\\s+map"),

    WALK("walk\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)"),

    SHOW_ENERGY("energy\\s+show"),
    CHEAT_SET_ENERGY("energy\\s+set\\s+-v\\s+(?<value>\\d+)"),
    CHEAT_ENERGY_UNLIMITED("energy\\s+unlimited"),

    WEATHER("weather"),
    WEATHER_FORECAST("weather\\s+forecast"),
    CHEAT_WEATHER_SET("cheat\\s+weather\\s+set\\s+(?<type>\\S+)"),
    CHEAT_THOR("cheat\\s+Thor\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)"),

    BUILD_GREENHOUSE("greenhouse\\s+build"),
    ;

    private final String pattern;

    GameCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(pattern).matcher(input.trim());
        if (matcher.matches()) return matcher;
        return null;
    }
}
