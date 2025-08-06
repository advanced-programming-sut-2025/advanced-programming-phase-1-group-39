package com.StardewValley.models;

public class GameSetting {
    private float playerSpeed = 1000f;
    private static float animalSpeed = 40f;

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public static float getAnimalSpeed() {
        return animalSpeed;
    }
}
