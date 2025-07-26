package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.graphicControllers.SecurityQuestionController;
import com.StardewValley.models.App;
import com.StardewValley.models.GameMenuAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SecurityQuestionMenuScreen implements Screen {
    private Stage stage;
    private final Skin skin;

    private final Image background;
    private final Image logo;
    private final Label securityQuestionLabel;

    private CheckBox question1;
    private CheckBox question2;
    private CheckBox question3;
    private CheckBox question4;
    private CheckBox question5;

    private final Label answerLabel;
    private TextField answer;
    private  Label answerErrorLabel;

    private TextButton registerButton;
    private TextButton backButton;

    public Table table = new Table();
    private Music music;
    private final SecurityQuestionController controller;

    public SecurityQuestionMenuScreen() {
        this.controller = AppControllers.securityQuestionController;
        this.skin = GameMenuAssetManager.skin;
        this.background = new Image(GameMenuAssetManager.MenuTexture);
        this.logo = new Image(GameMenuAssetManager.logoTexture);
        this.securityQuestionLabel = new Label("Chose your Security Question :", skin);
        this.question1 = new CheckBox("1- What is your dream job?", skin);
        this.question2 = new CheckBox("2- What was the name of your favorite teacher in school?", skin);
        this.question3 = new CheckBox("3- If you could have dinner with any historical figure, who would it be?", skin);
        this.question4 = new CheckBox("4- What was your first school's name?", skin);
        this.question5 = new CheckBox("5- What was the model of your very first phone?", skin);
        this.answerLabel = new Label("Answer your Question:", skin);
        this.answer = new TextField("", skin);
        this.answer.setMessageText("Enter your Answer");
        this.answerErrorLabel = new Label("", skin);
        this.registerButton = new TextButton("Register", skin);
        this.backButton = new TextButton("Back", skin);
        this.music = App.getApp().getMusic();
        AppControllers.securityQuestionController.setView(this);
    }


    @Override
    public void show() {
        music.setLooping(true);
        music.play();

        stage = new Stage(new FillViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

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
        table.top().padTop(50).center();

        // Title Label
        securityQuestionLabel.setColor(Color.valueOf("ffd60a"));
        securityQuestionLabel.setFontScale(1.5f);
        table.add(securityQuestionLabel).colspan(2).center().padBottom(100);
        table.row();

        // ==== Checkboxes ====
        Table checkboxTable = new Table();
        checkboxTable.add(question1).left().padBottom(10).row();
        checkboxTable.add(question2).left().padBottom(10).row();
        checkboxTable.add(question3).left().padBottom(10).row();
        checkboxTable.add(question4).left().padBottom(10).row();
        checkboxTable.add(question5).left();
        table.add(checkboxTable).colspan(2);
        table.row().padTop(50);

        // ==== Answer Input ====
        answerLabel.setColor(Color.valueOf("023047"));
        table.add(answerLabel).center().colspan(2);
        table.row();
        table.add(answer).width(800).colspan(2);
        table.row();

        answerErrorLabel.setColor(Color.valueOf("ff002b"));
        table.add(answerErrorLabel).center().colspan(2);
        table.row().padTop(30);

        // ==== Buttons ====
        registerButton.setColor(Color.valueOf("E9D8A6"));
        backButton.setColor(Color.valueOf("E9D8A6"));

        Table buttonRow = new Table();
        buttonRow.add(registerButton).width(350).padRight(20);
        table.add(buttonRow).colspan(2);
        table.row();

        backButton.setPosition(20, 1300);
        stage.addActor(backButton);

        stage.addActor(table);

        controller.handleSecurityMenu();
    }

    @Override
    public void render(float v) {
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


    public CheckBox getQuestion1() {
        return question1;
    }

    public CheckBox getQuestion2() {
        return question2;
    }

    public CheckBox getQuestion3() {
        return question3;
    }

    public CheckBox getQuestion4() {
        return question4;
    }

    public CheckBox getQuestion5() {
        return question5;
    }

    public TextField getAnswer() {
        return answer;
    }

    public Label getAnswerErrorLabel() {
        return answerErrorLabel;
    }

    public TextButton getRegisterButton() {
        return registerButton;
    }

    public TextButton getBackButton() {
        return backButton;
    }
}
