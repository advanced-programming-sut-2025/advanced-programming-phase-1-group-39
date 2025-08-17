package com.StardewValley.models;

import com.StardewValley.models.services.GameAssetManager;
import com.badlogic.gdx.audio.Music;

public class GameSetting {
    private float playerSpeed = 1000f;
    private static float animalSpeed = 40f;

    private static final float MAX_NIGHT_ALPHA = 0.7f;

//    private static Music music = GameAssetManager.music1;


    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public static float getAnimalSpeed() {
        return animalSpeed;
    }

    public static float getMaxNightAlpha() {
        return MAX_NIGHT_ALPHA;
    }

//    public static Music getMusic() {
//        return music;
//    }

//    public static void setMusic(Music music) {
//        GameSetting.music = music;
//    }
}
