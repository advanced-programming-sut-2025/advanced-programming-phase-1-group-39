package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.*;
import com.StardewValley.models.cropsAndFarming.Plant;
import com.StardewValley.models.cropsAndFarming.Tree;
import com.StardewValley.models.inventory.Inventory;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
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

    // Terminal
    private Window terminalWindow = null;
    private Table historyTable;
    private ScrollPane scrollPane;
    private TextField commandInput;
    private String lastCommand;

    //  player
    private TextureAtlas playerAtlas;
    private HashMap<Player, ArrayList<Animation<TextureRegion>>> playersAnimations = new LinkedHashMap<>();

    private float stateTime = 0f;

    // UI
    private Texture clock;
    private BitmapFont font;
    private BitmapFont smallFont;

    private GlyphLayout layout = new GlyphLayout();
    private TextureRegion inventorySlot;
    private TextureRegion inventoryHighlightSlot;


    private ProgressBar energyBar;
    private Image energyBox;
    private Label energyAmount;
    Table inventoryTable = new Table();
    private int lastSelectedSlot = -1;
    private int lastBagHash = 0;

    private final int MAX_CACHE_SIZE = 5000;
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

    private Label errorLabel;

    public GameScreen() {
        this.controller = AppGuiControllers.gameGuiController;
        controller.setScreen(this);
        this.game = App.getApp().getCurrentGame();
        gameMenuInputAdapter = new GameInputAdapter(controller, this);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
        batch = new SpriteBatch();
        this.camera = new OrthographicCamera();

        uiStage = new Stage(new FitViewport(1920, 1080));

        // all tables are adding to this stack
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);
        uiStage.addActor(rootStack);

        // for error message
        Table messageTable = new Table();
        messageTable.setFillParent(true);
        rootStack.add(messageTable);

        errorLabel = new Label("", GameAssetManager.messageBoxStyle);
        errorLabel.setVisible(false);
        errorLabel.setAlignment(Align.center);
        messageTable.add(errorLabel).bottom().padBottom(50).expandY(); // expandY is needed to effect by the bottom()

        // for hud table
        Table hudTable = new Table();
        hudTable.setFillParent(true);
        rootStack.add(hudTable);

        energyBar = new ProgressBar(0, (float) Constants.MAX_ENERGY, 1, true, GameAssetManager.greenBarStyle);
        energyBar.setAnimateDuration(0.1f);
        energyBox = new Image(GameAssetManager.energyBox);
        energyBox.setScaling(Scaling.fit); // to not scale and fill without proper scale
        Stack energyStack = new Stack();
        energyStack.add(energyBox);

        energyAmount = new Label("200 / 200", GameAssetManager.skin);
        energyAmount.setAlignment(Align.center);

        Table barTable = new Table();
        barTable.add(energyAmount).top().padTop(-10f).row();
        barTable.add(energyBar).expand().fill().pad(80,8,20,0);
//        barTable.add(energyBar).expand().fill().pad(10);
        energyStack.add(barTable);


        hudTable.add(energyStack)
                .width(1.5f * energyBox.getWidth()).height(1.5f * energyBox.getHeight())
                .expand().bottom().right().pad(20f);

        // inventory
        inventoryTable.setFillParent(false);
        inventoryTable.bottom().center().padTop(950f);
        rootStack.add(inventoryTable);
    }

    public void showError(String message) {
        if(message == null || message.isEmpty()) {
            errorLabel.setVisible(false);
            return;
        }

        errorLabel.setText(message);

        errorLabel.clearActions();
        errorLabel.getColor().a = 1;
        errorLabel.setVisible(true);

        errorLabel.addAction(Actions.sequence(
                Actions.delay(3f),
                Actions.fadeOut(1f),
                Actions.visible(false)
        ));
    }

    public void loadTextures() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("characters/Abigail/sprites_player.atlas"));
        clock = new Texture(Gdx.files.internal("Clock.png"));
        clock.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Color[] playerColors = new Color[4];
        playerColors[0] = Color.CYAN;
        playerColors[1] = Color.RED;
        playerColors[2] = Color.ORANGE;
        playerColors[3] = Color.WHITE;
        int i = 0;
        for (Player player : game.getPlayers()) {
            loadPlayerAnimations(player);
            player.setColor(playerColors[i++]);
        }

        inventorySlot = new TextureRegion(new Texture("inventory/Mail.2jpg.jpg"));
        inventoryHighlightSlot = new TextureRegion(new Texture("inventory/Mail3.jpg"));
    }

    private void loadPlayerAnimations(Player player) {
        ArrayList<Animation<TextureRegion>> animations = new ArrayList<>();
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
            animations.add(new Animation<>(0.15f, walkFrames, Animation.PlayMode.LOOP));
        }

        playersAnimations.put(player, animations);
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

    public void renderPlayers() {
        for (Player player : game.getPlayers()) {
            renderPlayer(player);
        }
    }

    private void renderPlayer(Player currentPlayer) {
        int animIndex = switch (currentPlayer.getDirection()) {
            case UP -> 3;
            case RIGHT -> 2;
            case DOWN -> 1;
            case LEFT -> 4;
            default -> 0;
        };

        batch.setColor(currentPlayer.getColor());

        Animation<TextureRegion> currentAnimation = playersAnimations.get(currentPlayer).get(animIndex);
        float elapsedTime = stateTime;

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);
        if (currentFrame == null) return;

        float drawX = currentPlayer.getX() - (Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_W) / 2f;
        float drawY = currentPlayer.getY();

        batch.draw(currentFrame, drawX, drawY, Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_W, Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_H);

        batch.setColor(Color.WHITE);
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

        String date = (time.getDayOfWeek().toString().substring(0,3)) + ". " + time.getDay();
        font.draw(batch, date, drawX + 150, drawY + 210);
        font.draw(batch, time.getHourText(), drawX + 150, drawY + 120);
        font.draw(batch, String.valueOf(400), drawX + 200, drawY + 40);
    }

    private void updateEnergyBar() {
        Player player = game.getPlayerInTurn();
        float playerEnergy = (float) player.getEnergy();
        energyBar.setValue(playerEnergy);

        if (playerEnergy > 170) energyBar.setStyle(GameAssetManager.greenBarStyle);
        else if (playerEnergy > 120) energyBar.setStyle(GameAssetManager.yellowBarStyle);
        else if (playerEnergy > 50) energyBar.setStyle(GameAssetManager.orangeBarStyle);
        else energyBar.setStyle(GameAssetManager.redBarStyle);

        String energyAmount;
        if (player.isEnergyUnlimited()) energyAmount = "infinite";
        else energyAmount = (int) playerEnergy + " / 200";

        this.energyAmount.setText(energyAmount);
    }

    private void renderInventory() {
        inventoryTable.clear();

        Player player = game.getPlayerInTurn();
        Inventory inventory = player.getInventory();
        int numSlots = player.getMaxInventorySize();

        for(int i = 0; i < numSlots; i++) {
            Stack slotStack = new Stack();

            // اسلات زمینه (پس‌زمینه)
            Image slotBg = new Image(new Texture("inventory/Mail.2jpg.jpg"));
            slotStack.add(slotBg);

            // اگر آیتم داشت، عکس آیتم و تعدادش
            if(i < inventory.getInventoryItems().size() && inventory.getInventoryItems().get(i) != null) {
                TextureRegionDrawable itemDrawable = new TextureRegionDrawable(inventory.getInventoryItems().get(i).getItem().getTexture());
                Image itemImg = new Image(itemDrawable);
                slotStack.add(itemImg);

                int quantity = inventory.getInventoryItems().get(i).getAmount();
                Label countLabel = new Label(String.valueOf(quantity), GameAssetManager.skin);
                countLabel.setFontScale(1f);
                slotStack.add(countLabel);
            }

            if(i == player.getSelectedSlot()) {
                Image highlight = new Image(new Texture("inventory/Mail3.jpg"));
                slotStack.add(highlight);

                if (i < inventory.getInventoryItems().size() && inventory.getInventoryItems().get(i) != null) {
                    TextureRegionDrawable itemDrawable = new TextureRegionDrawable(inventory.getInventoryItems()
                            .get(i).getItem().getTexture());
                    Image itemImg = new Image(itemDrawable);
                    slotStack.add(itemImg);
                }

            }

            inventoryTable.add(slotStack).size(60, 60); // سایز + فاصله بین اسلات‌ها
        }
    }


    // WINDOWS
    public void closeAllUiMenus() {
        if (exitWindow != null) hideExitMenu();
        if (terminalWindow != null && terminalWindow.isVisible()) hideExitMenu();
    }
    // exit Menu
    public void toggleExitMenu() {
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
                Main.getMain().switchScreen(new MainMenuScreen());
            }
        });
        TextButton nextTurnButton = new TextButton("Go Next Turn", skin);
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                blackBackgroundAnimation(() -> controller.changeTurn());
                controller.handleButtonDisable(game, exitGameButton);
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
        exitWindow.add(nextTurnButton).width(buttonsSize);
        exitWindow.row().padTop(15);
        exitWindow.add(exitGameButton).width(buttonsSize);
        exitWindow.row().padTop(25);
        exitWindow.add(backButton).width(buttonsSize);

        uiStage.addActor(exitWindow);
        Gdx.input.setInputProcessor(uiStage);
    }

    public void hideExitMenu() {
        exitWindow.remove();
        exitWindow = null;
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
    }

    // Terminal Window
    public void hideTerminalBox() {
        if (terminalWindow.isVisible()) {
            terminalWindow.setVisible(false);
            Gdx.input.setInputProcessor(gameMenuInputAdapter);
        }
    }

    public void toggleTerminalBox() {
        if (terminalWindow != null && terminalWindow.isVisible()) {
            hideTerminalBox();
        }

        closeAllUiMenus();

        Skin skin = GameAssetManager.skin;
        if (terminalWindow == null) {
            terminalWindow = new Window("", skin);
            terminalWindow.setModal(true);
            terminalWindow.setMovable(true);
            terminalWindow.setResizable(true);

            terminalWindow.setSize(1200, 600);
            terminalWindow.setPosition(
                    uiStage.getWidth() / 2f,
                    uiStage.getHeight() / 2f,
                    Align.center
            );

            historyTable = new Table(skin);
            historyTable.align(Align.topLeft);

            scrollPane = new ScrollPane(historyTable, skin);
            scrollPane.setFadeScrollBars(false);

            commandInput = new TextField("", skin);
            setCommandInputListener();

            terminalWindow.add(scrollPane).expand().fill().row();
            terminalWindow.add(commandInput).expandX().fillX().padTop(10);

            uiStage.addActor(terminalWindow);
        }
        terminalWindow.setVisible(true);
        commandInput.setText("");
        Gdx.input.setInputProcessor(uiStage);
        uiStage.setKeyboardFocus(commandInput);
    }

    private void setCommandInputListener() {
        commandInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    String command = textField.getText().trim();
                    if (command.isEmpty()) return;

                    addTextToHistory("> " + command, Color.YELLOW);
                    lastCommand = command;
                    String resultMessage = controller.processCommand(command);
                    addTextToHistory(resultMessage, Color.WHITE);

                    textField.setText("");
                }
            }
        });

        commandInput.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.UP) {
                    commandInput.setText(lastCommand != null ? lastCommand : "");
                    return true;
                } else if (keycode == com.badlogic.gdx.Input.Keys.SLASH
                        || keycode == com.badlogic.gdx.Input.Keys.BACKSLASH) {
                    hideTerminalBox();
                }
                return false;
            }
        });
    }

    private void addTextToHistory(String text, Color color) {
        if (historyTable == null) return;
        Label newText = new Label(text, GameAssetManager.skin);
        newText.setColor(color);
        newText.setWrap(true);
        historyTable.add(newText).expandX().fillX().left().padLeft(10).padBottom(10).row();
        scrollPane.layout();
        scrollPane.setScrollPercentY(1);
    }

    // Other Game Animations
    public void blackBackgroundAnimation(Runnable onCompleteRunnable) {
        Image blackScreen = new Image(GameAssetManager.blackBox);
        blackScreen.setSize(uiStage.getWidth(), uiStage.getHeight());
        blackScreen.setPosition(0, 0);

        blackScreen.setOrigin(Align.center);
        blackScreen.setScaleY(0f);

        blackScreen.addAction(Actions.sequence(
                Actions.scaleTo(1, 1, 0.3f),

                Actions.delay(0.2f),

                Actions.run(onCompleteRunnable),

                Actions.scaleTo(1, 0, 0.3f),

                Actions.removeActor()
        ));

        uiStage.addActor(blackScreen);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void show() {
        App.getApp().getMusic().pause();

        font = new BitmapFont();
        font.getData().setScale(2f);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(1f);
        smallFont.setColor(Color.valueOf("bc6c25"));

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadTextures();
        energyBar.setValue((float) game.getPlayerInTurn().getEnergy());

        showError("Welcome " + game.getPlayerInTurn().getNickname() + " !");
        //renderInventory();
    }

    @Override
    public void render(float v) {
        try {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            gameMenuInputAdapter.handlePlayerMovement(v, game);

            updateEnergyBar();
            renderCamera();

            // everything render based on camera
            batch.setProjectionMatrix(camera.combined);
            stateTime += v;
            batch.begin();
            renderTiles();
            renderPlayers();

            // TODO : (Better) move clock render to uiStage
            renderClockUI();
            int slot = game.getPlayerInTurn().getSelectedSlot();
            int bag = game.getPlayerInTurn().getInventory().hashCode();
            if (slot != lastSelectedSlot || bag != lastBagHash) {
                renderInventory();
                lastSelectedSlot = slot;
                lastBagHash = bag;
            }
            batch.end();

            uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            uiStage.draw();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
