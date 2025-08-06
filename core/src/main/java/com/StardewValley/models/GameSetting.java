package com.StardewValley.models;

public class GameSetting {
    private float playerSpeed = 1000f;
    private static float animalSpeed = 40f;

    private static final float MAX_NIGHT_ALPHA = 0.7f;


    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public static float getAnimalSpeed() {
        return animalSpeed;
    }

    public static float getMaxNightAlpha() {
        return MAX_NIGHT_ALPHA;
    }
}
