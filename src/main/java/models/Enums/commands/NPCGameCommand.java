package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NPCGameCommand {

    MeetNPC("meet\\s+NPC\\s+(?<NPCName>.*?)"),
    GiveGiftToNPC("gift\\s+NPC\\s+(?<NPCName>.*?)\\s+-i\\s+(?<item>.*?)"),
    ShowFriendShipList("friendship\\s+NP\\s+list"),
    ShowQuestsList("quests\\s+list"),
    QuestsFinish("quests\\s+finish\\s+-i(?<index>\\d+)"),

    //quests finish -i <index>



    ;

    private final String pattern;

    NPCGameCommand(String pattern) {
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
