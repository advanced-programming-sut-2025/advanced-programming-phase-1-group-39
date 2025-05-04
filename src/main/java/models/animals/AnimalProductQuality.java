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

    public static AnimalProductQuality fromScore(double score) {
        if (score < 0.5) return NORMAL;
        if (score < 0.7) return SILVER;
        if (score < 0.9) return GOLD;
        return IRIDIUM;
    }
}
