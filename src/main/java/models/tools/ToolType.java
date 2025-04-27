package models.tools;

public enum ToolType {
    BASIC(5), COPPER(4), IRON(3), GOLD(2), IRIDIUM(1);

    private final int baseEnergyCost;

    ToolType(int baseEnergyCost) {
        this.baseEnergyCost = baseEnergyCost;
    }

    public int getBaseEnergyCost() {
        return baseEnergyCost;
    }
}
