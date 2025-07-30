package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.GameController;
import com.StardewValley.models.*;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.services.AppDataManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameScreen implements Screen {
    private GameController controller;
    private Game game;
    private GameMenuInputAdapter gameMenuInputAdapter;
    private SpriteBatch batch;

    private TextureAtlas playerAtlas;
    private final ArrayList<Animation<TextureRegion>> playerAnimations = new ArrayList<>();

    private enum Direction { UP, DOWN, LEFT, RIGHT, NONE }

    private Direction currentDirection = Direction.NONE;
    private float stateTime = 0f;

    private Texture clock;
    private BitmapFont font;

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

    public void loadTextures() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("characters/Abigail/sprites_player.atlas"));
        clock = new Texture(Gdx.files.internal("Clock.png"));

        for (int i = 14; i > 9; i--) {
            Array<TextureRegion> walkFrames = new Array<>();
            if (i == 14) {
                for (int j = 0; j < 4; j++) {
                    String region = "player_" + 13 + "_" + 0;
                    walkFrames.add(playerAtlas.findRegion(region));
                }
            } else {
                for (int j = 0; j < 4; j++) {
                    String region = "player_" + i + "_" + j;
                    walkFrames.add(playerAtlas.findRegion(region));
                }
            }
            playerAnimations.add(new Animation<>(0.15f, walkFrames, Animation.PlayMode.LOOP));
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

        Player currentPlayer = App.getApp().getCurrentGame().getPlayerInTurn();

        int startX = Math.max(currentPlayer.getStartOfFarm().x(), (int) (cameraLeft / tileSize));
        int startY = Math.max(currentPlayer.getStartOfFarm().y(), (int) (cameraBottom / tileSize));
        int endX = Math.min(currentPlayer.getEndOfFarm().x(), startX + (int) (viewportWidth / tileSize) + 2);
        int endY = Math.min(currentPlayer.getEndOfFarm().y(), startY + (int) (viewportHeight / tileSize) + 2);

        Tile[][] tiles = game.getMap().getTiles();

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                Location l = new Location(x, y);
                TextureRegion texture = tileCache.get(l);

                if (texture == null) {
                    Tile tile = tiles[Constants.FARM_HEIGHT - y + 1][x];
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
        currentDirection = Direction.NONE;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += cameraSpeed * delta;
            currentDirection = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= cameraSpeed * delta;
            currentDirection = Direction.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= cameraSpeed * delta;
            currentDirection = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += cameraSpeed * delta;
            currentDirection = Direction.RIGHT;
        }

        camera.update();
    }


    private void renderPlayer() {
        int tileSize = Map.TILE_SIZE;

        int animIndex = switch (currentDirection) {
            case UP -> 3;
            case RIGHT -> 2;
            case DOWN -> 1;
            case LEFT -> 4;
            default -> 0;
        };

        Animation<TextureRegion> currentAnimation = playerAnimations.get(animIndex);
        float elapsedTime = stateTime;

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);
        if (currentFrame == null) return;

        float drawX = camera.position.x - tileSize / 2f;
        float drawY = camera.position.y - tileSize;

        batch.draw(currentFrame, drawX, drawY, tileSize, tileSize * 2);
    }

    private void renderClockUI() {
        float uiWidth = clock.getWidth();
        float uiHeight = clock.getHeight();

        float drawX = camera.position.x + (camera.viewportWidth / 2) - uiWidth - 20;
        float drawY = camera.position.y + (camera.viewportHeight / 2) - uiHeight - 20;


        batch.draw(clock, drawX, drawY);
        Time time = App.getApp().getCurrentGame().getTime();
        font.draw(batch, time.getDayOfWeek() + ". " + time.getDay(), drawX + 100, drawY + 210);
        font.draw(batch, time.getHourText(), drawX + 150, drawY + 120);
        font.draw(batch, String.valueOf(400), drawX + 200, drawY + 40);
    }



    @Override
    public void show() {
        font = new BitmapFont();
        font.getData().setScale(2f);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(game.getPlayerInTurn().getLocation().x() * Map.TILE_SIZE, game.getPlayerInTurn().getLocation().y() * Map.TILE_SIZE, 0);
        loadTextures();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleCameraMovement(v);

        batch.setProjectionMatrix(camera.combined);
        stateTime += v;
        batch.begin();
        renderTiles();
        renderPlayer();
        renderClockUI();

        /// test
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Main.getMain().switchScreen(new MainMenuScreen());
        }

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
        AppDataManager.saveApp();
        if (game != null) {
            AppDataManager.saveGame(game);
            System.out.println("Saved game");
        }
    }
}
