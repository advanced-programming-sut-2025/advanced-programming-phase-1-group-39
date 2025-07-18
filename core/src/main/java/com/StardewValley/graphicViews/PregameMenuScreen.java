package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.GameMenuController;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PregameMenuScreen implements Screen {
    private GameMenuController controller;

    private Stage stage;
    private Table table;

    private Image gameTitle;
    private Texture background;

    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton backButton;

    private Window newGameWindow;

    public PregameMenuScreen() {
        this.controller = AppControllers.gameMenuController;
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

        stage.addActor(table);
        addButtonsListener();
        setNewGameWindow();
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
                Main.getMain().switchScreen(Menu.MAIN_MENU.getScreen());
            }
        });
    }

    public void setNewGameWindow() {
        Skin skin = GameAssetManager.skin;
        newGameWindow = new Window("Starting New Game", skin);
        newGameWindow.debug();
        newGameWindow.setSize(1000, 800); // Adjusted size for better fit
        newGameWindow.setPosition(
                Gdx.graphics.getWidth() / 2f - newGameWindow.getWidth() / 2f,
                Gdx.graphics.getHeight() / 2f - newGameWindow.getHeight() / 2f
        );


        TextField user1 = new TextField("", skin);
        TextField user2 = new TextField("", skin);
        TextField user3 = new TextField("", skin);
        TextField user4 = new TextField("", skin);

        TextButton startButton = new TextButton("Start", skin);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
//                controller.startNewGame(); TODO : complete
            }
        });
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                newGameWindow.remove();
            }
        });


        int inputW = 550;
        newGameWindow.row().padTop(45);
        newGameWindow.add(user1).width(inputW).colspan(2);
        user1.setText(App.getApp().getLoggedInUser().getUserName());
        user1.setDisabled(true);
        user1.setColor(Color.RED);

        newGameWindow.row().padTop(45);
        newGameWindow.add(user2).width(inputW).colspan(2);
        user2.setMessageText("username 2");

        newGameWindow.row().padTop(45);
        newGameWindow.add(user3).width(inputW).colspan(2);
        user3.setMessageText("username 3");

        newGameWindow.row().padTop(45);
        newGameWindow.add(user4).width(inputW).colspan(2);
        user4.setMessageText("username4");

        newGameWindow.row().padTop(100);
        newGameWindow.add(startButton).width(250);
        newGameWindow.add(backButton).width(250).padLeft(25);
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
        stage.dispose();
        background.dispose();
    }
}
