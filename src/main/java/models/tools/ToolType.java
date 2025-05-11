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

    public static ToolType getNext(ToolType type) {
        switch (type) {
            case BASIC -> {
                return COPPER;
            }
            case COPPER -> {
                return IRON;
            }
            case IRON -> {
                return GOLD;
            }
            case GOLD, IRIDIUM -> {
                return IRIDIUM;
            }
        }
        return null;
    }
    public static ToolType fromString(String input) {
        for (ToolType type : values()) {
            if (input.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }
}