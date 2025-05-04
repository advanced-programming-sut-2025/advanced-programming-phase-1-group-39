package models.buildings;

import models.animals.FarmAnimal;
import models.animals.LivingPlace;
import java.util.ArrayList;
import java.util.List;
import models.animals.Animal;

public class AnimalBuilding extends Building {
    protected final LivingPlace type;
    protected final ArrayList<FarmAnimal> animals;

    public AnimalBuilding(String name, LivingPlace type) {
        super(name);
        this.type = type;
        this.animals = new ArrayList<>();
    }

    public int getCapacity() {
        return type.getCapacity();
    }

    public boolean addAnimal(FarmAnimal animal) {
        if (animals.size() >= getCapacity()) return false;
        return animals.add(animal);
    }

    public ArrayList<FarmAnimal> getAnimals() {
        return animals;
    }

    public LivingPlace getType() {
        return type;
    }

    public void endDay() {
        for (FarmAnimal animal : animals) {
            animal.endDay();
        }
    }
}

