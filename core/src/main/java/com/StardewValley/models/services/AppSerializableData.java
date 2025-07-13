package com.StardewValley.models.services;


import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Game;
import com.StardewValley.models.User;

public class AppSerializableData {
    public String randomPassword;
    public User pendingUser;
    public boolean isRegisterSuccessful;
    public boolean stayLoggedIn;
    public User loggedInUser;
    public int lastGameId;
    public Menu currentMenu;
    public Game currentGame;
}
