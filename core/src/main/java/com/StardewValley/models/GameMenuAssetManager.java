package com.StardewValley.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Map;

public class GameMenuAssetManager {

    public static Skin skin =  new Skin(Gdx.files.internal("skin1/skin/pixthulhu-ui.json"));
    //public static Skin skin = new Skin(Gdx.files.internal("skin2/uiskin.json"));
    public static Texture logoTexture = new Texture("Stardew_Valley_Images-main/sprites/Logo No Background.png");
    public static Texture MenuTexture = new Texture("Stardew_Valley_Images-main/sprites/Panorama.png");;

    public static Music music1 = Gdx.audio.newMusic(Gdx.files.internal("musics/01. Stardew Valley Overture.mp3"));

    private GameMenuAssetManager() {

    }



}
