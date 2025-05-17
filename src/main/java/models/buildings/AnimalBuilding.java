package models.buildings;

import models.Location;
import models.animals.Animal;
import models.animals.LivingPlace;
import java.util.ArrayList;

public class AnimalBuilding extends Building {
    protected final LivingPlace type;
    protected final ArrayList<Animal> animals;

    public AnimalBuilding(String name, Location location, int width, int height, LivingPlace type) {
        super(name, location, width, height);
        this.type = type;
        this.animals = new ArrayList<>();
    }

    public int getCapacity() {
        if (type.getCapacity() - animals.size() <= 0) {
            return 0;
        }
        return type.getCapacity() - animals.size();
    }
    public Boolean hasCapacity(int amount) {
        if (amount <= (getCapacity() - animals.size())) {
            return true;
        }
        return false;
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

    public void endDay() {
        for (Animal animal : animals) {
            animal.endDay();
        }
    }
}

