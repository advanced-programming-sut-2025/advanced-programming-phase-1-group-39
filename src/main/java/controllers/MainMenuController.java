package controllers;

import models.App;
import models.Enums.Menu;
import models.Result;

import java.util.regex.Matcher;

public class MainMenuController {

    public Result logout() {
        App.getApp().setLoggedInUser(null);
        App.getApp().setCurrentMenu(Menu.SIGNUP_MENU);
        return new Result(true, "You have been logged out of your account. You are now in the signup menu.");
    }

    public Result goProfileMenu() {
        App.getApp().setCurrentMenu(Menu.PROFILE_MENU);
        return new Result(true, "You are now in the profile menu.");
    }

    public Result goGameMenu() {
        App.getApp().setCurrentMenu(Menu.GAME_MENU);
        return new Result(true, "You are now in the game menu.");
    }

    public Result showCurrentMenu() {
        return new Result(true, "You are now in the current menu.");
    }

}
