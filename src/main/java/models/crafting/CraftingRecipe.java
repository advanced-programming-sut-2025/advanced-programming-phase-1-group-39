package models.crafting;

import models.artisan.ArtisanMachine;
import models.artisan.ArtisanMachineType;
import models.artisan.BeeHouse;

import java.util.Map;

public enum CraftingRecipe {
        CHERRY_BOMB(new CraftingItem("Cherry Bomb", 50),
                Map.of("Copper Ore", 4, "Coal", 1),
                "Mining Level 1"),

        BOMB(new CraftingItem("Bomb", 50),
                Map.of("Iron Ore", 4, "Coal", 1),
                "Mining Level 2"),

        MEGA_BOMB(new CraftingItem("Mega Bomb", 50),
                Map.of("Gold Ore", 4, "Coal", 1),
                "Mining Level 3"),

        SPRINKLER(new CraftingItem("Sprinkler", 0),
                Map.of("Copper Bar", 1, "Iron Bar", 1),
                "Farming Level 1"),

        QUALITY_SPRINKLER(new CraftingItem("Quality Sprinkler", 0),
                Map.of("Iron Bar", 1, "Gold Bar", 1),
                "Farming Level 2"),

        IRIDIUM_SPRINKLER(new CraftingItem("Iridium Sprinkler", 0),
                Map.of("Gold Bar", 1, "Iridium Bar", 1),
                "Farming Level 3"),

        CHARCOAL_KILN(new ArtisanMachine("Charcoal Kiln", 0),
                Map.of("Wood", 20, "Copper Bar", 2),
                "Foraging Level 1"),

        FURNACE(new ArtisanMachine("Furnace", 0),
                Map.of("Copper Ore", 20, "Stone", 25),
                "-"),

        SCARECROW(new CraftingItem("Scarecrow", 0),
                Map.of("Wood", 50, "Coal", 1, "Fiber", 20),
                "-"),

        DELUXE_SCARECROW(new CraftingItem("Deluxe Scarecrow", 0),
                Map.of("Wood", 50, "Coal", 1, "Fiber", 20, "Iridium Ore", 1),
                "Farming Level 2"),

        BEE_HOUSE(new BeeHouse("Bee House", 0),
                Map.of("Wood", 40, "Coal", 8, "Iron Bar", 1),
                "Farming Level 1"),

        CHEESE_PRESS(new ArtisanMachine("Cheese Press", 0),
                Map.of("Wood", 45, "Stone", 45, "Copper Bar", 1),
                "Farming Level 2"),

        KEG(new ArtisanMachine("Keg", 0),
                Map.of("Wood", 30, "Copper Bar", 1, "Iron Bar", 1),
                "Farming Level 3"),

        LOOM(new ArtisanMachine("Loom", 0),
                Map.of("Wood", 60, "Fiber", 30),
                "Farming Level 3"),

        MAYONNAISE_MACHINE(new ArtisanMachine("Mayonnaise Machine", 0),
                Map.of("Wood", 15, "Stone", 15, "Copper Bar", 1),
                "-"),

        OIL_MAKER(new ArtisanMachine("Oil Maker", 0),
                Map.of("Wood", 100, "Gold Bar", 1, "Iron Bar", 1),
                "Farming Level 3"),

        PRESERVES_JAR(new ArtisanMachine("Preserves Jar", 0),
                Map.of("Wood", 50, "Stone", 40, "Coal", 8),
                "Farming Level 2"),

        DEHYDRATOR(new ArtisanMachine("Dehydrator", 0),
                Map.of("Wood", 30, "Stone", 20, "Fiber", 30),
                "Pierre's General Store"),

        FISH_SMOKER(new ArtisanMachine("Fish Smoker", 0),
                Map.of("Wood", 50, "Iron Bar", 3, "Coal", 10),
                "Fish Shop"),

        MYSTIC_TREE_SEED(new CraftingItem("Mystic Tree Seed", 100),
                Map.of("Acorn", 5, "Maple Seed", 5, "Pine Cone", 5, "Mahogany Seed", 5),
                "Foraging Level 4");

        private final CraftingItem data;
        private final Map<String, Integer> ingredients;
        private final String source;

        CraftingRecipe(CraftingItem data, Map<String, Integer> ingredients, String source) {
                this.data = data;
                this.ingredients = ingredients;
                this.source = source;
        }

        public CraftingItem getItem() {
                return data;
        }

        public Map<String, Integer> getIngredients() {
                return ingredients;
        }

        public String getSource() {
                return source;
        }

        public String getName() {
                return data.getName();
        }

        public int getSellPrice() {
                return data.getSellPrice();
        }

        public static CraftingRecipe getByName(String name) {
                for (CraftingRecipe recipe : values()) {
                        if (recipe.getName().equalsIgnoreCase(name)) return recipe;
                }
                return null;
        }

        @Override
        public String toString() {
                return "CraftingRecipe{" +
                        "data=" + data +
                        ", ingredients=" + ingredients +
                        ", source='" + source + '\'' +
                        '}';
        }
}
