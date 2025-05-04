package models.animals;

public enum AnimalProductQuality {
    NORMAL(1),
    SILVER(1.25),
    GOLD(1.5),
    IRIDIUM(2);

    double rate;

    AnimalProductQuality(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
