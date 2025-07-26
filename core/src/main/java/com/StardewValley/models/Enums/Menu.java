package com.StardewValley.models.Enums;

import com.StardewValley.graphicViews.*;
import com.StardewValley.views.*;
import com.StardewValley.views.LoginMenuView;
import com.StardewValley.views.MainMenuView;
import com.StardewValley.views.ProfileMenuView;
import com.badlogic.gdx.Screen;

public enum Menu {
    SIGNUP_MENU("signup menu", new SignupMenuView()),
    SECURITY_QUESTION_MENU("securityQuestion menu", new SignupMenuView()),
    LOGIN_MENU("login menu", new LoginMenuView()),
    FORGET_PASSWORD_MENU("forget password", new LoginMenuView()),
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

    public Screen getScreen() {
        return switch (this) {
            case SIGNUP_MENU -> new SignupMenuScreen();
            case LOGIN_MENU -> new LoginMenuScreen();
            case MAIN_MENU -> new MainMenuScreen();
            case GAME_MENU -> new GameScreen();
            default -> null;
        };
    }
}
