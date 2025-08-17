package com.StardewValley.graphicViews;


import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Direction;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.map.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
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

        // check if not conscious
        Player player = game.getPlayerInTurn();
        if (player.getCurrentState().equals(GameScreen.PlayerState.Unconscious)) return;
        float initialX = player.getX(),
                initialY = player.getY();

        // greenhouse check
        if (controller.nearGreenHouse(player) && !player.isBuildGreenhouse()) {
            Result buildGreenHousePopup = controller.buildGreenHouseRequest(player);
            if (buildGreenHousePopup.success()) {
                screen.showPopup(buildGreenHousePopup.message(), () -> {
                    screen.blackBackgroundAnimation(() -> controller.buildGreenhouse(player, game), 0.5f);
                });
            } else {
                screen.showError(buildGreenHousePopup.message());
            }
        }


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

        if (game.isPositionPassable(newX, player.getY())) {
            player.setLocationAbsolut(newX, player.getY());
        }
        if (game.isPositionPassable(player.getX(), newY)) {
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
            screen.blackBackgroundAnimation(() -> controller.changeTurn(), 0.2f);
        } else if (keycode == Input.Keys.BACKSLASH || keycode == Input.Keys.SLASH) {
            screen.toggleTerminalBox();
        }
        ///  test
        else if (keycode == Input.Keys.MINUS) {
            screen.getGame().getPlayerInTurn().changeEnergy(-10);
        } else if (keycode == Input.Keys.TAB) {
            screen.cheatPlayer();
        } else if (keycode == Input.Keys.N) {
            Game game = App.getApp().getCurrentGame();
            game.getMap().growWateredPlantsAndTrees();
        } else if (keycode == Input.Keys.C) {
            screen.changeCookingMenu();
        } else if (keycode == Input.Keys.B) {
            screen.changeCraftingMenu();
        } else if (keycode == Input.Keys.P) {
            screen.showShopMenu();
        } else if (keycode == Input.Keys.E) {
            screen.toggleInventoryMenu();
        } else if (keycode == Input.Keys.M) {
            screen.toggleBiggerMiniMap();
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int selectedSlot = keycode - Input.Keys.NUM_1; // 0 تا 8
            Player player = screen.getGame().getPlayerInTurn();
            if (selectedSlot < player.getMaxInventorySize()) {
                player.setSelectedSlot(selectedSlot);
            }
        } else if (keycode == Input.Keys.NUM_0) {
            Player player = screen.getGame().getPlayerInTurn();
            int slotIndex = 9; // اسلات دهم
            if (slotIndex < player.getMaxInventorySize()) {
                player.setSelectedSlot(slotIndex);
            }
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
        if (button == Input.Buttons.RIGHT) {
            Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 worldCoords = screen.getCamera().unproject(new Vector3(screenX, screenY, 0));
            screen.getCamera().unproject(worldCoordinates);

            for (Player player : screen.getGame().getPlayers()) {
                for (Animal animal : player.getAnimals()) {
                    float animalX = animal.getX();
                    float animalY = animal.getY();
                    int tileSize = Map.TILE_SIZE;
                    Rectangle animalBounds = new Rectangle(animalX, animalY, tileSize, tileSize);

                    if (animalBounds.contains(worldCoordinates.x, worldCoordinates.y)) {
                        screen.showAnimalInfoPopup(animal);
                        return true;
                    }
                }
            }

            for (String npcId : screen.npcNames) {
                Rectangle bounds = new Rectangle(screen.getNPCx(npcId), screen.getNPCy(npcId),
                        screen.getNPCTexture(npcId).getWidth(),
                        screen.getNPCTexture(npcId).getHeight());
                if (bounds.contains(worldCoords.x, worldCoords.y)) {
                    screen.openNpcGiftMenu(npcId);
                    return true;
                }
            }
        } else if (button == Input.Buttons.LEFT) {
            // ۱. مختصات screen به world
            Vector3 worldCoords = screen.getCamera().unproject(new Vector3(screenX, screenY, 0));
            // ۲. تبدیل world به tile
            Location mouseTile = Map.pixelToTileConverter(new Location(worldCoords.x, worldCoords.y));
            Player player = screen.getGame().getPlayerInTurn();
            Location playerTile = player.getTileLocation();

            Direction dir = getMouseDirectionAroundPlayer(playerTile, mouseTile);

            Rectangle sellBinBounds = new Rectangle(
                    screen.getShippingBin().getLocation().x(),
                    screen.getShippingBin().getLocation().y(),
                    screen.getShippingBin().getWidth(),
                    screen.getShippingBin().getHeight()
            );

            if (sellBinBounds.contains(worldCoords.x, worldCoords.y)) {
                screen.showSellBasketWindow();
                return true;
            }

            // دیباگ برای تست مختصات:
            System.out.println("mouseTile: " + mouseTile.x() + "," + mouseTile.y());
            System.out.println("playerTile: " + playerTile.x() + "," + playerTile.y());
            System.out.println("dir: " + dir);

            if (dir != null) {
                controller.useTool(dir);
            }
            // چک برای هر NPC
            for (String npcId : screen.npcNames) {
                float npcX = screen.getNPCx(npcId);
                float npcY = screen.getNPCy(npcId);
                Texture npcTexture = screen.getNPCTexture(npcId);

                float cloudX = npcX;
                float cloudY = npcY + npcTexture.getHeight() + 10;
                float cloudW = screen.isDialogVisible(npcId) ?
                        screen.getSpeechBubbleWidth(npcId) :
                        screen.getSpeechCloudTexture().getWidth();
                float cloudH = screen.isDialogVisible(npcId) ?
                        screen.getSpeechBubbleHeight(npcId) :
                        screen.getSpeechCloudTexture().getHeight();

                if (worldCoords.x >= cloudX && worldCoords.x <= cloudX + cloudW &&
                        worldCoords.y >= cloudY && worldCoords.y <= cloudY + cloudH) {
                    screen.toggleNpcDialog(npcId);
                    return true;
                }
            }// دیگه لازم نیست ادامه بدیم
        }
        return false;
    }
}