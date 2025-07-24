package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.MainGuiController;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.models.services.SaveAppManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private final Skin skin;

    private final Image background;
    private final Image logo;
    private final Label menuTitle;

    private TextButton gameMenuButton;
    private TextButton profileMenuButton;
    private TextButton logoutButton;

    public Table table = new Table();
    private Music music;
    private final MainGuiController controller;

    public MainMenuScreen() {
        this.controller = AppGuiControllers.mainGuiController;
        this.skin = GameAssetManager.skin;
        this.background = new Image(GameAssetManager.MenuTexture2);
        this.logo = new Image(GameAssetManager.logoTexture);
        this.menuTitle = new Label("Main Menu", skin);
        this.gameMenuButton = new TextButton("Game Menu", skin);
        this.profileMenuButton = new TextButton("Profile", skin);
        this.logoutButton = new TextButton("Logout", skin);
        this.music = GameAssetManager.music1;
        AppGuiControllers.mainGuiController.setMenuView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        music.setLooping(true);
        music.play();

        // تنظیم و اضافه‌کردن بک‌گراند
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(1, 1, 1, 0.5f);
        stage.addActor(background);

        // اضافه‌کردن لوگو بالا وسط
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 1.5f);
        logo.setPosition(
                Gdx.graphics.getWidth() / 2f - logo.getWidth() / 2f,
                Gdx.graphics.getHeight() - logo.getHeight() - 10
        );
        stage.addActor(logo);

        // تنظیمات جدول
        table.setFillParent(true);
        table.top().padTop(250);
        stage.addActor(table);

        // عنوان منو
        menuTitle.setColor(Color.valueOf("ffd60a"));
        menuTitle.setFontScale(2f);
        table.add(menuTitle).colspan(2).center().padBottom(80);
        table.row();

        // دکمه Game Menu
        gameMenuButton.setColor(Color.valueOf("8ecae6"));
        table.add(gameMenuButton).width(400).height(100).padBottom(30);
        table.row();

        // دکمه Profile Menu
        profileMenuButton.setColor(Color.valueOf("ffb703"));
        table.add(profileMenuButton).width(400).height(100).padBottom(30);
        table.row();

        // دکمه Logout
        logoutButton.setColor(Color.valueOf("E9D8A6"));
        table.add(logoutButton).width(400).height(100);
        table.row();

        controller.handleMainMenu();
    }


    @Override
    public void render(float v) {
        Main.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        ScreenUtils.clear(0, 0, 0, 1);
        Main.getBatch().setShader(null);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        SaveAppManager.saveApp();
        stage.dispose();
    }

    // Auxiliary functions :

    public TextButton getGameMenuButton() {
        return gameMenuButton;
    }

    public TextButton getProfileMenuButton() {
        return profileMenuButton;
    }

    public TextButton getLogoutButton() {
        return logoutButton;
    }
}
