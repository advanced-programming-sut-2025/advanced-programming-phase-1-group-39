package com.StardewValley.models.Enums;

import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.graphicViews.PregameMenuScreen;
import com.StardewValley.views.*;
import com.badlogic.gdx.Screen;

public enum Menu {
    SIGNUP_MENU("signup menu", new SignupMenuView(), new com.StardewValley.graphicViews.SignupMenuView()),
    SECURITY_QUESTION_MENu("securityQuestion menu", new SignupMenuView(), new SecurityQuestionMenuView()),
    LOGIN_MENU("login menu", new LoginMenuView(), new com.StardewValley.graphicViews.LoginMenuView()),
    FORGET_PASSWORD_MENU("forget password", new LoginMenuView(), new ForgetPasswordMenuView()),
    MAIN_MENU("main menu", new MainMenuView(), new com.StardewValley.graphicViews.GameView()),
    PROFILE_MENU("profile menu", new ProfileMenuView(), new com.StardewValley.graphicViews.GameView()),
    GAME_MENU("game menu", new GameMenuView(), new PregameMenuScreen()),
    GAME("game", new GameView(), new GameScreen()),
    ExitMenu("exit", new ExitMenuView(), new GameScreen()),;

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
