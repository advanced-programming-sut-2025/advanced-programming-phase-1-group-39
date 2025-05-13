package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameCommands {
    PRINT_MAP("print\\s+map\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)\\s+-s\\s+(?<size>\\d+)"),
    HELP_READING_MAP("help\\s+reading\\s+map"),

    SHOW_CRAFTING_RECIPES("show\\s+crafting\\s+recipes"),
    CRAFT("crafting\\s+craft\\s+(.+?)"),
    CHEAT_ADD_ITEM("cheat\\s+add\\s+item\\s+-n\\s+(.+?)\\s+-c\\s+(\\d+)"),

    SHOW_FOOD_RECIPES("show\\s+food\\s+recipes"),

    PET("pet\\s+-n\\s+(.+?)"),
    CHEAT_FRIENDSHIP_ANIMAL("cheat\\s+set\\s+friendship\\s+-n\\s+(.+?)\\s+-c\\s+(\\d+)"),
    SHOW_ANIMALS("animals"),
    SHEPHERD_ANIMALS("shepherd\\s+animals\\s+-n\\s+(.+?)\\s+-l\\s+(\\d+)\\s+(\\d+)"),
    FEED_ANIMAL("feed\\s+hay\\s+-n\\s+(.+?)"),
    SHOW_ANIMAL_PRODUCTS("show\\s+animal\\s+products"),
    COLLECT_PRODUCE("collect\\s+produce\\s+-n\\s+(.+?)"),
    FISHING("fishing -p (.+?)"),

    ARTISAN_USE("artisan\\s+use\\s+\"(.*?)\"(?:\\s+\"(.*?)\")+"),
    ARTISAN_GET("artisan\\s+get\\s+(.+?)");

    private final String pattern;

    GameCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(pattern).matcher(input.trim());
        if (matcher.matches()) return matcher;
        return null;
    }
}