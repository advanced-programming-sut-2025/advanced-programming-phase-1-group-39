package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum InteractionsCommand {


    ShowFriendshipList("friendships"),

    Talk("talk\\s+-u\\s+(?<username>.*?)\\s+-m\\s+(?<message>.*?)"),
    TalkHistory("talk\\s+history\\s+-u\\s+(?<username>.*?)"),

    Hug("hug\\s+-u\\s+(?<username>.*?)"),

    Gift("gift\\s+-u\\s+(?<username>.*?)\\s+-i\\s+(?<item>.*?)\\s+-a\\s+(?<amount>\\d+)"),
    GiftList("gift\\s+list"),
    GiftRate("gift\\s+rate\\s+")


    // gift history -u <username>
    // gift rate -i <gift-number> -r <rate>
    // gift -u <username> -i <item> -a <amount>
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
