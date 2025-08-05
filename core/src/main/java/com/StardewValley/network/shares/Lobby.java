package com.StardewValley.network.shares;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String lobbyName;
    private final List<String> players; // نام بازیکنان
    private String admin; // نام ادمین

    public Lobby(String lobbyName, String admin) {
        this.id = UUID.randomUUID().toString(); // یک ID یونیک برای هر لابی
        this.lobbyName = lobbyName;
        this.admin = admin;
        this.players = new ArrayList<>();
        this.players.add(admin);
    }

    // Getter ها را اینجا اضافه کنید
    public String getId() { return id; }
    public String getLobbyName() { return lobbyName; }
    public List<String> getPlayers() { return players; }
    public String getAdmin() { return admin; }

    @Override
    public String toString() {
        return "Lobby: " + lobbyName + " | Admin: " + admin + " | Players: " + players.size() + "/4";
    }
}