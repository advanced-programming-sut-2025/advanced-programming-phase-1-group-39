package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginMenuCommands {

    Login("login\\s+-u\\s+(?<username>.*?)\\s+-p\\s+(?<password>.*?)(\\s+(?<stayLoggedIn>-stay-logged-in))?"),

    ForgetPassword("forget\\s+password\\s+-u\\s+(?<username>.*?)"),
    Answer("answer\\s+-a\\s+(?<answer>.*?)"),
    NewPassword("(?<newPassword>.*?)"),

    GoTOSignupMenu("go\\s+to\\s+signup\\s+menu"),

    ShowCurrentMenu("show\\s+current\\s+menu"),

    ExitMenu("menu\\s+exit"),

    ;

    private final String pattern;

    LoginMenuCommands(String pattern) {
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
