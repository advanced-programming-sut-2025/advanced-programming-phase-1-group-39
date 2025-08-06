package com.StardewValley.graphicViews;


import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Direction;
import com.StardewValley.models.animals.Animal;
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

        // check if not conscious
        Player player = game.getPlayerInTurn();
        if (player.getCurrentState().equals(GameScreen.PlayerState.Unconscious)) return;
        float   initialX = player.getX(),
                initialY = player.getY();

        // greenhouse check
        if (controller.nearGreenHouse(player) && !player.isBuildGreenhouse()) {
            Result buildGreenHousePopup = controller.buildGreenHouseRequest(player);
            if (buildGreenHousePopup.success()) {
                screen.showPopup(buildGreenHousePopup.message(), ()->{
                    screen.blackBackgroundAnimation(()-> controller.buildGreenhouse(player, game), 0.5f);
                });
            } else {
                screen.showError(buildGreenHousePopup.message());
            }
        }


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
        float newX = player.getX() + movement.x;
        float newY = player.getY() + movement.y;

        // check farm borders
        Result result;
        if (!(result = game.getMap().canWalkTo(Map.pixelToTileConverter(new Location(newX, newY)), player, game.getPlayers()))
                .success()) {
            screen.showError(result.message());
            return;
        }

        if (game.isPositionPassable(newX, player.getY())) {
            player.setLocationAbsolut(newX, player.getY());
        }
        if (game.isPositionPassable(player.getX(), newY)) {
            player.setLocationAbsolut(player.getX(), newY);
        }
        player.setDirection(currentDirection);
        // change energy
        if (player.getX() != initialX || player.getY() != initialY) {
            player.changeEnergy(-Constants.MAX_ENERGY * 0.0005 * ((speed * delta) / Map.TILE_SIZE));
        }
    }

    public void handleAnimalMovement(Game game, Animal animal, Vector2 movement) {
        float newX = animal.getX() + movement.x;
        float newY = animal.getY() + movement.y;

        // check farm borders
        if (game.isPositionPassable(newX, animal.getY())) {
            animal.setLoc(newX, animal.getY());
        }
        if (game.isPositionPassable(animal.getX(), newY)) {
            animal.setLoc(animal.getX(), newY);
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            screen.toggleExitMenu();
        } else if (keycode == Input.Keys.ENTER) {
            screen.blackBackgroundAnimation(() -> controller.changeTurn(), 0.2f);
        } else if (keycode == Input.Keys.BACKSLASH || keycode == Input.Keys.SLASH) {
            screen.toggleTerminalBox();
        }
        ///  test
        else if (keycode == Input.Keys.MINUS) {
            screen.getGame().getPlayerInTurn().changeEnergy(-10);
        }

        else if (keycode == Input.Keys.N) {
            Game game = App.getApp().getCurrentGame();
            game.getMap().growWateredPlantsAndTrees();
        } else if (keycode == Input.Keys.C) {
            screen.changeCookingMenu();
        } else if (keycode == Input.Keys.B) {
            screen.changeCraftingMenu();
        } else if (keycode == Input.Keys.P) {
            screen.showShopMenu();
        } else if (keycode == Input.Keys.TAB) {
            screen.cheatPlayer();
        }

        else if (keycode == Input.Keys.M) {
            screen.toggleBiggerMiniMap();
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
}