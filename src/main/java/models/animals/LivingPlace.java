package models.animals;

public enum LivingPlace {
    COOP(4),
    BIG_COOP(8),
    DELUXE_COOP(12),
    BARN(4),
    BIG_BARN(8),
    DELUXE_BARN(12);

    int capacity;

    LivingPlace(int capacity) {
        this.capacity = capacity;
    }
}
