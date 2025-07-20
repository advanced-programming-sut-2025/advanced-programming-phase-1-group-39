package com.StardewValley.models.tools;

public enum FishingPoleType {
    TRAINING_ROD(0.1, 8, "Training_Rod.png"),
    BAMBOO_POLE(0.5, 8, "Bamboo_Pole.png"),
    FIBERGLASS_ROD(0.9, 6, "Fiberglass_Rod.png"),
    IRIDIUM_ROD(1.2, 4, "Iridium_Rod.png");

    private double multiplier;
    private int usingEnergy;
    private String path;

    FishingPoleType(double multiplier, int usingEnergy, String path) {
        this.multiplier = multiplier;
        this.usingEnergy = usingEnergy;
        this.path = path;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getUsingEnergy() {
        return usingEnergy;
    }

    public static FishingPoleType getType(String name) {
        if (name.equalsIgnoreCase("Bamboo Pole")) {
            return BAMBOO_POLE;
        }
        if (name.equalsIgnoreCase("Training Rod")) {
            return TRAINING_ROD;
        }
        if (name.equalsIgnoreCase("Fiberglass Rod")) {
            return FIBERGLASS_ROD;
        }
        if (name.equalsIgnoreCase("Iridium Rod")) {
            return IRIDIUM_ROD;
        }

        return null;
    }

    public String getPath() {
        return path;
    }
}