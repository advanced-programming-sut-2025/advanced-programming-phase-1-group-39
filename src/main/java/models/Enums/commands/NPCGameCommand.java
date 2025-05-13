package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NPCGameCommand {

    MeetNPC("meet\\s+NPC\\s+(?<NPCName>.*?)"),
    GifNPC("gift\\s+NPC\\s+(?<NPCName>.*?)\\s+-i\\s+(?<item>.*?)"),



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
