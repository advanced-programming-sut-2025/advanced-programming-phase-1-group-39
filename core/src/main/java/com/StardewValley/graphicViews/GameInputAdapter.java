package com.StardewValley.graphicViews;

import com.StardewValley.controllers.GameController;
import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.Enums.Direction;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameInputAdapter extends InputAdapter {

    private final float moveCooldown = 0.1f;
    private float moveTimer = 0f;
    private final GameScreen screen;
    private final GameController controller;

    public GameInputAdapter(GameScreen screen, GameController controller) {
        this.screen = screen;
        this.controller = controller;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    public void update(float delta) {
        moveTimer += delta;
        float cameraSpeed = 1500f;

        if (moveTimer < moveCooldown) return;

        Direction direction = Direction.NONE;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
            direction = Direction.UP;
        else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
            direction = Direction.DOWN;
        else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            direction = Direction.LEFT;
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            direction = Direction.RIGHT;

        if (direction != Direction.NONE) {
            boolean moved = screen.tryMovePlayer(direction.dx, direction.dy);
            if (moved)
                moveTimer = 0f;
        }
    }
}
