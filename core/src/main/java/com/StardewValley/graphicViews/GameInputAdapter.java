package com.StardewValley.graphicViews;

import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Direction;
import com.StardewValley.models.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class GameInputAdapter extends InputAdapter {
    private GameGuiController controller;
    private GameScreen screen;

    public GameInputAdapter(GameGuiController controller, GameScreen screen) {
        this.controller = controller;
        this.screen = screen;
    }

    public void handlePlayerMovement(float delta, Game game) {
        float speed = game.getGameSetting().getPlayerSpeed();

        Player player = game.getPlayerInTurn();

        Direction currentDirection = Direction.NONE;

        Vector2 movement = new Vector2(0,0);
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.y += 1;
            currentDirection = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.y -= 1;
            currentDirection = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.x -= 1;
            currentDirection = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.x += 1;
            currentDirection = Direction.RIGHT;
        }
        // check can move to
        movement.nor().scl(speed * delta);
        if (movement.isZero()) {
            player.setDirection(Direction.NONE);
            return;
        }

        float boundingWidth = Constants.PLAYER_SPRITE_TILE_W / 2f;
        float boundingHeight = Constants.PLAYER_SPRITE_TILE_H / 2f;

        Map map = game.getMap();

        float newX = player.getX() + movement.x;
        float newY = player.getY() + movement.y;

        // check farm borders
        Result result;
        if (!(result = game.getMap().canWalkTo(Map.pixelToTileConverter(new Location(newX, newY)), player, game.getPlayers())).success()) {
            screen.showError(result.message());
            return;
        }

        if (movement.x > 0) {
            if (!map.isPositionPassable(newX + boundingWidth / 2, player.getY())) {
                return;
            }
        } else if (movement.x < 0) {
            if (!map.isPositionPassable(newX - boundingWidth / 2, player.getY())) {
                return;
            }
        }

        if (movement.y > 0) {
            if (!map.isPositionPassable(player.getX(), newY + boundingHeight / 2)) {
                return;
            }
        } else if (movement.y < 0) {
            if (!map.isPositionPassable(player.getX(), newY - boundingHeight / 2)) {
                return;
            }
        }

        player.setLocationAbsolut(newX, newY);
        player.setDirection(currentDirection);

        // change energy
        player.changeEnergy(-Constants.MAX_ENERGY * 0.0005 * ((speed * delta) / Map.TILE_SIZE));
        System.out.println("speed * delta = " + speed * delta);
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.toggleExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            screen.blackBackgroundAnimation(() -> controller.changeTurn());
        } else if (keycode == Input.Keys.BACKSLASH || keycode == Input.Keys.SLASH) {
            screen.toggleTerminalBox();
        }
        return true;
    }


}