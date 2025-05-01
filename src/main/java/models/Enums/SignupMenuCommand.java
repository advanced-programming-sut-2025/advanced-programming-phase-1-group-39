package models.Enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignupMenuCommand {



    ShowCurrentManu("show\\s+current\\s+manu"),

    GoToLoginMenu("manu\\s+enter\\s+login"),

    ExitMenu("manu\\s+exit"),

    ;

    private final String pattern;

    SignupMenuCommand(String pattern) { this.pattern = pattern; }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

}
