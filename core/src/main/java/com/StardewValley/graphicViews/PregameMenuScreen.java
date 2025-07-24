package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.PregameGuiController;
import com.StardewValley.models.App;
import com.StardewValley.models.Result;
import com.StardewValley.models.User;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.models.services.SaveAppManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

public class PregameMenuScreen implements Screen {
    private PregameGuiController controller;

    private Stage stage;
    private Table table;

    private Image gameTitle;
    private Texture background;

    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton backButton;

    // New Game Window
    private Window newGameWindow;
    private ArrayList<Table> startGamePages = new ArrayList<>();
    private int startGamePageIndex = 0;
    private int numOfStartGamePages = 5;
    private Table startGameContentTable;

    private TextButton nextButton;
    private TextButton previousButton;

    private TextField user1;
    private TextField user2;
    private TextField user3;
    private TextField user4;
    private Label errorLabel;

    public PregameMenuScreen() {
        this.controller = AppGuiControllers.pregameGuiController;
    }

    @Override
    public void show() {
        // 1. Initialize all
        background = new Texture(Gdx.files.internal("menu_background.jfif"));

        stage = new Stage(new FitViewport(1920, 1080));
        table = new Table();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage); // Important

        Skin skin = GameAssetManager.skin;
        newGameButton = new TextButton("New Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        backButton = new TextButton("Back", skin);

        stage.clear();

        gameTitle = new Image(GameAssetManager.titleImage);
        // Making Pregame Menu Style
        table.center();
        table.row();
        gameTitle.setScaling(Scaling.fill);
        table.add(gameTitle).colspan(4).width(1000);

        int buttonSize = 300;
        table.row().padTop(150);
        table.add(newGameButton).width(buttonSize);
        table.add(loadGameButton).width(buttonSize).padLeft(buttonSize/10);
        table.add(backButton).width(buttonSize).padLeft(buttonSize/10);

        user1 = new TextField("", skin);
        user2 = new TextField("", skin);
        user3 = new TextField("", skin);
        user4 = new TextField("", skin);

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        stage.addActor(table);
        addButtonsListener();
        buildNewGameWindow();
    }

    public void addButtonsListener() {
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                stage.addActor(newGameWindow);
            }
        });

        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                controller.loadGame();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                controller.goMainMenu();
                Main.getMain().switchScreen(new MainMenuScreen());
            }
        });
    }


    public Table addUsernamesFormTable(Skin skin) {
        Table usernamesForm = new Table();

        int inputW = 550;
        usernamesForm.row();
        usernamesForm.add(new Label("Player 1:", skin)).right().padRight(20);
        usernamesForm.add(user1).width(inputW).left();
        user1.setText(App.getApp().getLoggedInUser().getUserName());
        user1.setDisabled(true);
        user1.setColor(Color.GRAY);

        usernamesForm.row().padTop(45);
        usernamesForm.add(new Label("Player 2:", skin)).right().padRight(20);
        usernamesForm.add(user2).width(inputW).left();
        user2.setMessageText("Enter username");

        usernamesForm.row().padTop(45);
        usernamesForm.add(new Label("Player 3:", skin)).right().padRight(20);
        usernamesForm.add(user3).width(inputW).left();
        user3.setMessageText("Enter username");

        usernamesForm.row().padTop(45);
        usernamesForm.add(new Label("Player 4:", skin)).right().padRight(20);
        usernamesForm.add(user4).width(inputW).left();
        user4.setMessageText("Enter username");

        return usernamesForm;
    }

    public Table addMapSelectionTable(Skin skin, int playerNumber) {
        Table page = new Table();

        SelectBox<String> mapSelectBox = new SelectBox<>(skin);
        mapSelectBox.setItems(Map.getFarmTypeName(0), Map.getFarmTypeName(1));

        mapSelectBox.addListener(new ChangeListener() {
            public void changed(ChangeEvent changeEvent, Actor actor) {

            }
        });
        Label header = new Label("Choosing Map for " + controller.getUserNickName(playerNumber) + "'s Farm", skin);
        header.setFontScale(2f);
        header.setColor(Color.YELLOW);
        page.add(header).center().fillX().colspan(2);
        page.row().padTop(50);
        page.add(new Label("Map Type:", skin)).right().padRight(10);
        page.add(mapSelectBox).width(500).left();

        return page;
    }

    public void buildNewGameWindow() {
        Skin skin = GameAssetManager.skin;
        newGameWindow = new Window("", skin);
        newGameWindow.setModal(true); // to set the background buttons disable
        newGameWindow.setSize(1000, 1000);
        newGameWindow.setPosition(
                stage.getWidth() / 2f,
                stage.getHeight() / 2f,
                Align.center
        );

        startGamePages.add(addUsernamesFormTable(skin));
        for (int i = 0; i < 4; i++)
            startGamePages.add(new Table());


        startGameContentTable = new Table();
        nextButton = new TextButton("Next", skin);
        previousButton = new TextButton("Back", skin);

        showPage(0);
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (startGamePageIndex == 0) {
                    // check the form
                    Result result = controller.checkStartGame(user2.getText(), user3.getText(),
                            user4.getText());
                    if (!result.success())
                        errorLabel.setText(result.message());
                    else {
                        ArrayList<User> users = controller.getGameUsers();
                        for (int i = 1; i <= 4; i++) {
                            startGamePages.set(i, addMapSelectionTable(skin, i));
                        }
                        showPage(1);
                    }
                } else if (startGamePageIndex == numOfStartGamePages - 1) {
                    Main.getMain().switchScreen(new GameScreen());
                } else {
                    showPage(1);
                }
            }
        });
        previousButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (startGamePageIndex == 0)
                    newGameWindow.remove();
                else
                    showPage(-1);

            }
        });

        newGameWindow.add(startGameContentTable).expand().fill().colspan(2);
        newGameWindow.row().pad(100, 45 , 30 , 45).expandX().fillX();
        newGameWindow.add(previousButton).width(250).left();
        newGameWindow.add(nextButton).width(250).right();

        newGameWindow.row().padTop(50);
        newGameWindow.add(errorLabel).colspan(2);

    }

    private void showPage(int offset) {
        errorLabel.setText("");
        startGameContentTable.clear();

        startGamePageIndex += offset;

        if (startGamePageIndex == 0) {
            previousButton.setText("Close");
        } else if (startGamePageIndex == numOfStartGamePages - 1) {
            nextButton.setText("Start");
        } else {
            previousButton.setText("Back");
            nextButton.setText("Next");
        }

        startGameContentTable.add(startGamePages.get(startGamePageIndex));
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        Batch batch = Main.batch;

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        SaveAppManager.saveApp();
        stage.dispose();
        background.dispose();
    }

    public TextButton getBackButton() {
        return backButton;
    }

    public TextButton getLoadGameButton() {
        return loadGameButton;
    }

    public TextButton getNewGameButton() {
        return newGameButton;
    }
}
