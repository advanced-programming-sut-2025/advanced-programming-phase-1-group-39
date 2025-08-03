package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.App;
import com.StardewValley.models.Game;
import com.StardewValley.models.Result;
import com.StardewValley.models.cooking.FoodManager;
import com.StardewValley.models.cooking.FoodRecipe;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GameGuiController {

    public void handleButtonDisable(Game game, TextButton button) {
        if (!game.getPlayerInTurn().equals(game.getMainPlayer())) {
            button.setDisabled(true);
            button.setColor(Color.GRAY);
            button.setVisible(false);
        }
    }

    public void changeTurn() {
        Game game = App.getApp().getCurrentGame();
        game.nextTurn();
    }

    public void cook(FoodRecipe recipe) {
        Result result = FoodManager.cook(recipe.name(), App.getApp().getCurrentGame().getPlayerInTurn());
        System.out.println(result.message());
    }
}
