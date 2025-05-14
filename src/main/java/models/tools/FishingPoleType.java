package models.tools;

public enum FishingPoleType {
    TRAINING_ROD(0.1, 8),
    BAMBOO_POLE(0.5, 8),
    FIBERGLASS_ROD(0.9, 6),
    IRIDIUM_ROD(1.2, 4);

    private double multiplier;
    private int usingEnergy;

    FishingPoleType(double multiplier, int usingEnergy) {
        this.multiplier = multiplier;
        this.usingEnergy = usingEnergy;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getUsingEnergy() {
        return usingEnergy;
    }
}