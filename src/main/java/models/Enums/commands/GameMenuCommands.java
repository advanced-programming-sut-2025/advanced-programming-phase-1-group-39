package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {

    NewGame("game\\s+new\\s+-u\\s+(?<username1>\\S+)((\\s+(?<username2>\\S+))?)((\\s+(?<username3>\\S+))?)((\\s+(?<otherUsers>\\S+))?)"),

    ChoseMap("game\\s+map\\s+(?<mapNumber>\\S+)"),

    LoadGame("load\\s+game"),

    GoMainMenu("menu\\s+enter\\s+main"),

    ShowCurrentMenu("show\\s+current\\s+menu")

    ;

    private final String pattern;

    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }
}
