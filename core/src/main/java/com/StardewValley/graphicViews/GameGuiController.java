package com.StardewValley.graphicViews;

import com.StardewValley.models.App;
import com.StardewValley.models.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class GameGuiController {

    public void handleButtonDisable(Game game, TextButton button) {
        if (!game.getPlayerInTurn().equals(game.getMainPlayer())) {
            button.setDisabled(true);
            button.setColor(Color.GRAY);
        } else {
            button.setDisabled(false);
            button.setColor(Color.WHITE);
        }
    }

    public void changeTurn() {
        Game game = App.getApp().getCurrentGame();
        game.nextTurn();
    }
}
