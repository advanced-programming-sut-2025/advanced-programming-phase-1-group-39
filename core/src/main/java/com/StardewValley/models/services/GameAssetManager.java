package com.StardewValley.models.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    public static Skin skin;
    public static Texture titleImage;

    public static void initializeAssets() {
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        titleImage = new Texture(Gdx.files.internal("title-logo.png"));
    }
}
