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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    public PregameMenuScreen() {
        this.controller = AppControllers.gameMenuController;
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal("menu_background.jfif"));

        stage = new Stage(new FitViewport(1920, 1080));
        table = new Table();
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);

        Skin skin = GameAssetManager.skin;
        newGameButton = new TextButton("New Game", skin);
        loadGameButton = new TextButton("Load Game", skin);
        backButton = new TextButton("Back", skin);

        stage.clear();

        gameTitle = new Image(GameAssetManager.titleImage);

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
    }

    public void addButtonsListener() {
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                // TODO : window for starting a game
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

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        Batch batch = Main.batch;

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.draw();
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
