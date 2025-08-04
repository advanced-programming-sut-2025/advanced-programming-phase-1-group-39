package com.StardewValley.graphicViews;

import com.StardewValley.models.App;
import com.StardewValley.models.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameInputAdapter extends InputAdapter {
    private GameGuiController controller;
    private GameScreen screen;

    public GameInputAdapter(GameGuiController controller, GameScreen screen) {
        this.controller = controller;
        this.screen = screen;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.showExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            controller.changeTurn();
        } else if (keycode == Input.Keys.N) {
            Game game = App.getApp().getCurrentGame();
            game.getMap().growWateredPlantsAndTrees();
        } else if (keycode == Input.Keys.C) {
            screen.changeCookingMenu();
        } else if (keycode == Input.Keys.B) {
            screen.changeCraftingMenu();
        } else if (keycode == Input.Keys.P) {
            screen.showShopMenu();
        }
        return true;
    }


}