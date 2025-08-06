package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.MainGuiController;
import com.StardewValley.models.App;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private final Skin skin;

    private final Image background;
    private final Image logo;
    private final Label menuTitle;

    private TextButton gameMenuButton;
    private TextButton gameOnlineButton;
    private TextButton profileMenuButton;
    private TextButton logoutButton;
    private TextButton exitButton;

    private TextureRegion nameTexture;
    private Label nameLabel;
    private TextureRegion avatarTexture;

    public Table table = new Table();
    private Music music;
    private final MainGuiController controller;

    public MainMenuScreen() {
        this.controller = AppGuiControllers.mainGuiController;
        this.skin = GameAssetManager.skin;
        this.background = new Image(GameAssetManager.MenuTexture2);
        this.logo = new Image(GameAssetManager.logoTexture);
        this.menuTitle = new Label("Main Menu", skin);
        this.gameMenuButton = new TextButton("Offline Game", skin);
        this.gameOnlineButton = new TextButton("Online Game", skin);
        this.profileMenuButton = new TextButton("Profile", skin);
        this.logoutButton = new TextButton("Logout", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.nameTexture = GameAssetManager.nameLabel;
        this.nameLabel = new Label("Your Name :" + App.getApp().getLoggedInUser().getNickname(), skin);
        this.avatarTexture = new TextureRegion(new Texture(App.getApp().getLoggedInUser().getAvatar()));
        this.music = GameAssetManager.music1;
        AppGuiControllers.mainGuiController.setMenuView(this);
    }

    @Override
    public void show() {
        try {
            stage = new Stage(new FitViewport(1920, 1080));
            Gdx.input.setInputProcessor(stage);

            music.setLooping(true);
            music.play();

            // تنظیم و اضافه‌کردن بک‌گراند
            background.setSize(stage.getWidth(), stage.getHeight());
            background.setColor(1, 1, 1, 0.5f);
            stage.addActor(background);

            // اضافه‌کردن لوگو بالا وسط
            logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 1.5f);
            logo.setPosition(
                    Gdx.graphics.getWidth() / 2f - logo.getWidth() / 2f,
                    Gdx.graphics.getHeight() - logo.getHeight() - 10
            );
            stage.addActor(logo);

            Image avatarFrame = new Image(GameAssetManager.avatarFrame);
            Image avatarImage = new Image(avatarTexture);
            avatarImage.setSize(150, 150);
            avatarFrame.setSize(190, 190);
            avatarImage.setPosition(
                    205,  // فاصله از لبه چپ
                    Gdx.graphics.getHeight() - avatarImage.getHeight() - 157  // از بالا
            );
            avatarFrame.setPosition(
                    190,
                    Gdx.graphics.getHeight() - avatarImage.getHeight() - 170
            );
            stage.addActor(avatarFrame);
            stage.addActor(avatarImage);

            // === name texture (قاب پشت نام) سمت چپ زیر آواتار ===
            Image nameBackground = new Image(nameTexture);
            nameBackground.setSize(470, 70);
            nameBackground.setPosition(
                    50,  // فاصله از لبه چپ
                    avatarImage.getY() - nameBackground.getHeight() - 20
            );
            stage.addActor(nameBackground);

            // === name label روی قاب ===
            nameLabel.setFontScale(1.5f);
            nameLabel.setColor(Color.valueOf("dda15e"));
            nameLabel.setAlignment(Align.center);
            nameLabel.setSize(nameBackground.getWidth(), nameBackground.getHeight());  // تا متن وسط قاب بمونه
            nameLabel.setPosition(nameBackground.getX(), nameBackground.getY());
            stage.addActor(nameLabel);

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
            gameMenuButton.setColor(Color.valueOf("E9D8A6"));
            table.add(gameMenuButton).width(400).height(100).padBottom(30);
            table.row();

            gameOnlineButton.setColor(Color.valueOf("E9D8A6"));
            table.add(gameOnlineButton).width(400).height(100).padBottom(30);
            table.row();

            // دکمه Profile Menu
            profileMenuButton.setColor(Color.valueOf("E9D8A6"));
            table.add(profileMenuButton).width(400).height(100).padBottom(50);
            table.row();

            // دکمه Logout
            logoutButton.setColor(Color.valueOf("E9D8A6"));
            table.add(logoutButton).width(400).height(100).padBottom(30);
            table.row();

            exitButton.setColor(Color.valueOf("E9D8A6"));
            table.add(exitButton).width(400).height(100);

            controller.handleMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

    @Override
    public void dispose() {
        AppDataManager.saveApp();
        stage.dispose();
    }

    // Auxiliary functions :

    public TextButton getGameMenuButton() {
        return gameMenuButton;
    }

    public TextButton getGameOnlineButton() {
        return gameOnlineButton;
    }

    public TextButton getProfileMenuButton() {
        return profileMenuButton;
    }

    public TextButton getLogoutButton() {
        return logoutButton;
    }

    public TextButton getExitButton() { return  exitButton; }
}
