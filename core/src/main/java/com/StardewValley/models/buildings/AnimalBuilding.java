package com.StardewValley.models.buildings;

import com.StardewValley.models.Location;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.animals.LivingPlace;

import java.util.ArrayList;
import java.util.Random;

public class AnimalBuilding extends Building {
    protected final LivingPlace type;
    protected final ArrayList<Animal> animals;

    public float stateTime = 0;

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

    public Location getRandomLocationInside() {
        int randX = new Random().nextInt(getWidth() - 2) + getLocation().x() + 1;
        int randY = new Random().nextInt(getHeight() - 2) + getLocation().y() + 1;
        return new Location(randX, randY);
    }

    public boolean addAnimalAndSetLocationInside(Animal animal) {
        if (animals.size() >= getCapacity()) return false;
        animal.setLocation(getRandomLocationInside());
        return animals.add(animal);
    }
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }
    public boolean hasAnimal(Animal animal) {
        return animals.contains(animal);
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

