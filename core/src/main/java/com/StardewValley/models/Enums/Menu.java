package com.StardewValley.models.Enums;

import com.StardewValley.graphicControllers.SignupMenuController;
import com.StardewValley.views.*;
import com.badlogic.gdx.Screen;

public enum Menu {
    SIGNUP_MENU("signup menu", new SignupMenuView(), new com.StardewValley.graphicViews.SignupMenuView()),
    LOGIN_MENU("login menu", new LoginMenuView(), new com.StardewValley.graphicViews.GameView()),
    MAIN_MENU("main menu", new MainMenuView(), new com.StardewValley.graphicViews.GameView()),
    PROFILE_MENU("profile menu", new ProfileMenuView(), new com.StardewValley.graphicViews.GameView()),
    GAME_MENU("game menu", new GameMenuView(), new com.StardewValley.graphicViews.GameView()),
    GAME("game", new GameView(), new com.StardewValley.graphicViews.GameView()),
    ExitMenu("exit", new ExitMenuView(), new com.StardewValley.graphicViews.GameView()),;

    private View menuView;
    private String name;
    private Screen screen;

    Menu(String name, View menuView, Screen screen) {
        this.name = name;
        this.menuView = menuView;
        this.screen = screen;
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
        return screen;
    }
}
