package models.inventory;

public enum TrashType {
    BASIC(0), COPPER(0.15), IRON(0.3), GOLD(0.45), IRIDIUM(0.6);

    public double quantifier;

    TrashType(double quantifier) {
        this.quantifier = quantifier;
    }

}
