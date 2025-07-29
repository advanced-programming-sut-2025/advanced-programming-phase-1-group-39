package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.ProfileGuiController;
import com.StardewValley.models.App;
import com.StardewValley.models.User;
import com.StardewValley.models.services.GameAssetManager;
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

public class ProfileMenuScreen implements Screen {
    private Stage stage;
    private final Skin skin;

    private final Image background;
    private final Image logo;
    private final Label menuTitle;

    private Label username;
    private Label nickname;
    private Label maxCoin;
    private Label numberOfGamesPlayed;
    private Label genderLabel;

    private Label changeUsernameLabel;
    private TextField changeUsernameField;
    private Label changeUsernameErrorLabel;
    private TextButton changeUsernameButton;

    private Label changeNicknameLabel;
    private TextField changeNicknameField;
    private Label changeNicknameErrorLabel;
    private TextButton changeNicknameButton;

    private Label changePasswordLabel;
    private TextField changePasswordField;
    private Label changePasswordErrorLabel;
    private TextButton changePasswordButton;

    private Label changeEmailLabel;
    private TextField changeEmailField;
    private Label changeEmailErrorLabel;
    private TextButton changeEmailButton;

    private TextButton backButton;

    public Table table = new Table();
    private Music music;
    private final ProfileGuiController controller;

    public ProfileMenuScreen() {
        this.controller = AppGuiControllers.profileGuiController;
        this.skin = GameAssetManager.skin;
        this.background = new Image(GameAssetManager.MenuTexture2);
        this.logo = new Image(GameAssetManager.logoTexture);
        this.menuTitle = new Label("Profile Menu", skin);
        this.username = new Label("Username : " + App.getApp().getLoggedInUser().getUserName(), skin);
        this.nickname = new Label("Nickname : " + App.getApp().getLoggedInUser().getNickname(), skin);
        this.maxCoin = new Label("Most Coin Earned : " + App.getApp().getLoggedInUser().getHighestMoneyEarnedInASingleGame(), skin);
        this.numberOfGamesPlayed = new Label("Number of Games Played : " + App.getApp().getLoggedInUser().getNumberOfGamesPlayed(), skin);
        if (App.getApp().getLoggedInUser().getIsMale()) {
            this.genderLabel = new Label("Gender : Male", skin);
        } else { this.genderLabel = new Label("Gender : Female", skin); }
        this.changeUsernameLabel = new Label("Change Username :", skin);
        this.changeUsernameField = new TextField("", skin);
        this.changeUsernameField.setMessageText("Enter your new Username");
        this.changeUsernameErrorLabel = new Label("", skin);
        this.changeUsernameButton = new TextButton("Change Username", skin);
        this.changeNicknameLabel = new Label("Change Nickname :", skin);
        this.changeNicknameField = new TextField("", skin);
        this.changeNicknameField.setMessageText("Enter your new Nickname");
        this.changeNicknameErrorLabel = new Label("", skin);
        this.changeNicknameButton = new TextButton("Change Nickname", skin);
        this.changePasswordLabel = new Label("Change Password :", skin);
        this.changePasswordField = new TextField("", skin);
        this.changePasswordField.setMessageText("Enter your new Password");
        this.changePasswordErrorLabel = new Label("", skin);
        this.changePasswordButton = new TextButton("Change Password", skin);
        this.changeEmailLabel = new Label("Change Email :", skin);
        this.changeEmailField = new TextField("", skin);
        this.changeEmailField.setMessageText("Enter your new Email");
        this.changeEmailErrorLabel = new Label("", skin);
        this.changeEmailButton = new TextButton("Change Email", skin);
        this.backButton = new TextButton("Back", skin);
        this.music = GameAssetManager.music1;
        AppGuiControllers.profileGuiController.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);
        music.setLooping(true);
        music.play();

        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(1, 1, 1, 0.5f);
        stage.addActor(background);

        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 1.5f);
        logo.setPosition(
                Gdx.graphics.getWidth() / 2f - logo.getWidth() / 2f,
                Gdx.graphics.getHeight() - logo.getHeight() - 10
        );
        stage.addActor(logo);

        table.setFillParent(true);
        table.top().padTop(225);

        menuTitle.setFontScale(1.5f);
        menuTitle.setColor(Color.valueOf("ffd60a"));
        table.add(menuTitle).colspan(2).center().padBottom(50);
        table.row();

        changeUsernameLabel.setColor(Color.valueOf("ffee99"));
        changePasswordLabel.setColor(Color.valueOf("ffee99"));
        changeNicknameLabel.setColor(Color.valueOf("ffee99"));
        changeEmailLabel.setColor(Color.valueOf("ffee99"));

        changeUsernameErrorLabel.setColor(Color.valueOf("d00000"));
        changeNicknameErrorLabel.setColor(Color.valueOf("d00000"));
        changePasswordErrorLabel.setColor(Color.valueOf("d00000"));
        changeEmailErrorLabel.setColor(Color.valueOf("d00000"));

        changeUsernameButton.setColor(Color.valueOf("a7c957"));
        changeNicknameButton.setColor(Color.valueOf("a7c957"));
        changePasswordButton.setColor(Color.valueOf("a7c957"));
        changeEmailButton.setColor(Color.valueOf("a7c957"));

        // Info display
        username.setFontScale(1.5f);
        username.setColor(Color.valueOf("ff7b00"));
        nickname.setFontScale(1.5f);
        nickname.setColor(Color.valueOf("ff8800"));
        genderLabel.setFontScale(1.5f);
        genderLabel.setColor(Color.valueOf("ff9500"));
        maxCoin.setFontScale(1.5f);
        maxCoin.setColor(Color.valueOf("ffa200"));
        numberOfGamesPlayed.setFontScale(1.5f);
        numberOfGamesPlayed.setColor(Color.valueOf("ffaa00"));

        table.add(username).center().colspan(2).padBottom(10).row();
        table.add(nickname).center().colspan(2).padBottom(10).row();
        table.add(genderLabel).center().colspan(2).padBottom(10).row();
        table.add(maxCoin).center().colspan(2).padBottom(10).row();
        table.add(numberOfGamesPlayed).center().colspan(2).padBottom(50).row();

        // ===================== Row 1: Change Username + Password =====================
        Table row1 = new Table();

        // === Left Column: Username ===
        Table usernameCol = new Table();
        usernameCol.add(changeUsernameLabel).left().row();
        usernameCol.add(changeUsernameField).width(700).padBottom(5).row();
        usernameCol.add(changeUsernameErrorLabel).left().padBottom(5).row();
        usernameCol.add(changeUsernameButton);

        // === Right Column: Password ===
        Table passwordCol = new Table();
        passwordCol.add(changePasswordLabel).left().row();
        passwordCol.add(changePasswordField).width(700).padBottom(5).row();
        passwordCol.add(changePasswordErrorLabel).left().padBottom(5).row();
        passwordCol.add(changePasswordButton);

        row1.add(usernameCol).padRight(100);
        row1.add(passwordCol);
        table.add(row1).padBottom(30).row();

        // ===================== Row 2: Change Nickname + Email =====================
        Table row2 = new Table();

        // === Left Column: Nickname ===
        Table nicknameCol = new Table();
        nicknameCol.add(changeNicknameLabel).left().row();
        nicknameCol.add(changeNicknameField).width(700).padBottom(5).row();
        nicknameCol.add(changeNicknameErrorLabel).left().padBottom(5).row();
        nicknameCol.add(changeNicknameButton);

        // === Right Column: Email ===
        Table emailCol = new Table();
        emailCol.add(changeEmailLabel).left().row();
        emailCol.add(changeEmailField).width(700).padBottom(5).row();
        emailCol.add(changeEmailErrorLabel).left().padBottom(5).row();
        emailCol.add(changeEmailButton);

        row2.add(nicknameCol).padRight(100);
        row2.add(emailCol);
        table.add(row2).padBottom(30).row();

        // ==== Back Button ====
        backButton.setColor(Color.valueOf("E9D8A6"));
        backButton.setPosition(20, 1300);
        stage.addActor(backButton);

        stage.addActor(table);
        controller.handleProfileMenu();
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

    // Auxiliary functions :


    public Stage getStage() {
        return stage;
    }

    public Label getMenuTitle() {
        return menuTitle;
    }

    public Label getUsername() {
        return username;
    }

    public Label getNickname() {
        return nickname;
    }

    public Label getMaxCoin() {
        return maxCoin;
    }

    public Label getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public Label getGenderLabel() {
        return genderLabel;
    }

    public TextField getChangeUsernameField() {
        return changeUsernameField;
    }

    public Label getChangeUsernameErrorLabel() {
        return changeUsernameErrorLabel;
    }

    public TextButton getChangeUsernameButton() {
        return changeUsernameButton;
    }

    public TextField getChangeNicknameField() {
        return changeNicknameField;
    }

    public Label getChangeNicknameErrorLabel() {
        return changeNicknameErrorLabel;
    }

    public TextButton getChangeNicknameButton() {
        return changeNicknameButton;
    }

    public TextField getChangePasswordField() {
        return changePasswordField;
    }

    public Label getChangePasswordErrorLabel() {
        return changePasswordErrorLabel;
    }

    public TextButton getChangePasswordButton() {
        return changePasswordButton;
    }

    public TextField getChangeEmailField() {
        return changeEmailField;
    }

    public Label getChangeEmailErrorLabel() {
        return changeEmailErrorLabel;
    }

    public TextButton getChangeEmailButton() {
        return changeEmailButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }
}
