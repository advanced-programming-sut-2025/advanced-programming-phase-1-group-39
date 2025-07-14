package com.StardewValley.graphicViews;

import com.StardewValley.controllers.GameController;
import com.badlogic.gdx.InputAdapter;

public class GameMenuInputAdapter extends InputAdapter {
    private GameController controller;

    public GameMenuInputAdapter(GameController controller) {
        this.controller = controller;
    }
}
