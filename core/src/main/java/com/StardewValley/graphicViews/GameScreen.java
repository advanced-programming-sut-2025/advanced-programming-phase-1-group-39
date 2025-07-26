package com.StardewValley.graphicViews;

import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.GameController;
import com.StardewValley.models.App;
import com.StardewValley.models.Constants;
import com.StardewValley.models.Game;
import com.StardewValley.models.Location;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameScreen implements Screen {
    private GameController controller;
    private Game game;
    private GameMenuInputAdapter gameMenuInputAdapter;
    private SpriteBatch batch;

    private final int MAX_CACHE_SIZE = 3000;
    private final HashMap<Location, TextureRegion> tileCache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(HashMap.Entry<Location, TextureRegion> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };
    private OrthographicCamera camera;


    public GameScreen() {
        this.controller = AppControllers.gameController;
        this.game = App.getApp().getCurrentGame();
        gameMenuInputAdapter = new GameMenuInputAdapter(controller);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
        batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
    }


    public void renderTiles() {
        float camX = camera.position.x;
        float camY = camera.position.y;
        float viewportWidth = camera.viewportWidth;
        float viewportHeight = camera.viewportHeight;

        int tileSize = Map.TILE_SIZE;

        float cameraLeft = camX - viewportWidth / 2;
        float cameraBottom = camY - viewportHeight / 2;

        int startX = Math.max(0, (int) (cameraLeft / tileSize));
        int startY = Math.max(0, (int) (cameraBottom / tileSize));
        int endX = Math.min(Constants.FARM_WIDTH, startX + (int) (viewportWidth / tileSize) + 2);
        int endY = Math.min(Constants.FARM_HEIGHT, startY + (int) (viewportHeight / tileSize) + 2);

        Tile[][] tiles = game.getMap().getTiles();

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Location l = new Location(x, y);
                TextureRegion texture = tileCache.get(l);

                if (texture == null) {
                    Tile tile = tiles[Constants.FARM_HEIGHT - y][x];
                    texture = tile.getType().getTextureRegion();
                    tileCache.put(l, texture);
                }

                float drawX = x * tileSize;
                float drawY = y * tileSize;

                batch.draw(texture, drawX, drawY, tileSize, tileSize);
            }
        }
    }

    public void handleCameraMovement(float delta) {
        float cameraSpeed = 1500f;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += cameraSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= cameraSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= cameraSpeed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += cameraSpeed * delta;
        }

        camera.update();
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(game.getPlayerInTurn().getLocation().x(), game.getPlayerInTurn().getLocation().y(), 0);

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleCameraMovement(v);

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
