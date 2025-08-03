package com.StardewValley.graphicViews;

import com.StardewValley.models.Enums.Direction;
import com.StardewValley.models.Game;
import com.StardewValley.models.Location;
import com.StardewValley.models.Player;
import com.StardewValley.models.Result;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

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
        Location newLocation = new Location(player.getX(), player.getY());
        Direction currentDirection = Direction.NONE;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            newLocation.addVector(0, +speed * delta);
            currentDirection = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            newLocation.addVector(0, -speed * delta);
            currentDirection = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            newLocation.addVector(-speed * delta, 0);
            currentDirection = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            newLocation.addVector(speed * delta, 0);
            currentDirection = Direction.RIGHT;
        }
        // check can move to
        Location tileLoc = Map.pixelToTileConverter(newLocation);
        Tile tile = game.getMap().getTile(tileLoc.x(), tileLoc.y());

        Result result;
        if (!(result = game.getMap().canWalkTo(tileLoc, player, game.getPlayers())).success()) {
            screen.showError(result.message());
            return;
        }
        if (tile != null && tile.canWalkOnTile()) {
            player.setLocationAbsolut(newLocation.x(), newLocation.y());
            player.setDirection(currentDirection);
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.showExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            controller.changeTurn();
        }
        return true;
    }


}