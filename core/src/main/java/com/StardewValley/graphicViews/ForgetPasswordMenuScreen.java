package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.ForgetPasswordGuiController;
import com.StardewValley.models.GameMenuAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ForgetPasswordMenuScreen implements Screen {
    private Stage stage;
    private final Skin skin;

    private final Image background;
    private final Image logo;
    private final Label menuTitle;

    private Label usernameLabel;
    private TextField usernameField;
    private Label usernameErrorLabel;

    private Label securityLabel;
    private TextField securityField;
    private Label securityErrorLabel;

    private Label newPasswordLabel;
    private TextField newPasswordField;
    private Label newPasswordErrorLabel;

    private TextButton randomPasswordButton;
    private TextButton changePasswordButton;
    private TextButton backButton;

    public Table table = new Table();
    private Music music;
    private final ForgetPasswordGuiController controller;

    public ForgetPasswordMenuScreen() {
        this.controller = AppGuiControllers.forgetPasswordGuiController;
        this.skin = GameMenuAssetManager.skin;
        this.background = new Image(GameMenuAssetManager.MenuTexture);
        this.logo = new Image(GameMenuAssetManager.logoTexture);
        this.menuTitle = new Label("Forget Password", skin);
        this.usernameLabel = new Label("Username :", skin);
        this.usernameField = new TextField("", skin);
        this.usernameField.setMessageText("Enter your username");
        this.usernameErrorLabel = new Label("", skin);
        this.securityLabel = new Label("Security Question :", skin);
        this.securityField = new TextField("", skin);
        this.securityField.setMessageText("Enter your answer to selected question");
        this.securityErrorLabel = new Label("", skin);
        this.newPasswordLabel = new Label("New Password :", skin);
        this.newPasswordField = new TextField("", skin);
        this.newPasswordField.setMessageText("Enter your new password");
        this.newPasswordErrorLabel = new Label("", skin);
        this.randomPasswordButton = new TextButton("Random", skin);
        this.changePasswordButton = new TextButton("Change Password", skin);
        this.backButton = new TextButton("Back", skin);
        this.music = GameMenuAssetManager.music1;
        AppGuiControllers.forgetPasswordGuiController.setView(this);
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
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

        // ===== عنوان منو =====
        menuTitle.setColor(Color.valueOf("ffd60a"));
        menuTitle.setFontScale(1.8f);
        table.add(menuTitle).colspan(2).center().padBottom(60);
        table.row();

        // ===== فیلد نام کاربری =====
        Table usernameRow = new Table();
        usernameLabel.setColor(Color.valueOf("023047"));
        usernameRow.add(usernameLabel).left().colspan(2);
        usernameRow.row();
        usernameRow.add(usernameField).width(650);
        table.add(usernameRow).colspan(2).center().padBottom(10);
        table.row();
        usernameErrorLabel.setColor(Color.RED);
        table.add(usernameErrorLabel).left().colspan(2).padBottom(20);
        table.row();

        // ===== فیلد پاسخ امنیتی =====
        Table securityRow = new Table();
        securityLabel.setColor(Color.valueOf("023047"));
        securityRow.add(securityLabel).left().colspan(2);
        securityRow.row();
        securityRow.add(securityField).width(650);
        table.add(securityRow).colspan(2).center().padBottom(10);
        table.row();
        securityErrorLabel.setColor(Color.RED);
        table.add(securityErrorLabel).left().colspan(2).padBottom(20);
        table.row();

        // ===== فیلد رمز عبور جدید =====
        Table passwordRow = new Table();
        newPasswordLabel.setColor(Color.valueOf("023047"));
        passwordRow.add(newPasswordLabel).left().colspan(2);
        passwordRow.row();
        passwordRow.add(newPasswordField).width(650);
        table.add(passwordRow).colspan(2).center().padBottom(10);
        table.row();
        newPasswordErrorLabel.setColor(Color.RED);
        table.add(newPasswordErrorLabel).left().colspan(2).padBottom(30);
        table.row();

        // ===== دکمه تغییر رمز =====
        changePasswordButton.setColor(Color.valueOf("90BE6D"));
        table.add(changePasswordButton).colspan(2).center().padBottom(30);
        table.row();

        randomPasswordButton.setColor(Color.valueOf("E9D8A6"));
        randomPasswordButton.setPosition(1500, 600);
        stage.addActor(randomPasswordButton);

        // ===== دکمه بازگشت =====
        backButton.setColor(Color.valueOf("E9D8A6"));
        backButton.setPosition(20, 1300);
        stage.addActor(backButton);

        controller.handleForgetPassword();
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

    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public Label getUsernameErrorLabel() {
        return usernameErrorLabel;
    }

    public TextField getSecurityField() {
        return securityField;
    }

    public Label getSecurityErrorLabel() {
        return securityErrorLabel;
    }

    public TextField getNewPasswordField() {
        return newPasswordField;
    }

    public Label getNewPasswordErrorLabel() {
        return newPasswordErrorLabel;
    }

    public TextButton getRandomPasswordButton() {
        return randomPasswordButton;
    }

    public TextButton getChangePasswordButton() {
        return changePasswordButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }
}
