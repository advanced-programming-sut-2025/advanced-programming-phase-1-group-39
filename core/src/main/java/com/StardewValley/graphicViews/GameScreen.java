package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.*;
import com.StardewValley.models.cropsAndFarming.Plant;
import com.StardewValley.models.cropsAndFarming.Tree;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameScreen implements Screen {
    private GameGuiController controller;
    private Game game;
    private GameInputAdapter gameMenuInputAdapter;
    private SpriteBatch batch;

    private Stage uiStage;
    private Window exitWindow = null;
    //  player
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
    private final HashMap<Location, TextureRegion> tileObjectCache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(HashMap.Entry<Location, TextureRegion> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };

    private OrthographicCamera camera;


    public GameScreen() {
        this.controller = AppGuiControllers.gameGuiController;
        this.game = App.getApp().getCurrentGame();
        gameMenuInputAdapter = new GameInputAdapter(controller, this);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
        batch = new SpriteBatch();
        this.camera = new OrthographicCamera();

        uiStage = new Stage(new FitViewport(1920, 1080));
    }

    public void loadTextures() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("characters/Abigail/sprites_player.atlas"));
        clock = new Texture(Gdx.files.internal("clock/Clock.png"));

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

    private TextureRegion getTileObjectTexture(Tile tile) {
        Location loc = tile.getLocation();

        TextureRegion texture = null;

        Plant plant = tile.getPlant();
        Tree tree = tile.getTree();

        if (plant == null && tree == null && tile.getItemOnTile() == null) {
            tileObjectCache.remove(loc);
        }

        if (plant != null && plant.stageChanged()) {
            tileObjectCache.remove(loc);
            texture = plant.getTexture();
            tileObjectCache.put(loc, texture);
            plant.syncLastStage();
        } else if (tree != null && tree.stageChanged()) {
            tileObjectCache.remove(loc);
            texture = tree.getTexture();
            tileObjectCache.put(loc, texture);
            tree.syncLastStage();
        }

        TextureRegion cached = tileObjectCache.get(loc);

        if (cached != null) {
            return cached;
        }

        if (tile.getTree() != null) {
            texture = tile.getTree().getTexture();
        } else if (tile.getPlant() != null) {
            texture = tile.getPlant().getTexture();
        } else if (tile.getItemOnTile() != null) {
            texture = tile.getItemOnTile().getItem().getTexture();
        }

        if (texture != null)
            tileObjectCache.put(loc, texture);

        return texture;
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
        int endX = Math.min(Constants.WORLD_MAP_WIDTH, startX + (int) (viewportWidth / tileSize) + 2);
        int endY = Math.min(Constants.WORLD_MAP_HEIGHT, startY + (int) (viewportHeight / tileSize) + 2);

        Tile[][] tiles = game.getMap().getTiles();

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int rowIndex = Constants.WORLD_MAP_HEIGHT - 1 - y;

                if (rowIndex < 0 || rowIndex >= Constants.WORLD_MAP_HEIGHT) continue;
                Tile tile = tiles[rowIndex][x];

                Location l = new Location(x, y);
                TextureRegion texture = tileCache.get(l);

                if (texture == null) {
                    texture = tile.getTexture();
                    tileCache.put(l, texture);
                }

                float drawX = x * tileSize;
                float drawY = y * tileSize;

                if (tile.isPlowed() && tile.isWatered()) {
                    batch.setColor(0.35f, 0.25f, 0.2f, 1f);
                } else if (tile.isPlowed()) {
                    batch.setColor(0.4f, 0.25f, 0.1f, 1f);
                } else if (tile.isWatered()) {
                    batch.setColor(0.75f, 0.75f, 0.75f, 1f);
                } else {
                    batch.setColor(1f, 1f, 1f, 1f);
                }


                batch.draw(texture, drawX, drawY, tileSize, tileSize);
            }
            batch.setColor(1, 1, 1, 1);
        }

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int rowIndex = Constants.WORLD_MAP_HEIGHT - 1 - y;

                if (rowIndex < 0 || rowIndex >= Constants.WORLD_MAP_HEIGHT) continue;
                Tile tile = tiles[rowIndex][x];

                float drawX = x * tileSize;
                float drawY = y * tileSize;

                TextureRegion objectTex = getTileObjectTexture(tile);
                if (objectTex != null) {
                    float objWidth = objectTex.getRegionWidth();
                    float objHeight = objectTex.getRegionHeight();

                    float scale = Math.min(tileSize / objWidth, (tileSize * 2) / objHeight);
                    float drawWidth = objWidth * scale;
                    float drawHeight = objHeight * scale;

                    float offsetX = (tileSize - drawWidth) / 2f;
                    float offsetY = 0f;

                    batch.draw(objectTex, drawX + offsetX, drawY + offsetY, drawWidth, drawHeight);
                }
            }
        }
    }

    public void handlePlayerMovement(float delta) {
        float speed = game.getGameSetting().getPlayerSpeed();

        Player player = game.getPlayerInTurn();
        Location newLocation = new Location(player.getX(), player.getY());
        currentDirection = Direction.NONE;

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
        // TODO : check movable
        player.setLocationAbsolut(newLocation.x(), newLocation.y());
    }


    private void renderPlayer() {
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

        Player currentPlayer = game.getPlayerInTurn();

        float drawX = currentPlayer.getX() - (Map.TILE_SIZE / 2f);
        float drawY = currentPlayer.getY();

        batch.draw(currentFrame, drawX, drawY, Map.TILE_SIZE, Map.TILE_SIZE * 2);
    }

    private void renderCamera() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }

    private void renderClockUI() {
        float clockWidth = clock.getWidth();
        float clockHeight = clock.getHeight();

        float drawX = camera.position.x + (camera.viewportWidth / 2) - clockWidth - 20;
        float drawY = camera.position.y + (camera.viewportHeight / 2) - clockHeight - 20;


        batch.draw(clock, drawX, drawY);
        Time time = App.getApp().getCurrentGame().getTime();
        TextureRegion season = new TextureRegion(new Texture(Gdx.files.internal("clock/" + time.getSeason().name() + ".png")));
        TextureRegion weather = new TextureRegion(new Texture(Gdx.files.internal("clock/" + App.getApp().getCurrentGame().getTodayWeather().getStatus().name() + ".png")));
        batch.draw(season, drawX + 210, drawY + 137, (float) season.getRegionWidth() /2, (float) season.getRegionHeight() /2);
        batch.draw(weather, drawX + 115, drawY + 137, (float) weather.getRegionWidth() /2, (float) weather.getRegionHeight() /2);

        String date = (time.getDayOfWeek().toString().substring(0,3)) + ". " + time.getDay();
        font.draw(batch, date, drawX + 150, drawY + 210);
        font.draw(batch, time.getHourText(), drawX + 150, drawY + 120);
        font.draw(batch, String.valueOf(App.getApp().getCurrentGame().getPlayerInTurn().getMoney()), drawX + 200, drawY + 40);
    }

    public void showExitMenu() {
        if (exitWindow != null) {
            hideExitMenu();
            return;
        }

        Skin skin = GameAssetManager.skin;
        exitWindow = new Window("", skin);
        exitWindow.setModal(true);
        exitWindow.setSize(800, 500);
        exitWindow.setPosition(
                uiStage.getWidth() / 2f,
                uiStage.getHeight() / 2f,
                Align.center
        );

        TextButton exitGameButton = new TextButton("Exit & Save Game", skin);
        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().setScreen(new MainMenuScreen());
            }
        });
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideExitMenu();
            }
        });
        controller.handleButtonDisable(game, exitGameButton);

        int buttonsSize = 500;
        exitWindow.row();
        exitWindow.add(exitGameButton).width(buttonsSize);
        exitWindow.row().padTop(15);
        exitWindow.add(backButton).width(buttonsSize);

        uiStage.addActor(exitWindow);
        Gdx.input.setInputProcessor(uiStage);
    }

    public void hideExitMenu() {
        exitWindow.remove();
        exitWindow = null;
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
    }

    @Override
    public void show() {
        App.getApp().getMusic().pause();

        font = new BitmapFont();
        font.getData().setScale(2f);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        loadTextures();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handlePlayerMovement(v);
        renderCamera();
        // everything render based on camera
        batch.setProjectionMatrix(camera.combined);
        stateTime += v;
        batch.begin();
        renderTiles();
        renderPlayer();
        // TODO : (Better) move clock render to uiStage
        renderClockUI();
        batch.end();

        uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        uiStage.draw();
    }

    @Override
    public void resize(int w, int h) {
        uiStage.getViewport().update(w,h, true);

        camera.viewportWidth = w;
        camera.viewportHeight = h;
        camera.update();

        Gdx.input.setInputProcessor(gameMenuInputAdapter);
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
        }
    }
}
