package models;

import java.util.Scanner;

public class Input {
    static Scanner scanner = new Scanner(System.in);

    public static String getNextLine() {
        return scanner.nextLine().trim();
    }
}
