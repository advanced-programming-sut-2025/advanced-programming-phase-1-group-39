package com.StardewValley.models.services;

import models.Enums.Menu;
import models.Game;
import models.User;

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
