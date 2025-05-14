package models.Enums;

public enum FriendshipLevel {
    LEVEL_0,
    LEVEL_1,
    LEVEL_2,
    LEVEL_3,
    LEVEL_4;

    public int requiredXp() {
        return 100 * (1 + this.ordinal());
    }

    public static FriendshipLevel fromOrdinal(int level) {
        return values()[Math.max(0, Math.min(level, values().length - 1))];
    }
}
