package models.animals;

import models.Item;

import java.util.ArrayList;

public class FarmAnimal extends Animal {
    String name;
    int price;
    LivingPlace place;

    ArrayList<AnimalProduct> products;

    int petPercentage = 0;
    int hungrinessPercentage = 0;

    public FarmAnimal(AnimalType type, String name, int price, LivingPlace place, ArrayList<AnimalProduct> products) {
        super(type);
        this.name = name;
        this.price = price;
        this.type = type;
        this.place = place;

        this.products = products;
    }

    public void produce() {}
    public void collectProduct() {}
}
