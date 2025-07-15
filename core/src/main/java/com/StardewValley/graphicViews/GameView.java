package com.StardewValley.graphicViews;

import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.GameController;
import com.StardewValley.models.App;
import com.StardewValley.models.Constants;
import com.StardewValley.models.Game;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;

public class GameView implements Screen {
    private GameController controller;
    private Game game;
    private GameMenuInputAdapter gameMenuInputAdapter;
    private SpriteBatch batch;
    private TextureRegion[][] tileTextures = new TextureRegion[150][300];
    private OrthographicCamera camera;

    public GameView() {
        this.controller = AppControllers.gameController;
        this.game = App.getApp().getCurrentGame();
        gameMenuInputAdapter = new GameMenuInputAdapter(controller);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
        batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
    }

    public void loadTextures(int endx, int endy) {
        Tile[][] tiles = game.getMap().getTiles();
        for (int i = 0; i < endx; i++) {
            for (int j = 0; j < endy; j++) {
                Tile tile = tiles[i][j];
                tileTextures[i][j] = tile.getType().getTextureRegion();
            }
        }
    }

    public void renderTiles() {
        float camX = camera.position.x;
        float camY = camera.position.y;
        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;

        int tileSize = Map.TILE_SIZE;

        float cameraLeft = camX - viewportWidth / 2;
        float cameraBottom = camY - viewportHeight / 2;

        int startX = 0;//Math.max(0, (int) (cameraLeft / tileSize) - 2);
        int startY = 0;//Math.max(0, (int) (cameraBottom / tileSize) - 2);
        int endX = 50;//Math.min(Constants.FARM_WIDTH * 160, (int) ((camX + viewportWidth / 2) / tileSize) + 2);
        int endY = 80;//Math.min(Constants.FARM_HEIGHT * 160, (int) ((camY + viewportHeight / 2) / tileSize) + 2);

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            startX += 1;
            startY += 1;
            endX += 1;
            endY += 1;
        }

        loadTextures(endX, endY);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                float drawX = x * tileSize;// - cameraLeft;
                float drawY = y * tileSize;// - cameraBottom;

                TextureRegion texture = tileTextures[x][y];
                if (texture != null) {
                    batch.draw(texture, drawX, drawY, tileSize, tileSize);

                }
            }
        }
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(game.getPlayerInTurn().getLocation().x(), game.getPlayerInTurn().getLocation().y(), 0);
        //loadTextures();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderTiles();
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
