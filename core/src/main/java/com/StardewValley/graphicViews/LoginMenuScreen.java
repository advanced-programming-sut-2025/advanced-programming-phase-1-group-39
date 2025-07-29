package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.LoginGuiController;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoginMenuScreen implements Screen {
    private Stage stage;
    private final Skin skin;

    private final Image background;
    private final Image logo;
    private final Label menuTitle;

    private Label usernameLabel;
    private TextField usernameField;
    private Label usernameErrorLabel;

    private Label passwordLabel;
    private TextField passwordField;
    private Label passwordErroeLabel;

    private CheckBox stayLoggedInCheckBox;

    private TextButton loginButton;
    private TextButton forgotPasswordButton;
    private TextButton backButton;

    public Table table = new Table();
    private Music music;
    private final LoginGuiController controller;

    public LoginMenuScreen() {
        this.controller = AppGuiControllers.loginGuiController;
        this.skin = GameAssetManager.skin;
        this.background = new Image(GameAssetManager.MenuTexture);
        this.logo = new Image(GameAssetManager.logoTexture);
        this.menuTitle = new Label("Login :", skin);
        this.usernameLabel = new Label("Username :", skin);
        this.usernameField = new TextField("", skin);
        this.usernameField.setMessageText("Enter your Username");
        this.usernameErrorLabel = new Label("", skin);
        this.passwordLabel = new Label("Password :", skin);
        this.passwordField = new TextField("", skin);
        this.passwordField.setMessageText("Enter your Password");
        this.passwordErroeLabel = new Label("", skin);
        this.stayLoggedInCheckBox = new CheckBox("Stay LoggedIn", skin);
        this.loginButton = new TextButton("Login", skin);
        this.forgotPasswordButton = new TextButton("Forgot Password", skin);
        this.backButton = new TextButton("Back", skin);
        this.music = GameAssetManager.music1;
        AppGuiControllers.loginGuiController.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        // موسیقی و بک‌گراند
        music.setLooping(true);
        music.play();

        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(1, 1, 1, 0.5f);
        stage.addActor(background);

        // لوگو بالا وسط
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 1.5f);
        logo.setPosition(
                Gdx.graphics.getWidth() / 2f - logo.getWidth() / 2f,
                Gdx.graphics.getHeight() - logo.getHeight() - 10
        );
        stage.addActor(logo);

        // تنظیمات جدول اصلی
        table.setFillParent(true);
        table.top().padTop(250);
        stage.addActor(table);

        // عنوان منو
        menuTitle.setColor(Color.valueOf("ffd60a"));
        menuTitle.setFontScale(1.8f);
        table.add(menuTitle).colspan(2).center().padBottom(60);
        table.row();

        // ===== Username =====
        Table usernameRow = new Table();
        usernameLabel.setColor(Color.valueOf("023047"));
        usernameRow.add(usernameLabel).left().colspan(2);
        usernameRow.row();
        usernameRow.add(usernameField).width(600);
        table.add(usernameRow).colspan(2).center().padBottom(10);
        table.row();
        usernameErrorLabel.setColor(Color.RED);
        table.add(usernameErrorLabel).left().colspan(2).padBottom(20);
        table.row();

        // ===== Password =====
        Table passwordRow = new Table();
        passwordLabel.setColor(Color.valueOf("023047"));
        passwordRow.add(passwordLabel).left().colspan(2);
        passwordRow.row();
        passwordRow.add(passwordField).width(600);
        table.add(passwordRow).colspan(2).center().padBottom(10);
        table.row();
        passwordErroeLabel.setColor(Color.RED);
        table.add(passwordErroeLabel).left().colspan(2).padBottom(30);
        table.row();

        // ===== Stay Logged In =====
        table.add(stayLoggedInCheckBox).center().colspan(2).padBottom(30);
        table.row();

        // ===== دکمه‌های ورود و فراموشی رمز =====
        Table buttonsRow = new Table();
        loginButton.setColor(Color.valueOf("E9D8A6"));
        forgotPasswordButton.setColor(Color.valueOf("FFB703"));
        buttonsRow.add(loginButton).width(450).height(110);
        buttonsRow.row().padTop(20);
        buttonsRow.add(forgotPasswordButton);
        table.add(buttonsRow).colspan(2).center().padBottom(40);
        table.row();

        // ===== Back Button =====
        backButton.setColor(Color.valueOf("E9D8A6"));
        backButton.setPosition(20, 950);
        stage.addActor(backButton);

        controller.handleLogin();
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


    public TextField getUsernameField() {
        return usernameField;
    }

    public Label getUsernameErrorLabel() {
        return usernameErrorLabel;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public Label getPasswordErroeLabel() {
        return passwordErroeLabel;
    }

    public CheckBox getStayLoggedInCheckBox() {
        return stayLoggedInCheckBox;
    }

    public TextButton getLoginButton() {
        return loginButton;
    }

    public TextButton getForgotPasswordButton() {
        return forgotPasswordButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }
}
