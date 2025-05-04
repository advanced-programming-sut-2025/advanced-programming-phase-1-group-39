package models.buildings;

import models.animals.LivingPlace;
import java.util.ArrayList;
import java.util.List;
import models.animals.Animal;

public abstract class AnimalBuilding extends Building {
    protected final LivingPlace type;
    protected final ArrayList<Animal> animals;

    public AnimalBuilding(String name, LivingPlace type) {
        super(name);
        this.type = type;
        this.animals = new ArrayList<>();
    }

    public int getCapacity() {
        return type.getCapacity();
    }

    public boolean addAnimal(Animal animal) {
        if (animals.size() >= getCapacity()) return false;
        return animals.add(animal);
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public LivingPlace getType() {
        return type;
    }
}

