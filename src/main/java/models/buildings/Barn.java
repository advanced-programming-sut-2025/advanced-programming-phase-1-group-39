package models.buildings;

import models.animals.LivingPlace;

public class Barn extends AnimalBuilding {
    public Barn(LivingPlace type) {
        super("Barn", type);
    }
}

