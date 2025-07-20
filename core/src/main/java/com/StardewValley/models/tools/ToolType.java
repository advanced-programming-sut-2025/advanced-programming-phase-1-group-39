package com.StardewValley.models.tools;

public enum ToolType {
    BASIC(0, ""), COPPER(1, "Copper_"), IRON(2, "Steel_"), GOLD(3, "Gold_"), IRIDIUM(4, "Iridium_");

    private final int energyReduce;
    private final String prefixForTexture;

    ToolType(int energyReduce, String prefix) {
        this.energyReduce = energyReduce;
        this.prefixForTexture = prefix;
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

    public String getPrefixForTexture() {
        return prefixForTexture;
    }
}