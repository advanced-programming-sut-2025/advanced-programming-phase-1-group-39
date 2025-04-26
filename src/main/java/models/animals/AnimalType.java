package models.animals;

import java.util.ArrayList;
import java.util.List;

public enum AnimalType {
        // حیوانات قفسی (COOP animals)
        CHICKEN("Chicken", 800, LivingPlace.COOP,
                List.of(
                        new AnimalProduct("Egg", 50),
                        new AnimalProduct("Large Egg", 95)
                ),
                1),

        DUCK("Duck", 1200, LivingPlace.COOP,
                List.of(
                        new AnimalProduct("Duck Egg", 95),
                        new AnimalProduct("Duck Feather", 250)
                ),
                2),

        RABBIT("Rabbit", 8000, LivingPlace.COOP,
                List.of(
                        new AnimalProduct("Wool", 340),
                        new AnimalProduct("Rabbit's Foot", 565)
                ),
                4),

        DINOSAUR("Dinosaur", 14000, LivingPlace.COOP,
                List.of(
                        new AnimalProduct("Dinosaur Egg", 350)
                ),
                7),

        // حیوانات طویله (BARN animals)
        COW("Cow", 1500, LivingPlace.BARN,
                List.of(
                        new AnimalProduct("Milk", 125),
                        new AnimalProduct("Large Milk", 190)
                ),
                1),

        GOAT("Goat", 4000, LivingPlace.BARN,
                List.of(
                        new AnimalProduct("Goat Milk", 225),
                        new AnimalProduct("Large Goat Milk", 345)
                ),
                2),

        SHEEP("Sheep", 8000, LivingPlace.BARN,
                List.of(
                        new AnimalProduct("Wool", 340)
                ),
                3),

        PIG("Pig", 16000, LivingPlace.BARN,
                List.of(
                        new AnimalProduct("Truffle", 625)
                ),
                1); // Truffle در روزهای خاص، اما ساده گذاشتیم

        public final String displayName;
        public final int price;
        public final LivingPlace livingPlace;
        public final ArrayList<AnimalProduct> products;
        public final int produceCycleDays;

        AnimalType(String displayName, int price, LivingPlace livingPlace, List<AnimalProduct> products, int produceCycleDays) {
            this.displayName = displayName;
            this.price = price;
            this.livingPlace = livingPlace;
            this.products = new ArrayList<>(products);
            this.produceCycleDays = produceCycleDays;
        }

        public FarmAnimal create(String name, LivingPlace place) {
            return new FarmAnimal(this, name, price, place, products);
        }

}
