package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameCommands {
    PRINT_MAP("print\\s+map\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)\\s+-s\\s+(?<size>\\d+)"),
    HELP_READING_MAP("help\\s+reading\\s+map"),

    SHOW_CRAFT_INFO("craft\\s+info\\s+-n\\s+(.+?)"),
    SHOW_TREE_INFO("tree\\s+info\\s+-n\\s+(.+?)"),
    SHOW_PLANT("show\\s+plant\\s+-l\\s+(\\d+)\\s+(\\d+)"),
    SHOW_TREE("show\\s+tree\\s+-l\\s+(\\d+)\\s+(\\d+)"),
    FERTILIZE("fertilize\\s+-f\\s+(.+?)\\s+-d\\+s(.+?)"),

    PLANT("plant\\s+-s\\s+(.+?)\\s+-d\\s+(.+?)"),

    SHOW_CRAFTING_RECIPES("show\\s+crafting\\s+recipes"),
    CRAFT("crafting\\s+craft\\s+(.+?)"),
    CHEAT_ADD_ITEM("cheat\\s+add\\s+item\\s+-n\\s+(.+?)\\s+-c\\s+(\\d+)"),

    COOKING_REFRIGERATOR("cooking\\s+refrigerator\\s+(put|pick)\\s+(.+?)"),
    COOK("cooking\\s+prepare\\s+(.+?)"),
    EAT("eat\\s+(.+?)"),

    SHOW_FOOD_RECIPES("show\\s+food\\s+recipes"),

    PET("pet\\s+-n\\s+(.+?)"),
    CHEAT_FRIENDSHIP_ANIMAL("cheat\\s+set\\s+friendship\\s+-n\\s+(.+?)\\s+-c\\s+(\\d+)"),
    SHOW_ANIMALS("animals"),
    SHEPHERD_ANIMALS("shepherd\\s+animals\\s+-n\\s+(.+?)\\s+-l\\s+(\\d+)\\s+(\\d+)"),
    FEED_ANIMAL("feed\\s+hay\\s+-n\\s+(.+?)"),
    SHOW_ANIMAL_PRODUCTS("show\\s+animal\\s+products"),
    COLLECT_PRODUCE("collect\\s+produce\\s+-n\\s+(.+?)"),
    FISHING("fishing\\s+-p\\s+(.+?)"),

    ARTISAN_USE("artisan\\s+use\\s+\"(.*?)\"(?:\\s+\"(.*?)\")+"),
    ARTISAN_GET("artisan\\s+get\\s+(.+?)"),

    START_TRADE("start\\s+trade"),
    TRADE("trade\\s+-u\\s+(.+?)\\s+-t\\s+(request|offer)\\s+-i\\s+(.+?)\\s+-a\\s+(\\d+)(?:\\s+-p\\s+(?<money>\\d+)|\\s+-ti\\s+(?<item>.+?)\\s+-ta\\s+(\\d+))"),
    SHOW_TRADES_LIST("trade\\s+list"),
    TRADE_RESPONSE("trade\\s+response\\s+(–accept|–reject)\\s+-i\\s+(\\d+)"),
    TRADE_HISTORY("trade\\s+history");

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