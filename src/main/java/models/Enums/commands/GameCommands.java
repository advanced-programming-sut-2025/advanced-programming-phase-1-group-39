package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameCommands {
    SHOW_CURRENT_MENU("show current menu"),
    EXIT_GAME("menu exit"),
    EXIT_APP("exit"),
    NEXT_TURN("next\\s+turn"),

    TIME("time"),
    DATE("date"),
    DATE_AND_TIME("datetime"),
    DAY_OF_WEEK("day\\s+of\\s+the\\s+week"),
    CHEAT_ADVANCE_TIME("cheat\\s+advance\\s+time\\s+(?<h>-?\\d+)h"),
    CHEAT_ADVANCE_DATE("cheat\\s+advance\\s+date\\s+(?<d>-?\\d+)d"),

    SHOW_SEASON("season"),

    PRINT_MAP("print\\s+map\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)\\s+-s\\s+(?<size>\\d+)"),
    HELP_READING_MAP("help\\s+reading\\s+map"),

    WALK("walk\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)"),

    SHOW_ENERGY("energy\\s+show"),
    CHEAT_SET_ENERGY("energy\\s+set\\s+-v\\s+(?<value>\\d+)"),
    CHEAT_ENERGY_UNLIMITED("energy\\s+unlimited"),

    WEATHER("weather"),
    WEATHER_FORECAST("weather\\s+forecast"),
    CHEAT_WEATHER_SET("cheat\\s+weather\\s+set\\s+(?<type>\\S+)"),
    CHEAT_THOR("cheat\\s+Thor\\s+-l\\s+(?<x>\\d+),(?<y>\\d+)"),

    BUILD_GREENHOUSE("greenhouse\\s+build"),

    INVENTORY_SHOW("inventory\\s+show"),
    INVENTORY_TRASH("inventory\\s+trash\\s+-i\\s+(?<itemName>.+)(\\s+-n\\s+(?<number>\\d+)?)"),

    // Tools
    TOOLS_EQUIP("tools\\s+equip\\s+(?<toolName>.+)"),
    TOOL_SHOW_CURRENT("tools\\s+show\\s+current"),
    TOOL_SHOW_AVAILABLE("tools\\s+show\\s+available"),

    TOOLS_UPGRADE("tools\\s+upgrade\\s+(?<name>\\S+)"),
    TOOL_USE("tools\\s+use\\s+-d\\s+(?<direction>\\S+)"),

    HOWMUCH_WATER("howmuch\\s+water"),
    GO_FISHING("fishing\\s+-p\\s+(?<pole>.+)"),

    SELL_PRODUCTS("sell\\s+(?<product>.+)\\s+-n\\s+(?<count>\\d+)"),

    // Craft
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

    BUILD("build\\s+-a\\s+(.+?)\\s+-l\\s+(\\d+)\\s+(\\d+)"),
    BUY_ANIMAL("buy\\s+animal\\s+-a\\s+(.+?)\\s+-n\\s+(.+?)"),
    PET("pet\\s+-n\\s+(.+?)"),
    CHEAT_FRIENDSHIP_ANIMAL("cheat\\s+set\\s+friendship\\s+-n\\s+(.+?)\\s+-c\\s+(\\d+)"),
    SHOW_ANIMALS("animals"),
    SHEPHERD_ANIMALS("shepherd\\s+animals\\s+-n\\s+(.+?)\\s+-l\\s+(\\d+)\\s+(\\d+)"),
    FEED_ANIMAL("feed\\s+hay\\s+-n\\s+(.+?)"),
    SHOW_ANIMAL_PRODUCTS("show\\s+animal\\s+products"),
    COLLECT_PRODUCE("collect\\s+produce\\s+-n\\s+(.+?)"),

    ARTISAN_USE("artisan\\s+use\\s+\"(.*?)\"(?:\\s+\"(.*?)\")+"),
    ARTISAN_GET("artisan\\s+get\\s+(.+?)"),
    PURCHASE("purchase\\s+(.+?)\\s+-n\\s+(\\d+)"),

    SHOW_ALL_PRODUCTS("show\\s+all\\s+products"),
    SHOW_AVAILABLE_PRODUCTS("show\\s+all\\s+available\\s+products"),
    CHEAT_ADD_MONEY("cheat\\s+add\\s+(\\d+)\\s+dollars"),

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