package models.animals;

public enum LivingPlace {
    COOP(4, 5, 5),
    BIG_COOP(8, 6, 6),
    DELUXE_COOP(12, 7, 7),
    BARN(4, 6, 8),
    BIG_BARN(8, 7, 9),
    DELUXE_BARN(12, 8, 10);

    private int capacity;
    private int width;
    private int height;

    LivingPlace(int capacity, int width, int height) {
        this.capacity = capacity;
        this.width = width;
        this.height = height;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static LivingPlace fromString(String name) {
        for (LivingPlace type : values()) {
            if (name.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }
}