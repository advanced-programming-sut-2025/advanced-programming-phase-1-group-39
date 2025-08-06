package com.StardewValley.graphicViews;

import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Direction;
import com.StardewValley.models.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
        float initialX = player.getX(), initialY = player.getY();


        Direction currentDirection = Direction.NONE;

        Vector2 movement = new Vector2(0, 0);
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
            player.setMoving(false);
            player.setDirection(Direction.NONE);
            return;
        }

        Map map = game.getMap();

        float newX = player.getX() + movement.x;
        float newY = player.getY() + movement.y;

        // check farm borders
        Result result;
        if (!(result = game.getMap().canWalkTo(Map.pixelToTileConverter(new Location(newX, newY)), player, game.getPlayers()))
                .success()) {
            screen.showError(result.message());
            return;
        }

        if (map.isPositionPassable(newX, player.getY())) {
            player.setLocationAbsolut(newX, player.getY());
        }
        if (map.isPositionPassable(player.getX(), newY)) {
            player.setLocationAbsolut(player.getX(), newY);
        }
        player.setMoving(true);
        player.setDirection(currentDirection);
        // change energy
        if (player.getX() != initialX || player.getY() != initialY) {
            player.changeEnergy(-Constants.MAX_ENERGY * 0.0005 * ((speed * delta) / Map.TILE_SIZE));
        }
    }

    public static Direction getMouseDirectionAroundPlayer(Location playerLoc, Location mouseTileLoc) {
        int dx = mouseTileLoc.x() - playerLoc.x();
        int dy = mouseTileLoc.y() - playerLoc.y();

        for (Direction dir : Direction.values()) {
            if (dir == Direction.NONE) continue;
            if (dx == dir.dx && dy == dir.dy) {
                return dir;
            }
        }
        return null;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.toggleExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            screen.blackBackgroundAnimation(() -> controller.changeTurn());
        } else if (keycode == Input.Keys.BACKSLASH || keycode == Input.Keys.SLASH) {
            screen.toggleTerminalBox();
        } else if (keycode == Input.Keys.E) {
            screen.toggleInventoryMenu();
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Game game = screen.getGame();

        int current = game.getPlayerInTurn().getSelectedSlot();
        int size = game.getPlayerInTurn().getMaxInventorySize();
        int next = (current + (amountY > 0 ? 1 : -1) + size) % size;
        game.getPlayerInTurn().setSelectedSlot(next);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // ۱. مختصات screen به world
        Vector3 worldCoords = screen.getCamera().unproject(new Vector3(screenX, screenY, 0));
        // ۲. تبدیل world به tile
        Location mouseTile = Map.pixelToTileConverter(new Location(worldCoords.x, worldCoords.y));
        Player player = screen.getGame().getPlayerInTurn();
        Location playerTile = player.getTileLocation();

        Direction dir = getMouseDirectionAroundPlayer(playerTile, mouseTile);

        // دیباگ برای تست مختصات:
        System.out.println("mouseTile: " + mouseTile.x() + "," + mouseTile.y());
        System.out.println("playerTile: " + playerTile.x() + "," + playerTile.y());
        System.out.println("dir: " + dir);

        if (dir != null) {
            controller.useTool(dir);
        }
        return true;
    }
}