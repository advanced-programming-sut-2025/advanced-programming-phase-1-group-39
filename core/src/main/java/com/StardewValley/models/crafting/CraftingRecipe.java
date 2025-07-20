package com.StardewValley.models.crafting;

import com.StardewValley.models.artisan.*;
import com.StardewValley.models.artisan.*;
import com.StardewValley.models.crafting.CraftingItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Map;

public enum CraftingRecipe {
        CHERRY_BOMB(new CraftingItem("Cherry Bomb", 50,
                new Texture(Gdx.files.internal("crafting/Cherry_Bomb.png"))),
                Map.of("Copper Ore", 4, "Coal", 1), "Mining Level 1"),

        BOMB(new CraftingItem("Bomb", 50, new Texture(Gdx.files.internal("crafting/Bomb.png"))),
                Map.of("Iron Ore", 4, "Coal", 1),
                "Mining Level 2"),

        MEGA_BOMB(new CraftingItem("Mega Bomb", 50, new Texture(Gdx.files.internal("crafting/Mega_Bomb.png"))),
                Map.of("Gold Ore", 4, "Coal", 1),
                "Mining Level 3"),

        SPRINKLER(new CraftingItem("Sprinkler", 0, new Texture(Gdx.files.internal("crafting/Sprinkler.png"))),
                Map.of("Copper Bar", 1, "Iron Bar", 1),
                "Farming Level 1"),

        QUALITY_SPRINKLER(new CraftingItem("Quality Sprinkler", 0, new Texture(Gdx.files.internal("crafting/Quality_Sprinkler.png"))),
                Map.of("Iron Bar", 1, "Gold Bar", 1),
                "Farming Level 2"),

        IRIDIUM_SPRINKLER(new CraftingItem("Iridium Sprinkler", 0, new Texture(Gdx.files.internal("crafting/Iridium_Sprinkler.png"))),
                Map.of("Gold Bar", 1, "Iridium Bar", 1),
                "Farming Level 3"),

        CHARCOAL_KILN(new CharcoalKiln("Charcoal Kiln", 0, new Texture(Gdx.files.internal("crafting/Charcoal_Kiln.png"))),
                Map.of("Wood", 20, "Copper Bar", 2),
                "Foraging Level 1"),

        SCARECROW(new CraftingItem("Scarecrow", 0, new Texture(Gdx.files.internal("crafting/Scarecrow.png"))),
                Map.of("Wood", 50, "Coal", 1, "Fiber", 20),
                "-"),

        DELUXE_SCARECROW(new CraftingItem("Deluxe Scarecrow", 0, new Texture(Gdx.files.internal("crafting/Deluxe_Scarecrow.png"))),
                Map.of("Wood", 50, "Coal", 1, "Fiber", 20, "Iridium Ore", 1),
                "Farming Level 2"),

        FURNACE(new Furnace("Furnace", 0, new Texture(Gdx.files.internal("crafting/Furnace.png"))),
                Map.of("Copper Ore", 20, "Stone", 25),
                "-"),

        BEE_HOUSE(new BeeHouse("Bee House", 0, new Texture(Gdx.files.internal("crafting/Bee_House.png"))),
                Map.of("Wood", 40, "Coal", 8, "Iron Bar", 1),
                "Farming Level 1"),

        CHEESE_PRESS(new CheesePress("Cheese Press", 0, new Texture(Gdx.files.internal("crafting/Cheese_Press.png"))),
                Map.of("Wood", 45, "Stone", 45, "Copper Bar", 1),
                "Farming Level 2"),

        KEG(new Keg("Keg", 0, new Texture(Gdx.files.internal("crafting/Keg.png"))),
                Map.of("Wood", 30, "Copper Bar", 1, "Iron Bar", 1),
                "Farming Level 3"),

        LOOM(new Loom("Loom", 0, new Texture(Gdx.files.internal("crafting/Loom.png"))),
                Map.of("Wood", 60, "Fiber", 30),
                "Farming Level 3"),

        MAYONNAISE_MACHINE(new MayonnaiseMachine("Mayonnaise Machine", 0, new Texture(Gdx.files.internal("crafting/Mayonnaise_Machine.png"))),
                Map.of("Wood", 15, "Stone", 15, "Copper Bar", 1),
                "-"),

        OIL_MAKER(new OilMaker("Oil Maker", 0, new Texture(Gdx.files.internal("crafting/Oil_maker.png"))),
                Map.of("Wood", 100, "Gold Bar", 1, "Iron Bar", 1),
                "Farming Level 3"),

        PRESERVES_JAR(new PreservesJar("Preserves Jar", 0, new Texture(Gdx.files.internal("crafting/Preserves_Jar.png"))),
                Map.of("Wood", 50, "Stone", 40, "Coal", 8),
                "Farming Level 2"),

        DEHYDRATOR(new Dehydrator("Dehydrator", 0, new Texture(Gdx.files.internal("crafting/Dehydrator.png"))),
                Map.of("Wood", 30, "Stone", 20, "Fiber", 30),
                "Pierre's General Store"),

        FISH_SMOKER(new FishSmoker("Fish Smoker", 0, new Texture(Gdx.files.internal("crafting/Fish_Smoker.png"))),
                Map.of("Wood", 50, "Iron Bar", 3, "Coal", 10),
                "Fish Shop"),

        MYSTIC_TREE_SEED(new CraftingItem("Mystic Tree Seed", 100, new Texture(Gdx.files.internal("crafting/Mystic_Tree_Seed.png"))),
                Map.of("Acorn", 5, "Maple Seed", 5, "Pine Cone", 5, "Mahogany Seed", 5),
                "Foraging Level 4"),

        GRASS_STARTER(new CraftingItem("Grass", 0, new Texture(Gdx.files.internal("crafting/Grass_Starter.png"))),
                Map.of("Wood", 1, "Fiber", 1), "Pierre's General Store");

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

        public static CraftingRecipe getRecipeByName(String name) {
                for (CraftingRecipe recipe : values()) {
                        if (recipe.getName().equalsIgnoreCase(name)) return recipe;
                }
                return null;
        }
        public static CraftingItem getItemByName(String name) {
                for (CraftingRecipe recipe : values()) {
                        if (recipe.getName().equalsIgnoreCase(name)) return recipe.data;
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
