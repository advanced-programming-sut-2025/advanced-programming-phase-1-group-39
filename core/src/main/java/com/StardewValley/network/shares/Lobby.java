package com.StardewValley.network.shares;

import com.StardewValley.models.map.FarmType;
import com.StardewValley.network.shares.message.LobbyData;

import java.io.Serializable;
import java.util.*;

public class Lobby implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_PLAYERS = 4;

    private final String id;
    private final String lobbyName;
    private boolean isPrivate;
    private boolean isVisibleToAll;

    // <<-- برای thread-safety بهتر، لیست را final می‌کنیم --
    private final List<String> players; //  !! Usernames
    private final Map<String, FarmType> playersMap = new HashMap<>();
    private final String admin;
    private boolean gameStarted;

    public Lobby(String lobbyName, String admin, boolean isPrivate, boolean isVisibleToAll) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.lobbyName = lobbyName;
        this.admin = admin;
        this.players = Collections.synchronizedList(new ArrayList<>());
        this.players.add(admin);
        this.gameStarted = false;

        this.isPrivate = isPrivate;
        this.isVisibleToAll = isVisibleToAll;
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
        synchronized (players) {
            if (!isFull() && !players.contains(playerName)) {
                players.add(playerName);
                playersMap.put(playerName, FarmType.MINE_FARM);
                return true;
            }
            return false;
        }
    }

    public void setMapOfPlayer(String player, FarmType farmType) {
        if (players.contains(player)) {
            playersMap.put(player, farmType);
        }
    }


    @Override
    public String toString() {
        // از getPlayerCount() استفاده می‌کنیم که مستقیما سایز لیست همگام‌شده را می‌خواند
        String status = gameStarted ? "[In Game]" : "[Waiting]";
        return status + " ID: " + id + " | Name: " + lobbyName + " | Players: " + getPlayerCount() + "/" + MAX_PLAYERS;
    }
}