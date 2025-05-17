package models.cooking;

import models.crafting.CraftingItem;
import models.crafting.CraftingRecipe;

import java.util.Map;

public enum FoodRecipe {

    FRIED_EGG(new Food("Fried Egg", 50, 35, null), Map.of("Egg", 1)),

    BAKED_FISH(new Food("Baked Fish", 75, 100, null), Map.of("Sardine", 1, "Salmon", 1, "Wheat", 1)),

    SALAD(new Food("Salad", 113, 110, null), Map.of("Leek", 1, "Dandelion", 1)),

    OLMELET(new Food("Olmelet", 100, 125, null), Map.of("Egg", 1, "Milk", 1)),

    PUMPKIN_PIE(new Food("Pumpkin Pie", 225, 385, null), Map.of("Pumpkin", 1, "Wheat Flour", 1, "Milk", 1, "Sugar", 1)),

    SPAGHETTI(new Food("Spaghetti", 75, 120, null), Map.of("Wheat Flour", 1, "Tomato", 1)),

    PIZZA(new Food("Pizza", 150, 300, null), Map.of("Wheat Flour", 1, "Tomato", 1, "Cheese", 1)),

    TORTILLA(new Food("Tortilla", 50, 50, null), Map.of("Corn", 1)),

    MAKI_ROLL(new Food("Maki Roll", 100, 220, null), Map.of("Any Fish", 1, "Rice", 1, "Fiber", 1)),

    TRIPLE_SHOT_ESPRESSO(new Food("Triple Shot Espresso", 200, 450, new FoodBuff("MaxEnergy", 100, 5)), Map.of("Coffee", 3)),

    COOKIE(new Food("Cookie", 90, 140, null), Map.of("Wheat Flour", 1, "Sugar", 1, "Egg", 1)),

    HASH_BROWNS(new Food("Hash Browns", 90, 120, new FoodBuff("Farming", 0, 5)), Map.of("Potato", 1, "Oil", 1)),

    PANCAKES(new Food("Pancakes", 90, 80, new FoodBuff("Foraging", 0, 11)), Map.of("Wheat Flour", 1, "Egg", 1)),

    FRUIT_SALAD(new Food("Fruit Salad", 263, 450, null), Map.of("Blueberry", 1, "Melon", 1, "Apricot", 1)),

    RED_PLATE(new Food("Red Plate", 240, 400, new FoodBuff("MaxEnergy", 50, 3)), Map.of("Red Cabbage", 1, "Radish", 1)),

    BREAD(new Food("Bread", 50, 60, null), Map.of("Wheat Flour", 1)),

    SALMON_DINNER(new Food("Salmon Dinner", 125, 300, null), Map.of("Salmon", 1, "Amaranth", 1, "Kale", 1)),

    VEGETABLE_MEDLEY(new Food("Vegetable Medley", 165, 120, null), Map.of("Tomato", 1, "Beet", 1)),

    FARMERS_LUNCH(new Food("Farmer's Lunch", 200, 150, new FoodBuff("Farming", 0, 5)), Map.of("Omelet", 1, "Parsnip", 1)),

    SURVIVAL_BURGER(new Food("Survival Burger", 125, 180, new FoodBuff("Foraging", 0, 5)), Map.of("Bread", 1, "Carrot", 1, "Eggplant", 1)),

    DISH_O_THE_SEA(new Food("Dish O' the Sea", 150, 220, new FoodBuff("Fishing", 0, 5)), Map.of("Sardine", 2, "Hash Browns", 1)),

    SEAFOAM_PUDDING(new Food("Seafoam Pudding", 175, 300, new FoodBuff("Fishing", 0, 10)), Map.of("Flounder", 1, "Midnight Carp", 1)),

    MINERS_TREAT(new Food("Miner's Treat", 125, 200, new FoodBuff("Mining", 0, 5)), Map.of("Carrot", 2, "Sugar", 1, "Milk", 1));

    public final Food data;
    public final Map<String, Integer> ingredients;

    FoodRecipe(Food data, Map<String, Integer> ingredients) {
        this.data = data;
        this.ingredients = ingredients;
    }

    public static FoodRecipe getRecipeByName(String name) {
        for (FoodRecipe recipe : values()) {
            if (recipe.name().equalsIgnoreCase(name)) return recipe;
        }
        return null;
    }
    public static Food getItemByName(String name) {
        for (FoodRecipe recipe : values()) {
            if (recipe.name().equalsIgnoreCase(name)) return recipe.data;
        }
        return null;
    }
}