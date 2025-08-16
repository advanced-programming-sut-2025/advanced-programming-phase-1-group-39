package com.StardewValley.models;

import com.StardewValley.models.services.AppDataManager;

import java.io.Serializable;

public class GameMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    public int gameId;
    public String description;

    public GameMetadata(Game game) {
        gameId = game.getId();
        resetDescription(game);
    }

    public String getFilePath() {
        return AppDataManager.getGamePath(gameId);
    }

    public void resetDescription(Game game) {
        Time time = game.getTime();
        description = time.getDateDetail() + ", " + time.getDayDetail();
    }
}
