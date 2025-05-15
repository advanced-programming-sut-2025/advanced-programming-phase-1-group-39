package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands {

    ChangeUsername("change\\s+username\\s+-u\\s+(?<username>.*?)"),
    ChangeNickname("change\\s+nickname\\s+-u\\s+(?<nickname>.*?)"),
    ChangeEmail("change\\s+email\\s+-e\\s+(?<email>.*?)"),
    ChangePassword("change\\s+password\\s+-p\\s+(?<password>.*?)\\s+-o\\s+(?<oldPassword>.*?)"),

    ShowUserInfo("user\\s+info"),

    GoToMainMenu("menu\\s+exit"),

    ShowCurrentMenu("menu\\s+current\\s+menu"),

    ;

    private final String pattern;

    ProfileMenuCommands(String pattern) {
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
