package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.SignupMenuController;
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

public class SignupMenuView implements Screen {
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
    private Label passwordErrorLabel;

    private Label confirmPasswordLabel;
    private TextField confirmPasswordField;
    private Label confirmPasswordErrorLabel;

    private Label nicknameLabel;
    private TextField nicknameField;
    private Label nicknameErrorLabel;

    private Label emailLabel;
    private TextField emailField;
    private Label emailErrorLabel;

    private final Label genderLabel;
    private final SelectBox<String> genderField;

    private TextButton signUpButton;
    private TextButton loginButton;
    private TextButton exitButton;
    private TextButton randomPasswordButton;

    public Table table = new Table();
    private Music music;
    private final SignupMenuController controller;

    public SignupMenuView() {
        this.controller = AppControllers.signupMenuController;
        this.skin = GameMenuAssetManager.skin;
        this.background = new Image(GameMenuAssetManager.MenuTexture);
        this.logo = new Image(GameMenuAssetManager.logoTexture);
        this.menuTitle = new Label("Sign Up", skin);
        this.usernameLabel = new Label("Username :", skin);
        this.usernameField = new TextField("Enter your Username", skin);
        this.usernameErrorLabel = new Label("", skin);
        this.passwordLabel = new Label("Password :", skin);
        this.passwordField = new TextField("Enter your Password", skin);
        this.passwordErrorLabel = new Label("", skin);
        this.confirmPasswordLabel = new Label("Confirm Password :", skin);
        this.confirmPasswordField = new TextField(" Re-enter your password", skin);
        this.confirmPasswordErrorLabel = new Label("", skin);
        this.nicknameLabel = new Label("Nickname :", skin);
        this.nicknameField = new TextField("Enter your Nickname", skin);
        this.nicknameErrorLabel = new Label("", skin);
        this.emailLabel = new Label("Email :", skin);
        this.emailField = new TextField("Enter your Email", skin);
        this.emailErrorLabel = new Label("", skin);
        this.genderLabel = new Label("Gender :", skin);
        this.genderField = new SelectBox<>(skin);
        this.genderField.setItems("Male", "Female");
        this.signUpButton = new TextButton("Sign Up", skin);
        this.loginButton = new TextButton("Login", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.randomPasswordButton = new TextButton("Random", skin);
        this.music = GameMenuAssetManager.music1;
        AppControllers.signupMenuController.setView(this);
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // لوگو بالا قرار بگیره
        logo.setSize(logo.getWidth() * 1.5f, logo.getHeight() * 1.5f);
        logo.setPosition(
                Gdx.graphics.getWidth() / 2f - logo.getWidth() / 2f,
                Gdx.graphics.getHeight() - logo.getHeight() - 10
        );

        // بک‌گراند
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(1, 1, 1, 0.5f);
        stage.addActor(background);

        // تنظیمات جدول اصلی
        table.setFillParent(true);
        table.top().padTop(100);
        table.center();

//        menuTitle.setFontScale(2.5f);
//        menuTitle.setColor(Color.valueOf("ffc750"));
//        table.add(menuTitle).colspan(2).center().padBottom(40);
//        table.row().pad(10, 0, 10, 0);

        // ==== Username ====
        usernameLabel.setColor(Color.valueOf("023047"));
        table.add(usernameLabel).left().colspan(0);
        table.row();
        table.add(usernameField).width(750).colspan(2);
        table.row();
        usernameErrorLabel.setColor(Color.valueOf("ff002b"));
        table.add(usernameErrorLabel).left().colspan(2);
        table.row().padTop(20);

        // ==== Password ====
        passwordLabel.setColor(Color.valueOf("023047"));
        table.add(passwordLabel).left().colspan(2);
        table.row();
        table.add(passwordField).width(750).colspan(2);
        table.row();
        passwordErrorLabel.setColor(Color.valueOf("ff002b"));
        table.add(passwordErrorLabel).left().colspan(2);
        table.row().padTop(20);
        randomPasswordButton.setColor(Color.valueOf("E9D8A6"));
        randomPasswordButton.setPosition(1550, 900);


        // ==== Confirm Password ====
        confirmPasswordLabel.setColor(Color.valueOf("023047"));
        table.add(confirmPasswordLabel).left().colspan(2);
        table.row();
        table.add(confirmPasswordField).width(750).colspan(2);
        table.row();
        confirmPasswordErrorLabel.setColor(Color.valueOf("ff002b"));
        table.add(confirmPasswordErrorLabel).left().colspan(2);
        table.row().padTop(20);

        // ==== Nickname ====
        nicknameLabel.setColor(Color.valueOf("023047"));
        table.add(nicknameLabel).left().colspan(2);
        table.row();
        table.add(nicknameField).width(750).colspan(2);
        table.row();
        nicknameErrorLabel.setColor(Color.valueOf("ff002b"));
        table.add(nicknameErrorLabel).left().colspan(2);
        table.row().padTop(20);

        // ==== Email ====
        emailLabel.setColor(Color.valueOf("023047"));
        table.add(emailLabel).left().colspan(2);
        table.row();
        table.add(emailField).width(750).colspan(2);
        table.row();
        emailErrorLabel.setColor(Color.valueOf("ff002b"));
        table.add(emailErrorLabel).left().colspan(2);
        table.row().padTop(20);

        // ==== Gender ====
        genderLabel.setColor(Color.valueOf("023047"));
        table.add(genderLabel).left().colspan(2);
        table.row();
        table.add(genderField).width(750).colspan(2);
        table.row().padTop(30);

        // ==== Buttons ====
        signUpButton.setColor(Color.valueOf("E9D8A6"));
        loginButton.setColor(Color.valueOf("E9D8A6"));
        exitButton.setColor(Color.valueOf("E9D8A6"));

        Table buttonRow = new Table();
        buttonRow.add(signUpButton).width(250).padRight(20);
        buttonRow.add(loginButton).width(250);
        table.add(buttonRow).colspan(2).padTop(20);
        table.row();

        //exitButton.setColor(Color.FIREBRICK);
        exitButton.setPosition(20, 1300);  // فاصله از لبه پایین و چپ
        stage.addActor(exitButton);

        // اضافه کردن به استیج
        stage.addActor(logo);
        stage.addActor(table);
        stage.addActor(randomPasswordButton);

        controller.handleSignup();
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


    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getNicknameField() {
        return nicknameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public SelectBox<String> getGenderField() {
        return genderField;
    }

    public TextButton getSignUpButton() {
        return signUpButton;
    }

    public TextButton getLoginButton() {
        return loginButton;
    }

    public TextButton getExitButton() {
        return exitButton;
    }

    public TextButton getRandomPasswordButton() {
        return randomPasswordButton;
    }


    public Label getUsernameErrorLabel() {
        return usernameErrorLabel;
    }

    public Label getPasswordErrorLabel() {
        return passwordErrorLabel;
    }

    public Label getNicknameErrorLabel() {
        return nicknameErrorLabel;
    }

    public Label getEmailErrorLabel() {
        return emailErrorLabel;
    }

    public Label getConfirmPasswordErrorLabel() {
        return confirmPasswordErrorLabel;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsernameErrorLabel(Label usernameErrorLabel) {
        this.usernameErrorLabel = usernameErrorLabel;
    }

    public void setPasswordErrorLabel(Label passwordErrorLabel) {
        this.passwordErrorLabel = passwordErrorLabel;
    }

    public void setConfirmPasswordErrorLabel(Label confirmPasswordErrorLabel) {
        this.confirmPasswordErrorLabel = confirmPasswordErrorLabel;
    }

    public void setNicknameErrorLabel(Label nicknameErrorLabel) {
        this.nicknameErrorLabel = nicknameErrorLabel;
    }

    public void setEmailErrorLabel(Label emailErrorLabel) {
        this.emailErrorLabel = emailErrorLabel;
    }

    public void setUsernameField(TextField usernameField) {
        this.usernameField = usernameField;
    }

    public void setPasswordField(TextField passwordField) {
        this.passwordField = passwordField;
    }

    public void setConfirmPasswordField(TextField confirmPasswordField) {
        this.confirmPasswordField = confirmPasswordField;
    }

    public void setNicknameField(TextField nicknameField) {
        this.nicknameField = nicknameField;
    }

    public void setEmailField(TextField emailField) {
        this.emailField = emailField;
    }
}
