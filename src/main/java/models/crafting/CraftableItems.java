package models.crafting;

import java.util.Map;

public enum CraftableItems {
        CHERRY_BOMB(new CraftingItem(
                "Cherry Bomb",
                Map.of("Copper Ore", 4, "Coal", 1),
                "Mining Level 1", 50)),

        BOMB(new CraftingItem(
                "Bomb",
                Map.of("Iron Ore", 4, "Coal", 1),
                "Mining Level 2", 50)),

        MEGA_BOMB(new CraftingItem(
                "Mega Bomb",
                Map.of("Gold Ore", 4, "Coal", 1),
                "Mining Level 3", 50)),

        SPRINKLER(new CraftingItem(
                "Sprinkler",
                Map.of("Copper Bar", 1, "Iron Bar", 1),
                "Farming Level 1", 0)),

        QUALITY_SPRINKLER(new CraftingItem(
                "Quality Sprinkler",
                Map.of("Iron Bar", 1, "Gold Bar", 1),
                "Farming Level 2", 0)),

        IRIDIUM_SPRINKLER(new CraftingItem(
                "Iridium Sprinkler",
                Map.of("Gold Bar", 1, "Iridium Bar", 1),
                "Farming Level 3", 0)),

        CHARCOAL_KILN(new CraftingItem(
                "Charcoal Kiln",
                Map.of("Wood", 20, "Copper Bar", 2),
                "Foraging Level 1", 0)),

        FURNACE(new CraftingItem(
                "Furnace",
                Map.of("Copper Ore", 20, "Stone", 25),
                "-", 0)),

        SCARECROW(new CraftingItem(
                "Scarecrow",
                Map.of("Wood", 50, "Coal", 1, "Fiber", 20),
                "-", 0)),

        DELUXE_SCARECROW(new CraftingItem(
                "Deluxe Scarecrow",
                Map.of("Wood", 50, "Coal", 1, "Fiber", 20, "Iridium Ore", 1),
                "Farming Level 2", 0)),

        BEE_HOUSE(new CraftingItem(
                "Bee House",
                Map.of("Wood", 40, "Coal", 8, "Iron Bar", 1),
                "Farming Level 1", 0)),

        CHEESE_PRESS(new CraftingItem(
                "Cheese Press",
                Map.of("Wood", 45, "Stone", 45, "Copper Bar", 1),
                "Farming Level 2", 0)),

        KEG(new CraftingItem(
                "Keg",
                Map.of("Wood", 30, "Copper Bar", 1, "Iron Bar", 1),
                "Farming Level 3", 0)),

        LOOM(new CraftingItem(
                "Loom",
                Map.of("Wood", 60, "Fiber", 30),
                "Farming Level 3", 0)),

        MAYONNAISE_MACHINE(new CraftingItem(
                "Mayonnaise Machine",
                Map.of("Wood", 15, "Stone", 15, "Copper Bar", 1),
                "-", 0)),

        OIL_MAKER(new CraftingItem(
                "Oil Maker",
                Map.of("Wood", 100, "Gold Bar", 1, "Iron Bar", 1),
                "Farming Level 3", 0)),

        PRESERVES_JAR(new CraftingItem(
                "Preserves Jar",
                Map.of("Wood", 50, "Stone", 40, "Coal", 8),
                "Farming Level 2", 0)),

        DEHYDRATOR(new CraftingItem(
                "Dehydrator",
                Map.of("Wood", 30, "Stone", 20, "Fiber", 30),
                "Pierre's General Store", 0)),

        FISH_SMOKER(new CraftingItem(
                "Fish Smoker",
                Map.of("Wood", 50, "Iron Bar", 3, "Coal", 10),
                "Fish Shop", 0)),

        MYSTIC_TREE_SEED(new CraftingItem(
                "Mystic Tree Seed",
                Map.of("Acorn", 5, "Maple Seed", 5, "Pine Cone", 5, "Mahogany Seed", 5),
                "Foraging Level 4", 100));

        public final CraftingItem data;

        CraftableItems(CraftingItem data) {
            this.data = data;
        }
}
