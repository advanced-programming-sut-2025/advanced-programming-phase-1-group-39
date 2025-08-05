package com.StardewValley.network.shares;

import com.StardewValley.models.Location;

import java.io.Serializable;
import java.util.Map;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    // یک نقشه که هر بازیکن را به وضعیتش (موقعیت و...) مرتبط می‌کند
    // Key: شناسه یکتای بازیکن (clientIdentifier)
    // Value: وضعیت آن بازیکن
    private final Map<String, Location> playerStates;

    // بعداً می‌توانید این موارد را هم اضافه کنید
    // private Time gameTime;
    // private Weather todayWeather;

    public GameState(Map<String, Location> playerStates) {
        this.playerStates = playerStates;
    }

    public Map<String, Location> getPlayerStates() {
        return playerStates;
    }
}