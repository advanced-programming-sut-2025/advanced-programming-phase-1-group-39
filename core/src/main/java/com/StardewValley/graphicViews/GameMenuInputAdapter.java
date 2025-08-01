package com.StardewValley.graphicViews;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameMenuInputAdapter extends InputAdapter {
    private GameGuiController controller;
    private GameScreen screen;

    public GameMenuInputAdapter(GameGuiController controller, GameScreen screen) {
        this.controller = controller;
        this.screen = screen;
    }



    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.showExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            controller.changeTurn();
        }
        return true;
    }
}