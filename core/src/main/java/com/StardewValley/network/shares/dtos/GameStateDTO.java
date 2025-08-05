package com.StardewValley.network.shares.dtos;

import java.io.Serializable;
import java.util.Map;
import com.StardewValley.models.Time;

public class GameStateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // نقشه وضعیت تمام بازیکنان
    private final Map<String, PlayerStateDTO> playerStates;
    private final Time gameTime; // وضعیت زمان بازی
    // می‌توانید وضعیت آب و هوا و ... را هم اضافه کنید

    public GameStateDTO(Map<String, PlayerStateDTO> playerStates, Time gameTime) {
        this.playerStates = playerStates;
        this.gameTime = gameTime;
    }

    // Getter ها
    public Map<String, PlayerStateDTO> getPlayerStates() { return playerStates; }
    public Time getGameTime() { return gameTime; }
}