package models.cropsAndFarming;

public enum FertilizerType {
    SPEED,
    QUALITY;

    public static FertilizerType getType(String input) {
        if (input.equalsIgnoreCase("Deluxe Retaining Soil") ||
                input.equalsIgnoreCase("Basic Retaining Soil")
                || input.equalsIgnoreCase("Quality Retaining Soil")) {
            return QUALITY;
        }
        if (input.equalsIgnoreCase("Speed-Gro")) {
            return SPEED;
        }
        return null;
    }
}
