package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum InteractionsCommand {


    ShowFriendshipList("friendships"),

    Talk("talk\\s+-u\\s+(?<username>.*?)\\s+-m\\s+(?<message>.*?)"),
    TalkHistory("talk\\s+history\\s+-u\\s+(?<username>.*?)"),

    Hug("hug\\s+-u\\s+(?<username>.*?)"),

    // talk -u <username> -m <message>
    // talk history -u <username>
    // hug -u <username>

    ;

    private final String pattern;

    InteractionsCommand(String pattern) {
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
