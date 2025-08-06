package com.StardewValley.models.animals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

public enum AnimalType {
        // Coop
        CHICKEN("Chicken", 800, LivingPlace.COOP,
                List.of(
                        new AnimalProduct("Egg", 50),
                        new AnimalProduct("Large Egg", 95)
                ),
                1, "shops/MarniesRanch/Chicken.png"),

        DUCK("Duck", 1200, LivingPlace.BIG_COOP,
                List.of(
                        new AnimalProduct("Duck Egg", 95),
                        new AnimalProduct("Duck Feather", 250)
                ),
                2, "shops/MarniesRanch/Duck.png"),

        RABBIT("Rabbit", 8000, LivingPlace.DELUXE_COOP,
                List.of(
                        new AnimalProduct("Wool", 340),
                        new AnimalProduct("Rabbit's Foot", 565)
                ),
                4, "shops/MarniesRanch/Rabbit.png"),

        DINOSAUR("Dinosaur", 14000, LivingPlace.BIG_COOP,
                List.of(
                        new AnimalProduct("Dinosaur Egg", 350)
                ),
                7, "shops/MarniesRanch/Dinosaur.png"),

        // Barn
        COW("Cow", 1500, LivingPlace.BARN,
                List.of(
                        new AnimalProduct("Milk", 125),
                        new AnimalProduct("Large Milk", 190)
                ),
                1, "shops/MarniesRanch/Cow.png"),

        GOAT("Goat", 4000, LivingPlace.BIG_BARN,
                List.of(
                        new AnimalProduct("Goat Milk", 225),
                        new AnimalProduct("Large Goat Milk", 345)
                ),
                2, "shops/MarniesRanch/Goat.png"),

        SHEEP("Sheep", 8000, LivingPlace.DELUXE_BARN,
                List.of(
                        new AnimalProduct("Wool", 340)
                ),
                3, "shops/MarniesRanch/Sheep.png"),

        PIG("Pig", 16000, LivingPlace.DELUXE_BARN,
                List.of(
                        new AnimalProduct("Truffle", 625)
                ),
                1, "shops/MarniesRanch/Pig.png");

        public final String displayName;
        public final int price;
        public final LivingPlace livingPlace;
        public final ArrayList<AnimalProduct> products;
        public final int produceCycleDays;

        public final String path;
        public transient TextureRegion textureRegion;

        AnimalType(String displayName, int price, LivingPlace livingPlace, List<AnimalProduct> products, int produceCycleDays, String path) {
            this.displayName = displayName;
            this.price = price;
            this.livingPlace = livingPlace;
            this.products = new ArrayList<>(products);
            this.produceCycleDays = produceCycleDays;

            this.path = path;
        }

        public Animal create(String name) {
            return new Animal(this, name, price, livingPlace, products);
        }

        public static List<AnimalProduct> getAllAnimalProducts() {
                List<AnimalProduct> all = new ArrayList<>();
                for (AnimalType type : AnimalType.values()) {
                        all.addAll(type.products);
                }
                return all;
        }

        public static AnimalType getType(String input) {
                for (AnimalType type : values()) {
                        if (type.name().equalsIgnoreCase(input)) {
                                return type;
                        }
                }
                return null;
        }

        public static void loadAllTextures() {
                for (AnimalType type : values()) {
                        Texture texture = new Texture(Gdx.files.internal(type.path));
                        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                        type.textureRegion = new TextureRegion(texture);
                }
        }

        public TextureRegion getTextureRegion() {
                return textureRegion;
        }
}
