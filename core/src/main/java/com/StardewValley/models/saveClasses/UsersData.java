package com.StardewValley.models.saveClasses;

import java.util.ArrayList;

public class UsersData {
    public ArrayList<UserData> users;
    public UserData loggedInUser = null;

    public UsersData(ArrayList<UserData> users, UserData loggedInUser) {
        this.users = users;
        this.loggedInUser = loggedInUser;
    }
}
