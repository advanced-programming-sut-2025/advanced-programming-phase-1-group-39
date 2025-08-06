package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.WeatherStatus;
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
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameScreen implements Screen {
    private static GameScreen screen;
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

    // effects
    private Stage effectsStage;
    private Animation<TextureRegion> rainAnimation;
    private Animation<TextureRegion> snowAnimation;

    private Image weatherEffectImage;
    private float animationTime = 0f;

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
        energyStack.add(barTable);

        miniMapWidget = new MiniMapWidget(this.game);
        minimapCell = hudTable.add(miniMapWidget).size(200, 150).top().left().pad(20);

        hudTable.add(energyStack)
                .width(1.5f * energyBox.getWidth()).height(1.5f * energyBox.getHeight())
                .expand().bottom().right().pad(20f);


        // for error message
        Table messageTable = new Table();
        messageTable.setFillParent(true);
        rootStack.add(messageTable);

        errorLabel = new Label("", GameAssetManager.messageBoxStyle);
        errorLabel.setVisible(false);
        errorLabel.setAlignment(Align.center);
        messageTable.add(errorLabel).bottom().padBottom(50).expandY(); // expandY is needed to effect by the bottom()
    }

    // utils
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


    public void showPopup(String message, Runnable runnable) {
        if (popupWindow.isVisible() || message.isEmpty()) return;

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


    public void loadTextures() {
        playerAtlas = new TextureAtlas(Gdx.files.internal("characters/Abigail/sprites_player.atlas"));
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
        player.setCurrentState(PlayerState.WalkingOrIdle);
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
                    batch.draw(texture, inMapLocation.x(), inMapLocation.y(), tileSize, tileSize);
                }
            }
        }
    }

    public void renderAnimals(float v) {
        for (Player player : game.getPlayers()) {
            for (Animal animal : player.getAnimals()) {
                Vector2 movement = animal.updateAnimalMovement(v);
                gameMenuInputAdapter.handleAnimalMovement(game , animal, movement);
                System.out.println("animal " + animal.getName() + " moved to " + animal.getX() + " " + animal.getY());

                TextureRegion texture = animal.getTexture();
                int tileSize = Map.TILE_SIZE;
                batch.draw(texture, animal.getX(), animal.getY(), tileSize, tileSize);
            }
        }
    }
/// test
    public void cheatPlayer() {
        Player player = game.getPlayerInTurn();

        player.addToBuildings(new AnimalBuilding("coop", player.getTileLocation(), 7,4, LivingPlace.COOP));
        AnimalBuilding building = player.getAnimalBuilding(LivingPlace.COOP);
        building.updateMap(game.getMap());

        Animal animal = new Animal(AnimalType.CHICKEN, "joojeh", 500, LivingPlace.COOP, new ArrayList<>() );
        player.addAnimal(animal);
        building.addAnimalAndSetLocationInside(animal);
        showError("cheated Animal");
    }

    public void renderPlayers(float data) {
        for (Player player : game.getPlayers()) {
            renderPlayer(player, data);
        }
    }

    private void renderPlayer(Player currentPlayer , float delta) {
        Animation<TextureRegion> currentAnimation;

        switch (currentPlayer.getCurrentState()) {
            case WalkingOrIdle:
                int animIndex = switch (currentPlayer.getDirection()) {
                    case UP -> 3;
                    case RIGHT -> 2;
                    case DOWN -> 1;
                    case LEFT -> 4;
                    default -> 0;
                };
                currentAnimation = playersAnimations.get(currentPlayer).get(animIndex);
                break;
            case Unconscious:
                currentAnimation = playersAnimations.get(currentPlayer).get(0); // TODO : change to unco
                break;
            default:
                currentAnimation = playersAnimations.get(currentPlayer).get(0);
                break;
        }

        batch.setColor(currentPlayer.getColor());

        TextureRegion currentFrame = currentAnimation.getKeyFrame(currentPlayer.getAnimationStateTime(), true);

        if (currentFrame == null) return;

        float drawX = currentPlayer.getX() - (Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_W) / 2f;
        float drawY = currentPlayer.getY();

        currentPlayer.updateAnimationStateTime(delta);

        batch.draw(currentFrame, drawX, drawY, Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_W, Map.TILE_SIZE * Constants.PLAYER_SPRITE_TILE_H);

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

        String date = (time.getDayOfWeek().toString().substring(0,3)) + ". " + time.getDay();
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
        scrollPane.setPosition((uiStage.getWidth() - cookingMenuTable.getWidth())/2,
                (uiStage.getHeight() - cookingMenuTable.getHeight())/2, Align.center);

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
        scrollPane.setPosition((uiStage.getWidth() - craftingMenu.getWidth())/2,
                (uiStage.getHeight() - craftingMenu.getHeight())/2, Align.center);

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

    private void renderInventory() {
        Player player = game.getPlayerInTurn();
        Inventory inventory = player.getInventory();
        int selectedSlot = player.getSelectedSlot(); // Assuming you have this method

        int screenWidth = Gdx.graphics.getWidth();
        int slotSize = Map.TILE_SIZE / 2;
        int numSlots = player.getMaxInventorySize();
        int startX = (screenWidth - numSlots * slotSize) / 2 ;
        int y = Map.TILE_SIZE / 2;

        for (int i = 0; i < numSlots; i++) {
            int x = startX + i * slotSize;

            batch.draw(inventorySlot, x, y, slotSize, slotSize);

            String slotNum = String.valueOf(i + 1);
            smallFont.draw(batch, slotNum, x + 2, y + slotSize - 2);
        }

        // Highlight selected slot
        if (selectedSlot >= 0 && selectedSlot < numSlots) {
            int highlightX = startX + selectedSlot * slotSize;
            batch.draw(inventoryHighlightSlot, highlightX, y, slotSize, slotSize);
        }

        for (int i = 0; i < numSlots; i++) {
            if (i < inventory.getInventoryItems().size()) {
                if (inventory.getInventoryItems().get(i) != null) {
                    int quantity = inventory.getInventoryItems().get(i).getAmount();

                    TextureRegion itemTex = inventory.getInventoryItems().get(i).getItem().getTexture();
                    if (itemTex != null) {
                        int x = startX + i * slotSize;
                        batch.draw(itemTex, x, y, slotSize, slotSize);

                        // Draw item quantity at bottom-right corner
                        String count = String.valueOf(quantity);
                        layout.setText(smallFont, count);
                        smallFont.draw(batch, count, x + slotSize - layout.width - 2, y + layout.height + 2);
                    }
                }
            }
        }
    }


    // WINDOWS
    public void closeAllUiMenus() {
        if (exitWindow != null) hideExitMenu();
        if (terminalWindow != null && terminalWindow.isVisible()) hideExitMenu();
        if (popupWindow != null && popupWindow.isVisible()) hidePopup();
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

            ((TextureRegionDrawable)weatherEffectImage.getDrawable()).setRegion(currentAnimation.getKeyFrame(animationTime, true));
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
            delayForAndDo(1.0f, () -> blackBackgroundAnimation(()-> {
                game.goToNextDay();
                wentNextDay = false;
            }, 1.0f));
        }
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

            updateEnergyBar();
            renderCamera();

            // everything render based on camera
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            renderTiles();
            renderBuildings();
            renderPlayers(v);
            renderAnimals(v);

            // TODO : (Better) move clock render to uiStage
            renderClockUI();
            //TODO : correct
//            renderInventory();

            batch.end();

            // Weather animation
            loadTodayWeatherAnimation(v);

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
        uiStage.getViewport().update(w,h, true);

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
