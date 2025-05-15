package models.trading;

public enum TradeType {
    REQUEST,
    OFFER;

    public static TradeType getTypeByString(String name) {
        for (TradeType type : values()) {
            if (name.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }
}
