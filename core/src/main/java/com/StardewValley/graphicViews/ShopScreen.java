package com.StardewValley.graphicViews;

import com.StardewValley.Main;
import com.StardewValley.models.Shops.Shop;
import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ShopScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Shop shop;

    public ShopScreen(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = GameAssetManager.skin;

        shop.showShopMenu(stage, skin);
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Main.getMain().setScreen(GameScreen.getScreen());
        }

        stage.act(delta);
        stage.draw();
    }


    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
