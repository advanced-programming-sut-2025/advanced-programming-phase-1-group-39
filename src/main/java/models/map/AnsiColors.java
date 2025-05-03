package models.map;

public class AnsiColors {
    // Reset
    public static final String ANSI_RESET = "\u001b[0m";

    // Regular Colors (Text)
    public static final String ANSI_BLACK = "\u001b[30m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_YELLOW = "\u001b[33m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE = "\u001b[37m";

    // Bold Colors (Text)
    public static final String ANSI_BLACK_BOLD = "\u001b[1;30m";
    public static final String ANSI_DARK_GRAY_BOLD = getAnsiBoldColor(64, 64, 64);
    public static final String ANSI_PURPLE_BOLD = getAnsiBoldColor(73, 0, 146);
    public static final String ANSI_GREEN_BOLD = getAnsiBoldColor(0, 156, 0);
    public static final String ANSI_BROWN_BOLD = getAnsiBoldColor(102, 41, 0);
    // Background Colors
    public static final String ANSI_BLACK_BACKGROUND = getAnsiBackGround(49, 43, 43);
    public static final String ANSI_GRAY_BACKGROUND = getAnsiBackGround(104, 104, 103);
    public static final String ANSI_BLUE_BACKGROUND = getAnsiBackGround(0, 172, 252);
    public static final String ANSI_DARK_GREEN_BACKGROUND = getAnsiBackGround(0, 95, 0);
    public static final String ANSI_ORANGE_BACKGROUND = getAnsiBackGround(200, 79, 0);
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    // Other colors
    public static final String ANSI_GOLDEN_BACKGROUND = getAnsiBackGround(155, 116, 0);
    public static final String ANSI_LIGHT_GOLDEN_BACKGROUND = getAnsiBackGround(197, 166, 72);
    public static final String ANSI_YELLOW_BACKGROUND = getAnsiBackGround(255, 215, 0);


    public static String getAnsiBackGround(int r, int g, int b) {
        return "\u001B[48;2;"+r+";"+g+";"+b+"m";
    }
    public static String getAnsiBoldColor(int r, int g, int b) {
        return "\u001B[1m\u001B[38;2;"+r+";"+g+";"+b+"m";
    }
}
