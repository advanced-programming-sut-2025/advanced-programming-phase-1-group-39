package com.StardewValley.network.shares;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_PLAYERS = 4;

    private final String id;
    private final String lobbyName;
    // <<-- برای thread-safety بهتر، لیست را final می‌کنیم --
    private final List<String> players;
    private final String admin;
    private boolean gameStarted;

    public Lobby(String lobbyName, String admin) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.lobbyName = lobbyName;
        this.admin = admin;
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.players.add(admin);
        this.gameStarted = false;
    }

    // Getter ها
    public String getId() { return id; }
    public String getLobbyName() { return lobbyName; }
    public List<String> getPlayers() { return new ArrayList<>(players); }
    public String getAdmin() { return admin; }
    public int getPlayerCount() { return players.size(); } // متد کمکی
    public boolean isGameStarted() {
        return gameStarted;
    }
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public boolean addPlayer(String playerName) {
        synchronized (players) { // اطمینان از اتمیک بودن عملیات روی لیست
            if (!isFull() && !players.contains(playerName)) {
                players.add(playerName);
                return true;
            }
            return false;
        }
    }

    @Override
    public String toString() {
        // از getPlayerCount() استفاده می‌کنیم که مستقیما سایز لیست همگام‌شده را می‌خواند
        String status = gameStarted ? "[In Game]" : "[Waiting]";
        return status + " ID: " + id + " | Name: " + lobbyName + " | Players: " + getPlayerCount() + "/" + MAX_PLAYERS;
    }
}