package com.StardewValley.models.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class GameAssetManager {
    public static Skin skin;
    public static Texture titleImage;
    //public static Skin skin = new Skin(Gdx.files.internal("skin2/uiskin.json"));
    public static Texture logoTexture = new Texture("Stardew_Valley_Images-main/sprites/Logo No Background.png");
    public static Texture MenuTexture = new Texture("Stardew_Valley_Images-main/sprites/Panorama.png");
    public static Texture MenuTexture2 = new Texture("Stardew_Valley_Images-main/sprites/pixel-art-river-landscape-illustration (1).jpg");

    public static Label.LabelStyle messageBoxStyle;

    public static Music music1 = Gdx.audio.newMusic(Gdx.files.internal("musics/01. Stardew Valley Overture.mp3"));



    public static void initializeAssets() {
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        titleImage = new Texture(Gdx.files.internal("title-logo.png"));

        setMessageBoxStyle();
    }

    public static TextureAtlas getCropsAtlas() {
        return new TextureAtlas(Gdx.files.internal("crops/crops.atlas"));
    }

    public static TextureAtlas getForagingsAtlas() {
        return new TextureAtlas(Gdx.files.internal("foragings/foragings.atlas"));
    }

    public static TextureAtlas getTressAtlas() {
        return new TextureAtlas(Gdx.files.internal("trees/trees.atlas"));
    }

    public static void setMessageBoxStyle() {
        Texture messageBoxTexture = new Texture(Gdx.files.internal("box.9.png"));
        NinePatch ninePatch = new NinePatch(messageBoxTexture, 1, 1, 1, 1);
        NinePatchDrawable messageBoxDrawable = new NinePatchDrawable(ninePatch);

        messageBoxStyle = new Label.LabelStyle();
        messageBoxStyle.font = skin.getFont("font");
        messageBoxStyle.fontColor = Color.BLACK;
        messageBoxStyle.background = messageBoxDrawable;

        messageBoxStyle.background.setLeftWidth(30);
        messageBoxStyle.background.setRightWidth(30);
        messageBoxStyle.background.setTopHeight(30);
        messageBoxStyle.background.setBottomHeight(30);

        messageBoxStyle.background.setMinWidth(450);
        messageBoxStyle.background.setMinHeight(100);
    }
}
