package models.tools;

public enum FishingPoleType {
    TRAINING_ROD(0.1),
    BAMBOO_POLE(0.5),
    FIBERGLASS_ROD(0.9),
    IRIDIUM_ROD(1.2);

    private double rate;

    FishingPoleType(double multiplier) {
        this.rate = multiplier;
    }

    public double getRate() {
        return rate;
    }
}