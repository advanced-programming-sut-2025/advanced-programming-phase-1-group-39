package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.WeatherStatus;
import com.StardewValley.models.NPC.AbigailNPC;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.NPC.PlayerNPCInteraction;
import com.StardewValley.models.Shops.Shop;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.animals.AnimalType;
import com.StardewValley.models.animals.LivingPlace;
import com.StardewValley.models.buildings.AnimalBuilding;
import com.StardewValley.models.buildings.Building;
import com.StardewValley.models.buildings.ShippingBin;
import com.StardewValley.models.cooking.FoodRecipe;
import com.StardewValley.models.crafting.CraftingRecipe;
import com.StardewValley.models.crafting.CraftingWidget;
import com.StardewValley.models.cropsAndFarming.Plant;
import com.StardewValley.models.cropsAndFarming.Tree;
import com.StardewValley.models.inventory.Inventory;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.map.Tile;
import com.StardewValley.models.map.TileType;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameScreen implements Screen {
    private static GameScreen screen;
    private GameGuiController controller;
    private static Game game;
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

    // player
    private TextureAtlas playerAtlas;
    private HashMap<Player, ArrayList<Animation<TextureRegion>>> playersAnimations = new LinkedHashMap<>();

    private float stateTime = 0f;


    private TextureRegion seasonTexture;
    private TextureRegion weatherTexture;

    // // player states
    public enum PlayerState {
        WalkingOrIdle,
        UsingTool,
        Unconscious
    }

    private boolean wentNextDay = false;


    // UI
    private Texture clock;
    private BitmapFont font;
    private BitmapFont smallFont;

    private GlyphLayout layout = new GlyphLayout();
    private TextureRegion inventorySlot;
    private TextureRegion inventoryHighlightSlot;

    // MiniMap window
    private Cell<MiniMapWidget> minimapCell;
    private Window bigMinimapWindow = null;

    private MiniMapWidget miniMapWidget;

    private ProgressBar energyBar;
    private Image energyBox;
    private Label energyAmount;

    private final int MAX_CACHE_SIZE = 3000;
    private final HashMap<Location, TextureRegion> tileCache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(HashMap.Entry<Location, TextureRegion> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    };

    private final HashMap<Location, TileType> tileTypeCache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(HashMap.Entry<Location, TileType> eldest) {
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
    private Window popupWindow;
    private Label popupText;
    private TextButton popupYesButton;
    private TextButton popupNoButton;
    private Runnable popupRunnable;

    // inventory
    private Integer pendingTrashSlot = null;
    private boolean pendingInventoryUiRefresh = false;
    private Window inventoryWindow = null;
    Table inventoryTable = new Table();
    private int lastSelectedSlot = -1;
    private int lastBagHash = 0;
    private Cell<?> contentCell;
    private Window skillTooltip;
    private Label skillTooltipLabel;
    private Image skillDescImage;
    private Texture starTexture;
    private HashMap<String, Integer> friendPlayers;


    // NPC
    private Texture npcAbigail, npcHarvey, npcLeah, npcRobin, npcSebastian;
    private Texture speechCloudTexture;
    private Texture speechBubbleTexture;
    public static Location abigailLocation;
    public static Location harveyLocation;
    public static Location leahLocation;
    public static Location robinLocation;
    public static Location sebastianLocation;
    private HashMap<String, Boolean> npcDialogVisible = new HashMap<>();
    public String[] npcNames = {"sebastian", "abigail", "harvey", "leah", "robin"};
    private Window npcMenuWindow;
    private String npcMenuCurrentNpcId;
    private HashMap<String, Boolean> npcHeartVisible = new HashMap<>();
    private Texture heartBubbleTexture;

    // animal popup
    private Window animalWindow;
    private Label animalName;
    private Image animalImage;
    private Animal animalToggled;
    private TextButton feedAnimalButton;
    private TextButton getProductsButton;
    private TextButton shepherdAnimalButton;


    // effects
    private Stage effectsStage;
    private Animation<TextureRegion> rainAnimation;
    private Animation<TextureRegion> snowAnimation;

    private Image weatherEffectImage;
    private float animationTime = 0f;

    private Image nightOverlay;

    // Cooking and crafting
    private Table cookingMenuTable;
    private ArrayList<TextButton> cookingMenuButtons = new ArrayList<>();
    private boolean cookingMenuOpen = false;
    private Table craftingMenu;
    private ArrayList<CraftingWidget> craftingWidgets = new ArrayList<>();
    private boolean craftingMenuOpen = false;


    public GameScreen() {
        this.screen = this;
        this.controller = AppGuiControllers.gameGuiController;
        controller.setScreen(this);
        this.game = App.getApp().getCurrentGame();
        this.friendPlayers = getFriendshipLevel();
        gameMenuInputAdapter = new GameInputAdapter(controller, this);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
        batch = new SpriteBatch();
        this.camera = new OrthographicCamera();

        uiStage = new Stage(new FitViewport(1920, 1080));
        effectsStage = new Stage(new FitViewport(1920, 1080));
        // all tables are adding to this stack
        Stack rootStack = new Stack();
        rootStack.setFillParent(true);
        uiStage.addActor(rootStack);

        // night black background
        nightOverlay = new Image(GameAssetManager.blackBox);
        nightOverlay.setFillParent(true);
        nightOverlay.setTouchable(Touchable.disabled);
        rootStack.add(nightOverlay);
        nightOverlay.getColor().a = 0;

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
        barTable.add(energyBar).expand().fill().pad(80, 8, 20, 0);
        energyStack.add(barTable);

        miniMapWidget = new MiniMapWidget(this.game);
        minimapCell = hudTable.add(miniMapWidget).size(200, 150).top().left().pad(20);

        hudTable.add(energyStack)
                .width(1.5f * energyBox.getWidth()).height(1.5f * energyBox.getHeight())
                .expand().bottom().right().pad(20f);

        // inventory
        inventoryTable.setFillParent(false);
        inventoryTable.bottom().center().padTop(950f);
        rootStack.add(inventoryTable);

        // NPC
        this.npcAbigail = new Texture("NPC/Abigail1.png");
        this.npcHarvey = new Texture("NPC/Abigail1.png");
        this.npcLeah = new Texture("NPC/Leah1.png");
        this.npcRobin = new Texture("NPC/Robin1.png");
        this.npcSebastian = new Texture("NPC/Sebastian1.png");
        this.speechCloudTexture = new Texture("NPC/Emot1.png");
        this.speechBubbleTexture = new Texture("NPC/comment.png");
        abigailLocation = Map.TileToPixelConverter(game.getNPC("abigail").getLocation());
        harveyLocation = Map.TileToPixelConverter(game.getNPC("harvey").getLocation());
        leahLocation = Map.TileToPixelConverter(game.getNPC("leah").getLocation());
        robinLocation = Map.TileToPixelConverter(game.getNPC("robin").getLocation());
        sebastianLocation = Map.TileToPixelConverter(game.getNPC("sebastian").getLocation());
        for (String npcName : npcNames) {
            npcDialogVisible.put(npcName, false);
        }
        this.heartBubbleTexture = new Texture("NPC/Emote2.png");
        for (String npc : npcNames) {
            npcHeartVisible.put(npc.toLowerCase(), false);
        }

        // for error message
        Table messageTable = new Table();
        messageTable.setFillParent(true);
        rootStack.add(messageTable);

        errorLabel = new Label("", GameAssetManager.messageBoxStyle);
        errorLabel.setVisible(false);
        errorLabel.setAlignment(Align.center);
        messageTable.add(errorLabel).bottom().padBottom(50).expandY(); // expandY is needed to effect by the bottom()
    }

    // utils and menu
    public void showError(String message) {
        if (message == null || message.isEmpty()) {
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


    public void loadPopupWindow() {
        popupWindow = new Window("", GameAssetManager.skin);
        popupWindow.setVisible(false);
        popupWindow.setModal(true);

        popupText = new Label("", GameAssetManager.messageBoxStyle);
        popupText.setWrap(true);
        popupText.setAlignment(Align.center);
        popupYesButton = new TextButton("Yes", GameAssetManager.skin);
        popupNoButton = new TextButton("No", GameAssetManager.skin);

        popupWindow.add(popupText).width(760).colspan(2).expandX().fillX().pad(20).row();
        popupWindow.add(popupNoButton).pad(20).uniformX();
        popupWindow.add(popupYesButton).pad(20).uniformX();
        popupWindow.pack();
        popupWindow.setVisible(false);


        uiStage.addActor(popupWindow);

        popupNoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hidePopup();
            }
        });

        popupYesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                popupRunnable.run();
                hidePopup();
            }
        });
    }

    public void showPopup(String message, Runnable runnable) {
        if (popupWindow.isVisible() || message.isEmpty()) return;

        closeAllUiMenus();

        popupWindow.setSize(800, 500);
        popupWindow.setPosition(
                uiStage.getWidth() / 2f,
                uiStage.getHeight() / 2f,
                Align.center
        );

        popupRunnable = runnable;

        popupText.setText(message);
        popupWindow.getColor().a = 1;
        popupWindow.setVisible(true);

        Gdx.input.setInputProcessor(uiStage);
    }

    public void hidePopup() {
        popupWindow.addAction(Actions.sequence(
                Actions.fadeOut(0.3f),
                Actions.visible(false)
        ));
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
    }


    private void setDisableButton(TextButton button, boolean disable) {
        button.setDisabled(disable);
        if (disable) {
            button.setColor(Color.GRAY);
        } else {
            button.setColor(Color.WHITE);
        }
    }

    public void loadAnimalInfoPopup() {
        animalWindow = new Window("", GameAssetManager.skin);
        animalWindow.setVisible(false);
        animalWindow.setModal(true);

        Label animalHeader = new Label("Animal Info", GameAssetManager.skin);
        animalHeader.setAlignment(Align.center);
        animalWindow.add(animalHeader).colspan(2).row();

        Label animalNameLabel = new Label("Name : ", GameAssetManager.skin);
        animalWindow.add(animalNameLabel).padRight(15);

        animalName = new Label("", GameAssetManager.skin);
        animalWindow.add(animalName).padRight(15).row();

        animalImage = new Image();
        animalWindow.add(animalImage).padRight(15).size(100, 100).colspan(2).row();

        TextButton petAnimal = new TextButton("Pet", GameAssetManager.skin);
        getProductsButton = new TextButton("Get Products", GameAssetManager.skin);
        setDisableButton(getProductsButton, true);
        feedAnimalButton = new TextButton("Feed", GameAssetManager.skin);
        setDisableButton(feedAnimalButton, true);
        shepherdAnimalButton = new TextButton("Shepherd Animal", GameAssetManager.skin);
        TextButton sell = new TextButton("Sell !!", GameAssetManager.skin);
        TextButton back = new TextButton("Back", GameAssetManager.skin);

        int buttonSize = 300;
        animalWindow.add(petAnimal).pad(20).colspan(2).width(buttonSize).row();
        animalWindow.add(getProductsButton).pad(20).colspan(2).width(buttonSize).row();
        animalWindow.add(feedAnimalButton).pad(20).colspan(2).width(buttonSize).row();
        animalWindow.add(sell).pad(20).colspan(2).width(buttonSize).row();
        animalWindow.add(back).padTop(35).colspan(2).width(buttonSize).row();

        animalWindow.pack();
        animalWindow.setVisible(false);

        uiStage.addActor(animalWindow);

        petAnimal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAnimalInfoPopup();
            }
        });
        getProductsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAnimalInfoPopup();
            }
        });
        feedAnimalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAnimalInfoPopup();
            }
        });
        shepherdAnimalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAnimalInfoPopup();
            }
        });
        sell.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.sellAnimal(animalToggled);
            }
        });
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideAnimalInfoPopup();
            }
        });

    }

    public void showAnimalInfoPopup(Animal animal) {
        closeAllUiMenus();

        if (animalWindow.isVisible()) hideAnimalInfoPopup();

        animalWindow.setSize(500, 1000);
        animalWindow.setPosition(
                uiStage.getWidth() / 2f,
                uiStage.getHeight() / 2f,
                Align.center
        );

        animalToggled = animal;
        animalName.setText(animal.getName());
        animalImage.setDrawable(new TextureRegionDrawable(animal.getTexture()));

        setDisableButton(getProductsButton, !animal.hasProductReady());

        boolean hasFeed = game.getPlayerInTurn().getInventory().hasEnoughStack("Hay", 1);
        setDisableButton(feedAnimalButton, !hasFeed);

        setDisableButton(shepherdAnimalButton, !animal.isOutsideToday());

        animalWindow.getColor().a = 1;
        animalWindow.setVisible(true);

        Gdx.input.setInputProcessor(uiStage);
    }

    public void hideAnimalInfoPopup() {
        animalWindow.setVisible(false);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
    }


    public void loadTextures() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("characters/woman/woman.atlas"));
        starTexture = new Texture(Gdx.files.internal("inventory/Achievement_Star_06.png"));
        clock = new Texture(Gdx.files.internal("clock/Clock.png"));
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

        // DOWN
        Array<TextureRegion> walkDown = new Array<>();
        for (int i = 0; i < 3; i++) {
            TextureRegion r = playerAtlas.findRegion("woman-move_ down", i);
            if (r == null) System.out.println("[Null region] woman-move_ down, " + i);
            walkDown.add(r);
        }
        animations.add(new Animation<>(0.15f, walkDown, Animation.PlayMode.LOOP));

        // RIGHT
        Array<TextureRegion> walkRight = new Array<>();
        for (int i = 0; i < 3; i++) {
            TextureRegion r = playerAtlas.findRegion("woman-move_ right", i);
            if (r == null) System.out.println("[Null region] woman-move_ right, " + i);
            walkRight.add(r);
        }
        animations.add(new Animation<>(0.15f, walkRight, Animation.PlayMode.LOOP));

        // UP
        Array<TextureRegion> walkUp = new Array<>();
        for (int i = 0; i < 3; i++) {
            TextureRegion r = playerAtlas.findRegion("woman-move_ up", i);
            if (r == null) System.out.println("[Null region] woman-move_ up, " + i);
            walkUp.add(r);
        }
        animations.add(new Animation<>(0.15f, walkUp, Animation.PlayMode.LOOP));

        // LEFT
        Array<TextureRegion> walkLeft = new Array<>();
        for (int i = 0; i < 3; i++) {
            TextureRegion r = playerAtlas.findRegion("woman-move_left", i); // بدون فاصله
            if (r == null) System.out.println("[Null region] woman-move_left, " + i);
            walkLeft.add(r);
        }
        animations.add(new Animation<>(0.15f, walkLeft, Animation.PlayMode.LOOP));

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


    private Color getSeasonTintColor() {
        switch (game.getTime().getSeason()) {
            case SPRING:
                // #D1E05D
                return new Color(0.82f, 0.88f, 0.36f, 1f);
            case SUMMER:
                // #F6FF49
                return new Color(0.96f, 1f, 0.28f, 1f);
            case FALL:
                // #FFC481
                return new Color(1f, 0.77f, 0.51f, 1f);
            case WINTER:
                // #b8ffeb
                return new Color(0.72f, 1f, 0.92f, 1f);
            default:
                return Color.WHITE; // رنگ عادی و بدون تغییر
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

        int startX = Math.max(0, (int) (cameraLeft / tileSize));
        int startY = Math.max(0, (int) (cameraBottom / tileSize));
        int endX = Math.min(Constants.WORLD_MAP_WIDTH, startX + (int) (viewportWidth / tileSize) + 2);
        int endY = Math.min(Constants.WORLD_MAP_HEIGHT, startY + (int) (viewportHeight / tileSize) + 2);

        Tile[][] tiles = game.getMap().getTiles();

        //Tiles
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int rowIndex = Constants.WORLD_MAP_HEIGHT - 1 - y;

                if (rowIndex < 0 || rowIndex >= Constants.WORLD_MAP_HEIGHT) continue;
                Tile tile = tiles[rowIndex][x];

                Location l = new Location(x, y);
                TextureRegion texture = tileCache.get(l);
                TileType type = tileTypeCache.get(l);

                if (type == null) {
                    type = tile.getType();
                    tileTypeCache.put(l, type);
                }

                if (texture == null || type != tile.getType()) {
                    texture = tile.getTexture();
                    tileCache.put(l, texture);
                }

                float drawX = x * tileSize;
                float drawY = y * tileSize;

                Color tileColor = new Color(1, 1, 1, 1);
                Color seasonTint = getSeasonTintColor();

                if (tile.isPlowed() && tile.isWatered()) {
                    batch.setColor(0.35f, 0.25f, 0.2f, 1f);
                } else if (tile.isPlowed()) {
                    batch.setColor(0.4f, 0.25f, 0.1f, 1f);
                } else if (tile.isWatered()) {
                    batch.setColor(0.75f, 0.75f, 0.75f, 1f);
                } else {
                    batch.setColor(1f, 1f, 1f, 1f);
                }

                batch.setColor(tileColor.mul(seasonTint));
                batch.draw(texture, drawX, drawY, tileSize, tileSize);
            }
        }

        //Obj on tiles
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


    public void renderBuildings() {
        for (Player player : game.getPlayers()) {
            for (Building building : player.getFarmBuildings()) {
                if (building instanceof ShippingBin) {
                    Location inMapLocation = Map.TileToPixelConverter(building.getLocation());
                    int tileSize = Map.TILE_SIZE;

                    TextureRegion texture = new TextureRegion(GameAssetManager.shippingBinTexture);
                    batch.draw(texture, inMapLocation.x(), inMapLocation.y());
                }
            }
        }
    }

    public void renderAnimals(float v) {
        for (Player player : game.getPlayers()) {
            for (Animal animal : player.getAnimals()) {
                animal.updateMovement(v, game);

                TextureRegion texture = animal.getTexture();
                int tileSize = Map.TILE_SIZE;
                batch.draw(texture, animal.getX(), animal.getY(), tileSize, tileSize);
            }
        }
    }


    /// test
    public void cheatPlayer() {
        Player player = game.getPlayerInTurn();
        Location pLoc = player.getTileLocation();
        player.addToBuildings(new AnimalBuilding("coop", new Location(pLoc.x() + 1, pLoc.y() + 1), 7, 4, LivingPlace.COOP));
        AnimalBuilding building = player.getAnimalBuilding(LivingPlace.COOP);
        building.updateMap(game.getMap());

        Animal animal = new Animal(AnimalType.CHICKEN, "joojeh", 500, LivingPlace.COOP, new ArrayList<>());
        player.addAnimal(animal);
        building.addAnimalAndSetLocationInside(animal);
        showError("cheated Animal");
    }

    /// end of test

    public void renderPlayers(float data) {
        for (Player player : game.getPlayers()) {
            renderPlayer(player, data);
        }
    }

    private void renderPlayer(Player currentPlayer, float delta) {
        int animIndex = switch (currentPlayer.getDirection()) {
            case DOWN -> 0;
            case RIGHT -> 1;
            case UP -> 2;
            case LEFT -> 3;
            default -> 0;
        };

        batch.setColor(currentPlayer.getColor());

        String equippedTool = getEquippedToolKey(currentPlayer);

        TextureRegion currentFrame = null;
        if (equippedTool == null) {
            Animation<TextureRegion> currentAnimation = playersAnimations.get(currentPlayer).get(animIndex);
            if (currentPlayer.isMoving()) {
                currentFrame = currentAnimation.getKeyFrame(stateTime, true);
            } else {
                currentFrame = currentAnimation.getKeyFrame(0f);
            }
        } else {
            String[] toolRegions = {
                    "woman-" + equippedTool + "_down",
                    "woman-" + equippedTool + "_right",
                    "woman-" + equippedTool + "_up",
                    "woman-" + equippedTool + "_left"
            };
            currentFrame = playerAtlas.findRegion(toolRegions[animIndex]);
            if (currentFrame == null) {
                System.out.println("[NULL tool region] " + toolRegions[animIndex]);
            }
        }

        if (currentFrame == null) return;

        float drawX = currentPlayer.getX() - (Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_W) / 2f;
        float drawY = currentPlayer.getY();
        float scale = 0.8f;
        batch.draw(currentFrame, drawX, drawY, currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        batch.setColor(Color.WHITE);
    }

    private void renderCamera() {
        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }


    // mini map window
    public void toggleBiggerMiniMap() {
        if (bigMinimapWindow != null) {
            bigMinimapWindow.remove();
            bigMinimapWindow = null;
            minimapCell.setActor(miniMapWidget);
            return;
        }

        minimapCell.setActor(null);

        bigMinimapWindow = new Window("", GameAssetManager.skin);
        bigMinimapWindow.setMovable(true);
        bigMinimapWindow.setResizable(true);

        bigMinimapWindow.add(miniMapWidget).size(1000, 750);
        bigMinimapWindow.pack(); // اندازه پنجره را تنظیم کن

        bigMinimapWindow.setPosition(uiStage.getWidth() / 2, uiStage.getHeight() / 2, Align.center);
        uiStage.addActor(bigMinimapWindow);
    }

    private void renderClockUI() {
        float clockWidth = clock.getWidth();
        float clockHeight = clock.getHeight();

        float drawX = camera.position.x + (camera.viewportWidth / 2) - clockWidth - 20;
        float drawY = camera.position.y + (camera.viewportHeight / 2) - clockHeight - 20;


        batch.draw(clock, drawX, drawY);
        Time time = App.getApp().getCurrentGame().getTime();

        batch.draw(seasonTexture, drawX + 210, drawY + 137, (float) seasonTexture.getRegionWidth() / 2, (float) seasonTexture.getRegionHeight() / 2);
        batch.draw(weatherTexture, drawX + 115, drawY + 137, (float) weatherTexture.getRegionWidth() / 2, (float) weatherTexture.getRegionHeight() / 2);

        String date = (time.getDayOfWeek().toString().substring(0, 3)) + ". " + time.getDay();
        font.draw(batch, date, drawX + 150, drawY + 210);
        font.draw(batch, time.getHourText(), drawX + 150, drawY + 120);
        font.draw(batch, String.valueOf(App.getApp().getCurrentGame().getPlayerInTurn().getMoney()), drawX + 200, drawY + 40);
    }

    public void changeCookingMenu() {
        cookingMenuOpen = !cookingMenuOpen;
        cookingMenuTable.setVisible(cookingMenuOpen);
        if (cookingMenuOpen) {
            Gdx.input.setInputProcessor(uiStage);
            Player player = game.getPlayerInTurn();
            ArrayList<FoodRecipe> recipes = new ArrayList<>(java.util.List.of(FoodRecipe.values()));
            for (int i = 0; i < cookingMenuButtons.size(); i++) {
                cookingMenuButtons.get(i).setDisabled(!player.hasLearnedFoodRecipe(recipes.get(i)));
            }
        } else {
            Gdx.input.setInputProcessor(gameMenuInputAdapter);
        }
    }

    public void prepareFoodMenu(Skin skin) {
        cookingMenuTable = new Table(skin);
        cookingMenuTable.setFillParent(true);
        cookingMenuTable.setVisible(false);
        cookingMenuTable.setBackground(skin.getDrawable("window")); // Use window background from atlas
        ScrollPane scrollPane = new ScrollPane(cookingMenuTable, skin);
        scrollPane.setSize(720, 1080);
        scrollPane.setPosition((uiStage.getWidth() - cookingMenuTable.getWidth()) / 2,
                (uiStage.getHeight() - cookingMenuTable.getHeight()) / 2, Align.center);

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label titleLabel = new Label("Cooking Menu", labelStyle);
        cookingMenuTable.add(titleLabel).pad(10).colspan(3).center();
        cookingMenuTable.row();

        Player player = App.getApp().getCurrentGame().getPlayerInTurn();
        for (FoodRecipe recipe : FoodRecipe.values()) {
            Image image = new Image(recipe.data.getTexture());
            Label nameLabel = new Label(recipe.name(), labelStyle);
            TextButton cookButton = new TextButton("Cook", skin);

            cookingMenuButtons.add(cookButton);

            cookingMenuTable.add(image).size(32, 32).pad(5);
            cookingMenuTable.add(nameLabel).left().pad(5);
            cookingMenuTable.add(cookButton).pad(5);

            cookButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.cook(recipe);
                }
            });
            cookingMenuTable.row();
        }
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeCookingMenu();
            }
        });
        cookingMenuTable.add(backButton).pad(5);

        uiStage.addActor(scrollPane);

    }

    public void changeCraftingMenu() {
        craftingMenuOpen = !craftingMenuOpen;
        craftingMenu.setVisible(craftingMenuOpen);
        if (craftingMenuOpen) {
            Gdx.input.setInputProcessor(uiStage);
            for (CraftingWidget widget : craftingWidgets) {
                widget.update(App.getApp().getCurrentGame().getPlayerInTurn());
            }
        } else {
            Gdx.input.setInputProcessor(gameMenuInputAdapter);
        }
    }

    public void prepareCraftingMenu(Skin skin) {
        craftingMenu = new Table();
        craftingMenu.setVisible(false);
        craftingMenu.setBackground(skin.getDrawable("window"));


        ScrollPane scrollPane = new ScrollPane(craftingMenu, skin);
        scrollPane.setSize(700, 700);
        scrollPane.setPosition((uiStage.getWidth() - craftingMenu.getWidth()) / 2,
                (uiStage.getHeight() - craftingMenu.getHeight()) / 2, Align.center);

        int perRow = 3;
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            CraftingWidget craftingWidget = new CraftingWidget(recipe, App.getApp().getCurrentGame().getPlayerInTurn(), skin, controller);
            craftingWidgets.add(craftingWidget);
            craftingMenu.add(craftingWidget).pad(10);
            if (perRow == 0) {
                craftingMenu.row();
                perRow = 3;
            } else {
                perRow--;
            }
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeCraftingMenu();
            }
        });
        craftingMenu.add(backButton).pad(5);

        uiStage.addActor(scrollPane);

    }

    public void showShopMenu() {
        Player player = game.getPlayerInTurn();
        Shop shop = game.getShopPlayerIsIn(player);
        if (shop == null) {
            showError("You aren't in a shop");
        } else {
            Gdx.input.setInputProcessor(uiStage);
            shop.showShopMenu(uiStage, GameAssetManager.skin);
        }
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

        if (player.getTurnEnergy() < 10) {
            showError("Your turn energy : " + (int) Math.ceil(player.getTurnEnergy()) + " !");
        }

        if (player.getTurnEnergy() <= 0 && player.getCurrentState() != PlayerState.Unconscious) {
            // در صورت غیر فعال بودن، چند بار nextTurn میشه
            player.setCurrentState(PlayerState.Unconscious);

            showError("You are not conscious now... going to sleep");

            delayForAndDo(1f, () -> blackBackgroundAnimation(() -> {
                        controller.changeTurn();
                    }, 0.3f)
            );
        }
    }

    // inventory

    private void renderInventory() {
        inventoryTable.clear();

        Player player = game.getPlayerInTurn();
        Inventory inventory = player.getInventory();
        int numSlots = player.getMaxInventorySize();

        for (int i = 0; i < numSlots; i++) {
            Stack slotStack = new Stack();

            Image slotBg = new Image(new Texture("inventory/Mail.2jpg.jpg"));
            slotStack.add(slotBg);

            if (i < inventory.getInventoryItems().size() && inventory.getInventoryItems().get(i) != null) {
                TextureRegionDrawable itemDrawable = new TextureRegionDrawable(inventory.getInventoryItems().get(i).getItem().getTexture());
                Image itemImg = new Image(itemDrawable);
                slotStack.add(itemImg);

                int quantity = inventory.getInventoryItems().get(i).getAmount();
                Label countLabel = new Label(String.valueOf(quantity), GameAssetManager.skin);
                countLabel.setFontScale(1f);
                slotStack.add(countLabel);
            }

            if (i == player.getSelectedSlot()) {
                Image highlight = new Image(new Texture("inventory/Mail3.jpg"));
                slotStack.add(highlight);

                if (i < inventory.getInventoryItems().size() && inventory.getInventoryItems().get(i) != null) {
                    TextureRegionDrawable itemDrawable = new TextureRegionDrawable(inventory.getInventoryItems()
                            .get(i).getItem().getTexture());
                    Image itemImg = new Image(itemDrawable);
                    slotStack.add(itemImg);
                }

            }

            inventoryTable.add(slotStack).size(60, 60);
        }
    }

    private String getEquippedToolKey(Player player) {
        Inventory inventory = player.getInventory();
        int selectedSlot = player.getSelectedSlot();
        ArrayList<ItemStack> items = inventory.getInventoryItems();
        if (selectedSlot >= 0 && selectedSlot < items.size()) {
            Item selectedItem = items.get(selectedSlot).getItem();
            if (selectedItem != null) {
                String name = selectedItem.getName().toLowerCase();
                if (name.contains("scythe")) return "scythe";
                if (name.equals("axe")) return "axe";
                if (name.contains("water")) return "water";
            }
        }
        return null;
    }

    public void toggleInventoryMenu() {
        if (inventoryWindow != null) {
            hideInventoryMenu();
            return;
        }
        Skin skin = GameAssetManager.skin;
        inventoryWindow = new Window("Inventory", skin);
        inventoryWindow.setModal(true);
        inventoryWindow.setSize(1356, 1000);
        inventoryWindow.setPosition(
                uiStage.getWidth() / 2f,
                uiStage.getHeight() / 2f,
                Align.center
        );

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // ==== ردیف اول: دکمه‌های تب ====
        String[] tabNames = {"Journal", "Inventory", "Skills", "Map", "Setting", "Social"};
        Table tabsRow = new Table();

        // رو این دکمه‌ها اکشن تعویض محتوا می‌ذاریم:
        for (String tab : tabNames) {
            TextButton tabBtn = new TextButton(tab, skin);
            tabsRow.add(tabBtn).pad(5);

            tabBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    contentCell.setActor(null);
                    switch (tab) {
                        case "Journal":
                            contentCell.setActor(getQuestsList());
                            break;
                        case "Inventory":
                            contentCell.setActor(getInventoryContentTable());
                            break;
                        case "Skills":
                            contentCell.setActor(getSkillsMenuTable());
                            break;
                        case "Map":
                            contentCell.setActor(getMapMenuTable());
                            break;
                        case "Setting":
                            contentCell.setActor(getSettingsTable());
                            break;
                        case "Social":
                            contentCell.setActor(getSocialMenuTable(game.getMainPlayer()));
                            break;
                        default:
                            Label comingSoon = new Label(tab + " content coming soon!", skin);
                            comingSoon.setAlignment(Align.center);
                            contentCell.setActor(comingSoon);
                    }
                }
            });
        }
        mainTable.add(tabsRow).growX().padTop(35).row();

        // ==== ردیف وسط: محتوای تب جاری ====
        contentCell = mainTable.add(getQuestsList()).expand().fill();
        mainTable.row();

        // ==== ردیف آخر: دکمه Close پایین ====
        TextButton closeBtn = new TextButton("Close", skin);
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideInventoryMenu();
            }
        });
        Table closeRow = new Table();
        closeRow.add(closeBtn).center().padBottom(12);
        mainTable.add(closeRow).growX().bottom().row();

        inventoryWindow.clearChildren();
        inventoryWindow.add(mainTable).grow().pad(8);

        uiStage.addActor(inventoryWindow);
        Gdx.input.setInputProcessor(uiStage);
    }

    private void hideInventoryMenu() {
        if (inventoryWindow != null) {
            inventoryWindow.remove();
            inventoryWindow = null;
            Gdx.input.setInputProcessor(gameMenuInputAdapter);
        }
    }

    private Table getQuestsList() {
        Table questsTable = new Table();
        Skin skin = GameAssetManager.skin;

        Label title = new Label("Journal - Quests", skin, "title");
        title.setAlignment(Align.center);
        title.setFontScale(1f);

        questsTable.add(title).colspan(2).padBottom(32).center().row();

        // NPC names & quests
        String[] npcs = {"Abigail", "Harvey", "Leah", "Robin", "Sebastian"};
        String[] colors = {"e63946", "f1faee", "a8dadc", "457b9d", "1d3557"};

        for (int i = 0; i < npcs.length; i++) {
            Label nameLabel = new Label(npcs[i] + ":", skin, "subtitle");
            nameLabel.setColor(Color.valueOf(colors[i]));
            nameLabel.setFontScale(0.9f);

            Label questsLabel = new Label(controller.getQuesList(npcs[i].toLowerCase()), skin);
            questsLabel.setWrap(true);
            questsLabel.setColor(Color.valueOf(colors[i]));
            questsLabel.setFontScale(0.9f);

            questsTable.add(nameLabel).padRight(50).top().left().width(170);
            questsTable.add(questsLabel).growX().padBottom(30).padTop(10).left().row();
        }

        questsTable.pad(30, 20, 30, 20).top().left();
        return questsTable;
    }

    private Table getInventoryTable() {
        Table inventoryGrid = new Table();
        Skin skin = GameAssetManager.skin;

        Player player = game.getPlayerInTurn();
        Inventory inventory = player.getInventory();
        int numSlots = player.getMaxInventorySize();
        ArrayList<ItemStack> items = inventory.getInventoryItems();

        final float slotSize = 82f;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int i = row * 10 + col;

                Stack slotStack = new Stack();

                Image slotBg = new Image(GameAssetManager.inventorySlot);
                slotBg.setColor(Color.WHITE);
                slotStack.add(slotBg);

                if (i < items.size() && items.get(i) != null && items.get(i).getItem() != null) {
                    TextureRegion itemTex = items.get(i).getItem().getTexture();
                    Image itemImg = new Image(new TextureRegionDrawable(itemTex));
                    slotStack.add(itemImg);

                    int quantity = items.get(i).getAmount();
                    if (quantity > 1) {
                        Table countTable = new Table();
                        Label lbl = new Label(String.valueOf(quantity), skin);
                        lbl.setFontScale(0.74f);
                        lbl.setColor(Color.GOLD);
                        countTable.add(lbl).bottom().center().padBottom(4);
                        countTable.setFillParent(true);
                        countTable.bottom();
                        slotStack.add(countTable);
                    }
                }

                if (i == player.getSelectedSlot()) {
                    Image highlight = new Image(GameAssetManager.inventoryHighlightSlot);
                    highlight.setColor(new Color(1, 1, 1, 0.41f));
                    slotStack.add(highlight);
                }

                final int slotIndex = i;
                slotStack.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        player.setSelectedSlot(slotIndex);
                        updateInventoryContent();
                        return true;
                    }
                });

                inventoryGrid.add(slotStack).size(slotSize, slotSize).pad(4);
            }
            inventoryGrid.row();
        }

        return inventoryGrid;
    }

    private Table getInventoryContentTable() {
        Skin skin = GameAssetManager.skin;

        Image trashImg = new Image(new Texture("inventory/stardewmoddingapi_ou7895mbeu.png"));
        trashImg.setScaling(Scaling.fit);
        trashImg.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Player player = game.getPlayerInTurn();
                int idx = player.getSelectedSlot();
                Inventory inv = player.getInventory();
                ArrayList<ItemStack> items = inv.getInventoryItems();

                if (idx >= 0 && idx < items.size() && items.get(idx) != null) {
                    pendingTrashSlot = idx;
                }

                return true;
            }
        });

        Table inventoryMenuTable = new Table(skin);

        Table gridTable = getInventoryTable();
        ScrollPane scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollPercentY(0); // اول جدول

        float slotSize = 82 + 8;
        scrollPane.setHeight(2 * slotSize);

        // ---- لیبل بالای جدول ----
        Label titleLabel = new Label("Inventory", skin, "title");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.16f);
        inventoryMenuTable.add(titleLabel).center().padBottom(28).row();

        Table toolsRow = new Table();
        toolsRow.add(scrollPane).width(944).height(2 * slotSize).padRight(14); // 10*82 + پدها
        toolsRow.add(trashImg).size(55, 55).center();

        inventoryMenuTable.add(toolsRow).center().padTop(33);
        inventoryMenuTable.row();

        return inventoryMenuTable;
    }

    private void updateInventoryContent() {
        if (contentCell != null) {
            contentCell.setActor(getInventoryContentTable());
        }
    }

    private Table getSkillsMenuTable() {
        Skin skin = GameAssetManager.skin;
        Player player = game.getPlayerInTurn();

        Table skillsTable = new Table(skin);

        // عنوان
        Label title = new Label("Skills", skin, "title");
        //title.setAlignment(Align.center);
        title.setFontScale(1.15f);
        skillsTable.add(title).padBottom(36).center().colspan(2).padLeft(700).row();

        // اسامی و آیکون و عکس توضیح هر مهارت
        String[] skills = {"Farming", "Fishing", "Mining", "Foraging"};
        String[] skillIcons = {
                GameAssetManager.farmingSkillName,
                GameAssetManager.fishingSkillName,
                GameAssetManager.miningSkillName,
                GameAssetManager.foragingSkillName
        };
        String[] skillDescImgs = {
                "inventory/farming.png",
                "inventory/fishing.png",
                "inventory/mining.png",
                "inventory/foraging.png"
        };
        int[] skillLevels = {
                player.getSkills().getFarmingLevel(),
                player.getSkills().getFishingLevel(),
                player.getSkills().getMiningLevel(),
                player.getSkills().getForagingLevel()
        };
        // تصاویر ستاره برای همه مهارت‌ها مشترک
        String[] starImages = {
                GameAssetManager.star1Name,
                GameAssetManager.star2Name,
                GameAssetManager.star3Name,
                GameAssetManager.star4Name
        };

        String[] skillColors = {
                "#B2E672", // Farming
                "#7DD1F4", // Fishing
                "#DDBEEA", // Mining
                "#FECC5C"  // Foraging
        };

        for (int i = 0; i < skills.length; i++) {
            Table row = new Table(skin);

            // آیکون مهارت
            Image skillIcon = new Image(new TextureRegion(new Texture(skillIcons[i])));
            skillIcon.setSize(64, 64);

            // عکس توضیح مهارت مخصوص همون اسکیل
            final TextureRegion descRegion = new TextureRegion(new Texture(skillDescImgs[i]));
            skillIcon.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    showSkillImageTooltip(descRegion, skillIcon);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    hideSkillImageTooltip();
                }
            });

            row.add(skillIcon).size(64, 64).padRight(18);

            // عنوان
            Label lbl = new Label(skills[i], skin, "title");
            lbl.setColor(Color.valueOf(skillColors[i]));
            lbl.setFontScale(0.5f);
            row.add(lbl).padRight(40);

            // ستاره‌ها (نمایش فقط لول‌هایی که باز شده)
            Table stars = new Table();
            int level = skillLevels[i];
            for (int lv = 0; lv < 4; lv++) {
                if (lv < level) {
                    Image star = new Image(new TextureRegion(new Texture(starImages[lv])));
                    star.setSize(28, 28);
                    stars.add(star).pad(2);
                }
            }
            row.add(stars).padLeft(24);

            skillsTable.add(row).padBottom(32).left().row();
        }

        skillsTable.top().pad(40, 36, 30, 36).left();
        return skillsTable;
    }

    private void showSkillImageTooltip(TextureRegion region, Actor icon) {
        if (skillDescImage == null) {
            skillDescImage = new Image(region);
            uiStage.addActor(skillDescImage);
        } else {
            skillDescImage.setDrawable(new TextureRegionDrawable(region));
            skillDescImage.setVisible(true);
        }
        // اندازه سه برابر آیکون مهارت
        skillDescImage.setSize(420, 210);
        // موقعیت سمت چپ آیکون
        float tipX = icon.localToStageCoordinates(new Vector2(-icon.getWidth() * 3 - 135, 0)).x;
        float tipY = icon.localToStageCoordinates(new Vector2(0, 50)).y;
        skillDescImage.setPosition(tipX, tipY);
        skillDescImage.toFront();
        skillDescImage.setVisible(true);
    }

    private void hideSkillImageTooltip() {
        if (skillDescImage != null)
            skillDescImage.setVisible(false);
    }

    private Table getMapMenuTable() {
        Skin skin = GameAssetManager.skin;
        Table mapTable = new Table(skin);

        Label title = new Label("World Map", skin, "title");
        title.setAlignment(Align.center);
        title.setFontScale(1.18f);

        mapTable.add(title).growX().height(70).padBottom(22).center().row();

        mapTable.add(miniMapWidget).size(720, 540).center().row();

        mapTable.pad(36, 36, 36, 36).center();
        return mapTable;
    }

    private Table getSettingsTable() {
        Skin skin = GameAssetManager.skin;
        Table settingsTable = new Table(skin);

        Label title = new Label("Settings", skin, "title");
        title.setAlignment(Align.center);
        title.setFontScale(1.18f);

        // ------------------------------
        Label changePlayer = new Label("Change Player :", skin);
        changePlayer.setFontScale(1.1f);
        changePlayer.setColor(Color.valueOf("9A8C98"));

        TextField changePlayerField = new TextField("", skin);
        changePlayerField.setMessageText("Enter new player username...");
        changePlayerField.setMaxLength(20);

        TextButton changePlayerButton = new TextButton("Change Player", skin);
        changePlayerButton.setColor(Color.valueOf("E9D8A6"));

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setColor(Color.valueOf("E9D8A6"));
        // ------------------------------
        settingsTable.pad(36);
        settingsTable.add(title).colspan(2).growX().height(70).padBottom(100).center().row();

        settingsTable.add(changePlayer).left().padRight(12).width(180).height(38).row();
        settingsTable.add(changePlayerField).left().width(800).row();

        settingsTable.add(changePlayerButton).left().padTop(35).padRight(50).row();

        settingsTable.add(exitButton).colspan(2).padTop(70).row();
        // ------------------------------
        changePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String newName = changePlayerField.getText().trim();
                if (controller.isUsernameExist(newName)) {
                    User newUser = controller.getUserByUsername(newName);
                    if (newUser.getCurrentGame() == null) {
                        newUser.setCurrentGame(game);
                    }
                }
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Main.getMain().switchScreen(new MainMenuScreen());
            }
        });

        return settingsTable;
    }

    private Table getSocialMenuTable(Player player) {

        Skin skin = GameAssetManager.skin;
        Table socialTable = new Table(skin);

        Label title = new Label("Social", skin, "title");
        title.setAlignment(Align.center);
        title.setFontScale(1.18f);
        socialTable.add(title).growX().height(70).padBottom(25).center().row();

        // بخش دوستان (بازیکنان)
        Label friendsLabel = new Label("Friends:", skin, "subtitle");
        friendsLabel.setFontScale(1.1f);
        friendsLabel.setColor(Color.valueOf("7b81a9"));
        socialTable.add(friendsLabel).left().padBottom(12).padTop(10).row();

        ArrayList<Player> otherPlayers = game.getOtherPlayers(player.getUsername());
        for (Player otherPlayer : otherPlayers) {
            socialTable.add(getSocialRow(otherPlayer.getUsername(), friendPlayers.get(otherPlayer.getUsername()))).left().padBottom(8).row();
        }

        // بخش NPC ها
        Label npcsLabel = new Label("NPCs:", skin, "subtitle");
        npcsLabel.setFontScale(1.1f);
        npcsLabel.setColor(Color.valueOf("BA9B82"));
        socialTable.add(npcsLabel).left().padBottom(8).row();

        ArrayList<PlayerNPCInteraction> friendships = player.getAllFriendships();
        for (String npc : npcNames) {
            int level = 0;
            for (PlayerNPCInteraction f : friendships) {
                if (f.getNPCName().equalsIgnoreCase(npc)) {
                    level = f.getFriendshipLevel();
                }
            }
            socialTable.add(getSocialRow(npc, level)).left().padBottom(12).row();
        }

        socialTable.pad(25, 45, 35, 45).center();
        return socialTable;
    }

    private Table getSocialRow(String name, int level) {
        Table row = new Table();
        Skin skin = GameAssetManager.skin;

        Label nameLabel = new Label(name, skin);
        nameLabel.setFontScale(1.05f);
        nameLabel.setColor(Color.valueOf("e9edc9"));

        row.add(nameLabel).width(140).padRight(15).left();

        for (int i = 0; i < level; i++) {
            row.add(new Image(starTexture)).size(24, 24).pad(2);
        }
        for (int i = level; i < 5; i++) {
            row.add().size(24, 24).pad(2);
        }
        return row;
    }

    public HashMap<String, Integer> getFriendshipLevel() {
        Player player = game.getPlayerInTurn();
        ArrayList<Player> otherPlayers = game.getOtherPlayers(player.getUsername());
        HashMap<String, Integer> friendshipLevel = new HashMap<>();
        for (Player otherPlayer : otherPlayers) {
            friendshipLevel.put(otherPlayer.getUsername(), game.getFriendship(player, otherPlayer).getFriendshipLevel());
        }
        return friendshipLevel;
    }

    // NPC
    private void renderNPCs(SpriteBatch batch) {
        renderNPC(batch, npcAbigail, abigailLocation.x(), abigailLocation.y(), "abigail");
        renderNPC(batch, npcHarvey, harveyLocation.x(), harveyLocation.y(), "harvey");
        renderNPC(batch, npcLeah, leahLocation.x(), leahLocation.y(), "leah");
        renderNPC(batch, npcRobin, robinLocation.x(), robinLocation.y(), "robin");
        renderNPC(batch, npcSebastian, sebastianLocation.x(), sebastianLocation.y(), "sebastian");
    }

    private void renderNPC(SpriteBatch batch, Texture npcTexture, float x, float y, String id) {
        // رسم خود NPC
        float scale = 3.5f;
        batch.draw(npcTexture, x, y, npcTexture.getWidth() * scale, npcTexture.getHeight() * scale);


        float cloudX = x;
        float cloudY = y + npcTexture.getHeight() * scale + 10;
        float scale2 = 3f;
        if (!npcDialogVisible.get(id)) {

            batch.draw(speechCloudTexture, cloudX, cloudY, speechCloudTexture.getWidth() * scale2,
                    speechCloudTexture.getHeight() * scale2);
        } else {
            String condition = controller.getConditions(game.getTime(), game.getPlayerInTurn().getFriendship(id), game.getTodayWeather());
            String text = controller.getDialogueByConditions(condition, id, game);
            BitmapFont font = smallFont;
            font.setColor(Color.BLACK);
            GlyphLayout layout = new GlyphLayout(font, text);

            float padding = 10f;
            float bubbleWidth = layout.width + padding * 2;
            float bubbleHeight = layout.height + padding * 2;

            float bubbleX = cloudX;
            float bubbleY = cloudY;

            batch.draw(speechBubbleTexture, bubbleX, bubbleY, bubbleWidth, bubbleHeight * 1.2f);
            font.draw(batch, layout, bubbleX + padding, bubbleY + bubbleHeight - 5f);
        }

        if (npcHeartVisible.get(id)) {
            batch.draw(heartBubbleTexture, cloudX, cloudY, speechCloudTexture.getWidth() * scale2,
                    speechCloudTexture.getHeight() * scale2);
        }
    }

    public void toggleNpcDialog(String npcId) {
        npcDialogVisible.put(npcId, !npcDialogVisible.get(npcId));
    }

    public float getNPCx(String npcId) {
        return switch (npcId) {
            case "abigail" -> abigailLocation.x();
            case "harvey" -> harveyLocation.x();
            case "leah" -> leahLocation.x();
            case "robin" -> robinLocation.x();
            case "sebastian" -> sebastianLocation.x();
            default -> 0;
        };
    }

    public float getNPCy(String npcId) {
        return switch (npcId) {
            case "abigail" -> abigailLocation.y();
            case "harvey" -> harveyLocation.y();
            case "leah" -> leahLocation.y();
            case "robin" -> robinLocation.y();
            case "sebastian" -> sebastianLocation.y();
            default -> 0;
        };
    }

    public Texture getNPCTexture(String npcId) {
        return switch (npcId) {
            case "abigail" -> npcAbigail;
            case "harvey" -> npcHarvey;
            case "leah" -> npcLeah;
            case "robin" -> npcRobin;
            case "sebastian" -> npcSebastian;
            default -> null;
        };
    }

    public boolean isDialogVisible(String npcId) {
        return npcDialogVisible.get(npcId);
    }

    public String getNpcDialogText(String npcId) {
        String condition = controller.getConditions(game.getTime(), game.getPlayerInTurn().getFriendship(npcId), game.getTodayWeather());
        return controller.getDialogueByConditions(condition, npcId, game);
    }

    public Texture getSpeechCloudTexture() {
        return speechCloudTexture;
    }

    public float getSpeechBubbleWidth(String npcId) {
        BitmapFont font = smallFont;
        GlyphLayout layout = new GlyphLayout(font, getNpcDialogText(npcId));
        return layout.width + 20f; //padding
    }

    public float getSpeechBubbleHeight(String npcId) {
        BitmapFont font = smallFont;
        GlyphLayout layout = new GlyphLayout(font, getNpcDialogText(npcId));
        return layout.height + 20f;
    }

    public void openNpcGiftMenu(String npcId) {
        this.npcMenuCurrentNpcId = npcId;

        Skin skin = GameAssetManager.skin;
        npcMenuWindow = new Window(npcId + "NPC menu", skin);
        npcMenuWindow.setModal(true);
        npcMenuWindow.setSize(1356, 1000);
        npcMenuWindow.setPosition(uiStage.getWidth() / 2f, uiStage.getHeight() / 2f, Align.center);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // === ردیف اول: دکمه‌های تب ===
        String[] tabNames = {"Gift", "Info", "Quests"};
        Table tabsRow = new Table();
        for (String tab : tabNames) {
            TextButton tabBtn = new TextButton(tab, skin);
            tabsRow.add(tabBtn).pad(5);

            tabBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    contentCell.setActor(null);
                    switch (tab) {
                        case "Gift":
                            contentCell.setActor(getGiftContentTable(npcId));
                            break;
                        case "Info":
                            contentCell.setActor(getNpcInfoTable(npcId));
                            break;
                        case "Quests":
                            contentCell.setActor(getNpcQuestTable(npcId));
                            break;
                    }
                }
            });
        }
        mainTable.add(tabsRow).growX().padTop(20).row();

        // === ردیف وسط: محتوای اولیه (Gift) ===
        contentCell = mainTable.add(getGiftContentTable(npcId)).expand().fill();
        mainTable.row();

        // === ردیف آخر: دکمه Close ===
        TextButton closeBtn = new TextButton("Close", skin);
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                closeNpcMenu();
            }
        });
        Table closeRow = new Table();
        closeRow.add(closeBtn).center().padBottom(12);
        mainTable.add(closeRow).growX().bottom().row();

        npcMenuWindow.clearChildren();
        npcMenuWindow.add(mainTable).grow().pad(8);

        uiStage.addActor(npcMenuWindow);
        Gdx.input.setInputProcessor(uiStage);
    }

    private Table getGiftInventoryTable() {
        Table inventoryGrid = new Table();
        Skin skin = GameAssetManager.skin;

        Player player = game.getPlayerInTurn();
        Inventory inventory = player.getInventory();
        int numSlots = player.getMaxInventorySize();
        ArrayList<ItemStack> items = inventory.getInventoryItems();

        final float slotSize = 82f;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int i = row * 10 + col;

                Stack slotStack = new Stack();

                Image slotBg = new Image(GameAssetManager.inventorySlot);
                slotBg.setColor(Color.WHITE);
                slotStack.add(slotBg);

                if (i < items.size() && items.get(i) != null && items.get(i).getItem() != null) {
                    TextureRegion itemTex = items.get(i).getItem().getTexture();
                    Image itemImg = new Image(new TextureRegionDrawable(itemTex));
                    slotStack.add(itemImg);

                    int quantity = items.get(i).getAmount();
                    if (quantity > 1) {
                        Table countTable = new Table();
                        Label lbl = new Label(String.valueOf(quantity), skin);
                        lbl.setFontScale(0.74f);
                        lbl.setColor(Color.GOLD);
                        countTable.add(lbl).bottom().center().padBottom(4);
                        countTable.setFillParent(true);
                        countTable.bottom();
                        slotStack.add(countTable);
                    }
                }

                if (i == player.getSelectedSlot()) {
                    Image highlight = new Image(GameAssetManager.inventoryHighlightSlot);
                    highlight.setName("giftHighlight");  // تگ برای تشخیص
                    highlight.setColor(new Color(1, 1, 1, 0.41f));
                    slotStack.add(highlight);
                }

                final int slotIndexFinal = i;
                slotStack.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        player.setSelectedSlot(slotIndexFinal);
                        refreshGiftInventoryHighlight(inventoryGrid, player);
                        return true;
                    }
                });

                inventoryGrid.add(slotStack).size(slotSize, slotSize).pad(4);
            }
            inventoryGrid.row();
        }

        return inventoryGrid;
    }

    private void refreshGiftInventoryHighlight(Table grid, Player player) {
        Array<Cell> cells = grid.getCells();
        for (int c = 0; c < cells.size; c++) {
            Stack slotStack = (Stack) cells.get(c).getActor();

            for (Actor actor : new Array<>(slotStack.getChildren())) {
                if ("giftHighlight".equals(actor.getName())) {
                    slotStack.removeActor(actor);
                    break;
                }
            }

            if (c == player.getSelectedSlot()) {
                Image highlight = new Image(GameAssetManager.inventoryHighlightSlot);
                highlight.setName("giftHighlight");  // اینجا هم تگ بزن
                highlight.setColor(new Color(1, 1, 1, 0.41f));
                slotStack.add(highlight);
            }
        }
    }

    private Table getGiftContentTable(String npcId) {
        Skin skin = GameAssetManager.skin;

        Table gridTable = getGiftInventoryTable();
        ScrollPane scrollPane = new ScrollPane(gridTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollPercentY(0);

        float slotSize = 82 + 8;
        scrollPane.setHeight(2 * slotSize);

        Image tickImg = new Image(new Texture("NPC/tick.png"));
        tickImg.setSize(55, 55);
        tickImg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giveSelectedItemToNpc(npcId);
            }
        });
        
        Label titleLabel = new Label("Select Item to Gift", skin, "title");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.1f);

        Table giftTable = new Table(skin);
        giftTable.add(titleLabel).center().padBottom(20).row();

        Table row = new Table();
        row.add(scrollPane).width(944).height(2 * slotSize).padRight(14);
        row.add(tickImg).size(55, 55).center();
        giftTable.add(row).center().padTop(10).row();

        return giftTable;
    }

    private Table getNpcInfoTable(String npcId) {
        Table info = new Table(GameAssetManager.skin);
        info.add(new Label("Info about " + npcId, GameAssetManager.skin)).center();
        return info;
    }

    private Table getNpcQuestTable(String npcId) {
        Table quests = new Table(GameAssetManager.skin);
        quests.add(new Label("Quests for " + npcId, GameAssetManager.skin)).center();
        return quests;
    }

    public void closeNpcMenu() {
        if (npcMenuWindow != null) {
            npcMenuWindow.remove();
            npcMenuWindow = null;
            npcMenuCurrentNpcId = null;
            Gdx.input.setInputProcessor(gameMenuInputAdapter);
        }
    }

    private void giveSelectedItemToNpc(String npcId) {
        String currentNpcId = npcId;

        Player player = game.getPlayerInTurn();
        int idx = player.getSelectedSlot();
        Inventory inv = player.getInventory();
        ArrayList<ItemStack> items = inv.getInventoryItems();

        if (idx >= 0 && idx < items.size() && items.get(idx) != null) {
            ItemStack stack = items.get(idx);

            if (player.getFriendship(currentNpcId).isFirstGift()) {
                controller.setFriendshipScore(player.getFriendship(currentNpcId), 200);
                player.getFriendship(currentNpcId).setFirstGift(false);
            }

            inv.pickItem(inv.getInventoryItems().get(idx).getName(), 1);

            closeNpcMenu();

            npcHeartVisible.put(currentNpcId.toLowerCase(), true);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    npcHeartVisible.put(currentNpcId.toLowerCase(), false);
                }
            }, 5f);
        }
    }

    // WINDOWS
    public void closeAllUiMenus() {
        if (exitWindow != null) hideExitMenu();
        if (terminalWindow != null && terminalWindow.isVisible()) hideExitMenu();
        if (popupWindow != null && popupWindow.isVisible()) hidePopup();
        if (animalWindow != null && animalWindow.isVisible()) hideAnimalInfoPopup();
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
                blackBackgroundAnimation(() -> controller.changeTurn(), 0.2f);
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
    public void blackBackgroundAnimation(Runnable onCompleteRunnable, float duration) {
        Image blackScreen = new Image(GameAssetManager.blackBox);
        blackScreen.setSize(uiStage.getWidth(), uiStage.getHeight());
        blackScreen.setPosition(0, 0);

        blackScreen.setOrigin(Align.center);
        blackScreen.setScaleY(0f);

        blackScreen.addAction(Actions.sequence(
                Actions.delay(0.1f),

                Actions.scaleTo(1, 1, duration),

                Actions.delay(0.2f),

                Actions.run(onCompleteRunnable),

                Actions.scaleTo(1, 0, duration),

                Actions.removeActor()
        ));

        uiStage.addActor(blackScreen);
    }

    public void delayForAndDo(float duration, Runnable runnable) {
        uiStage.addAction(Actions.sequence(
                Actions.delay(duration),
                Actions.run(runnable)
        ));
    }

    public void loadTodayWeatherAnimation(float delta) {
        WeatherStatus weather = game.getTodayWeather().getStatus();
        Animation<TextureRegion> currentAnimation = null;

        if (weather == WeatherStatus.RAIN || weather == WeatherStatus.STORM) {
            currentAnimation = rainAnimation;
        } else if (weather == WeatherStatus.SNOW) {
            currentAnimation = snowAnimation;
        }

        if (currentAnimation != null) {
            weatherEffectImage.setVisible(true);
            animationTime += delta;

            ((TextureRegionDrawable) weatherEffectImage.getDrawable()).setRegion(currentAnimation.getKeyFrame(animationTime, true));
        } else {
            weatherEffectImage.setVisible(false);
        }

        effectsStage.act(delta);
        effectsStage.draw();
    }

    public void checkGoingNextDay() {
        if (game.shouldGoToNextDay() && !wentNextDay) {
            wentNextDay = true;

            showError("Time to sleep 10 PM. Going next day");
            delayForAndDo(1.0f, () -> blackBackgroundAnimation(() -> {
                game.goToNextDay();
                wentNextDay = false;
            }, 1.0f));
        }
    }

    private void updateNightOverlay() {
        Time time = game.getTime();
        int currentHour = time.getHour();

        int startHour = 17;
        int endHour = 22;
        float targetAlpha = 0f;

        if (currentHour >= startHour && currentHour < endHour) {
            float progress = (float) (currentHour - startHour) / (endHour - startHour);
            targetAlpha = progress * GameSetting.getMaxNightAlpha();
        } else if (currentHour >= endHour || currentHour < 6) {
            targetAlpha = GameSetting.getMaxNightAlpha();
        }

        nightOverlay.getColor().a = targetAlpha;
    }


    public Game getGame() {
        return game;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void show() {
        App.getApp().getMusic().pause();

        font = new BitmapFont();
        font.getData().setScale(2f);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(1f);

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Skin skin = new Skin(Gdx.files.internal("skin2/uiskin.json"));

        prepareFoodMenu(skin);
        prepareCraftingMenu(skin);

        loadTextures();

        energyBar.setValue((float) game.getPlayerInTurn().getEnergy());

        // effects
        rainAnimation = GameAssetManager.rainingAnimation;
        snowAnimation = GameAssetManager.snowAnimation;

        weatherEffectImage = new Image(rainAnimation.getKeyFrame(0, true));
        weatherEffectImage.setSize(uiStage.getWidth(), uiStage.getHeight());
        weatherEffectImage.setTouchable(Touchable.disabled);
        weatherEffectImage.setVisible(false);

        effectsStage.addActor(weatherEffectImage);


        // popup window
        loadPopupWindow();

        loadAnimalInfoPopup();

        showError("Welcome " + game.getPlayerInTurn().getNickname() + " !");

        Time time = game.getTime();
        Weather todayWeather = game.getTodayWeather();

        Texture seasonTex = new Texture(Gdx.files.internal("clock/" + time.getSeason().name() + ".png"));
        seasonTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.seasonTexture = new TextureRegion(seasonTex);

        Texture weatherTex = new Texture(Gdx.files.internal("clock/" + todayWeather.getStatus().name() + ".png"));
        weatherTex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.weatherTexture = new TextureRegion(weatherTex);
    }

    @Override
    public void render(float v) {
        try {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            checkGoingNextDay();

            gameMenuInputAdapter.handlePlayerMovement(v, game);

            updateNightOverlay();

            updateEnergyBar();
            renderCamera();

            // everything render based on camera
            batch.setProjectionMatrix(camera.combined);
            stateTime += v;
            batch.begin();
            renderTiles();
            renderAnimals(v);

            renderBuildings();
            renderPlayers(v);
            renderNPCs(batch);

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

            // Weather animation
            loadTodayWeatherAnimation(v);

            if (pendingInventoryUiRefresh) {
                updateInventoryContent();
                pendingInventoryUiRefresh = false;
            }

            // UI
            uiStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            uiStage.draw();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (batch.isDrawing()) {
                batch.end();
            }
        }
    }

    @Override
    public void resize(int w, int h) {
        uiStage.getViewport().update(w, h, true);

        effectsStage.getViewport().update(w, h, true);

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

    public static GameScreen getScreen() {
        return screen;
    }

    public GameInputAdapter getGameMenuInputAdapter() {
        return gameMenuInputAdapter;
    }

    public Stage getStage() {
        return uiStage;
    }
}
