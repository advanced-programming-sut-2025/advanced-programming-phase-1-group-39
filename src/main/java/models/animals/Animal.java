package models.animals;

public abstract class Animal {
    AnimalType type;

    public Animal(AnimalType type) {
        this.type = type;
    }
}
