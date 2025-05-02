package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignupMenuCommand {


    Register("register\\s+-u\\s+(?<username>.*?)\\s+-p\\s+(?:(?<password>\\S+)\\s+(?<passwordConfirm>\\S+)|(?<random>random))\\s+-n\\s+(?<nickName>.*?)\\s+-e\\s+(?<email>.*?)\\s+-g\\s+(?<gender>\\S+)"),
    UserName("[a-zA-Z0-9-]+"),
    Password("[a-zA-Z0-9?<>,\"';:/|}\\{+)=*&^%$#!]+"),
    WeakPassword("(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[?<>,\"';:\\/|}\\{+\\)=*&^%$#!]).*"),
    Gender("(male|female)"),


    Email("(?=.{1,64}@)([a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._-]{0,62}[a-zA-Z0-9])@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9](?:\\.[a-zA-Z]{2,})+)"),

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
