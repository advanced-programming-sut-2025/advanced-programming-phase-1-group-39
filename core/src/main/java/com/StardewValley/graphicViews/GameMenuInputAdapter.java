package com.StardewValley.graphicViews;

import com.StardewValley.controllers.GameController;
import com.StardewValley.models.App;
import com.StardewValley.models.Game;
import com.badlogic.gdx.InputAdapter;

public class GameMenuInputAdapter extends InputAdapter {
    private final Game game;
    private final GameController controller;

    public GameMenuInputAdapter(GameController controller, Game game) {
        this.controller = controller;
        this.game = game;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        int current = game.getPlayerInTurn().getSelectedSlot();
        int size = game.getPlayerInTurn().getMaxInventorySize();
        int next = (current + (amountY > 0 ? 1 : -1) + size) % size;
        game.getPlayerInTurn().setSelectedSlot(next);
        return true;
    }
}