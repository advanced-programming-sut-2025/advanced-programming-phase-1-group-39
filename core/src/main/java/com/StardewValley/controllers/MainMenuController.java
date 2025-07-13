package com.StardewValley.controllers;


import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Result;

public class MainMenuController {

    public Result logout() {
        App.getApp().setLoggedInUser(null);
        App.getApp().setStayLoggedIn(false);
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
        return new Result(true, "You are now in the main menu.");
    }

    public Result exit() {
        App.getApp().setLoggedInUser(null);
        App.getApp().setStayLoggedIn(false);
        App.getApp().setCurrentMenu(Menu.SIGNUP_MENU);
        return new Result(true, "You have been logged out of your account. You are now in the signup menu.");
    }

    public void bigExit() {
        App.getApp().setCurrentMenu(Menu.ExitMenu);
    }



}
