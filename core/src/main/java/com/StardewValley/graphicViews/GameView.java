package com.StardewValley.graphicViews;

import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.GameController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameView implements Screen {
    private GameController controller;
    private GameMenuInputAdapter gameMenuInputAdapter;
    private SpriteBatch batch;
    private TextureRegion[][] tileTextures;



    public GameView() {
        this.controller = AppControllers.gameController;
        gameMenuInputAdapter = new GameMenuInputAdapter(controller);
        Gdx.input.setInputProcessor(gameMenuInputAdapter);
        batch = new SpriteBatch();
        loadTextures();
    }

    public void loadTextures() {}

    public void renderTiles() {}

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.setProjectionMatrix(game.getCamera().combined);
        batch.begin();
        renderTiles();
        batch.end();
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
