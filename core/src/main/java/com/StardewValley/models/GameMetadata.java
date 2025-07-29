package com.StardewValley.models;

import com.StardewValley.models.services.AppDataManager;

public class GameMetadata {
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
