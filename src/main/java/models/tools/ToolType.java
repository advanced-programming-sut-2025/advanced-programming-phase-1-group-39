package models.tools;

public enum ToolType {
    BASIC(0), COPPER(1), IRON(2), GOLD(3), IRIDIUM(4);

    private final int energyReduce;

    ToolType(int energyReduce) {
        this.energyReduce = energyReduce;
    }

    public int getEnergyReduce() {
        return energyReduce;
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