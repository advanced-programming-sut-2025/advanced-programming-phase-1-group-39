package com.StardewValley.network.shares.message;

public class LobbyData {
    public String lobbyName;
    public String adminUsername;
    public boolean isPrivate;
    public boolean isVisibleToAll;
    public String password;

    public LobbyData(String name,String adminUsername, boolean isPrivate, boolean isVisibleToAll, String password) {
        this.lobbyName = name;
        this.adminUsername = adminUsername;
        this.isPrivate = isPrivate;
        if (isPrivate)
            this.password = password;
        this.isVisibleToAll = isVisibleToAll;
    }
}
