package com.StardewValley.models.saveClasses;

import java.util.ArrayList;

public class UsersData {
    public ArrayList<UserData> users;
    public String loggedInUserName;

    public UsersData(ArrayList<UserData> users, String loggedInUserName) {
        this.users = users;
        this.loggedInUserName = loggedInUserName;
    }
}
