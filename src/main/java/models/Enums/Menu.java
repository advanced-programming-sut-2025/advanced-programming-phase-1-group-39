package models.Enums;

import views.*;

import java.util.Scanner;

public enum Menu {
    SIGNUP_MENU("signup menu", new SignupMenuView()),
    LOGIN_MENU("login menu", new LoginMenuView()),
    MAIN_MENU("main menu", new MainMenuView()),
    PROFILE_MENU("profile menu", new ProfileMenuView()),
    GAME_MENU("game menu", new GameMenuView()),
    GAME("game", new GameView()),
    ExitMenu("exit", new ExitMenuView());

    private View menuView;
    private String name;

    Menu(String name, View menuView) {
        this.name = name;
        this.menuView = menuView;
    }

    public void checkInput(String input) {
        menuView.checkCommand(input);
    }

    public View getMenuView() {
        return menuView;
    }

    public String getName() {
        return name;
    }

    public void checkCommand(String command) { this.menuView.checkCommand(command); }
}
