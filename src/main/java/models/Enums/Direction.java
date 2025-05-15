package models.Enums;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UPRIGHT(1, -1),
    UPLEFT(-1, -1),
    DOWNRIGHT(1, 1),
    DOWNLEFT(-1, 1);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction getDirection(String name) {
        for (Direction dir : Direction.values()) {
            if (dir.name().equalsIgnoreCase(name)) return dir;
        }
        return null;
    }
}
