package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands {

    Logout("user\\s+logout"),

    GoToProfileMenu("menu\\s+enter\\s+profile"),
    GoToGameMenu("menu\\s+enter\\s+game"),

    ShowCurrentMenu("show\\s+current\\s+menu"),

    ;

    private final String pattern;

    MainMenuCommands(String pattern) {
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
