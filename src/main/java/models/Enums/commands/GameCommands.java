package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameCommands {
    PRINT_MAP("print\\s+map\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)\\s+-s\\s+<size>");

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
