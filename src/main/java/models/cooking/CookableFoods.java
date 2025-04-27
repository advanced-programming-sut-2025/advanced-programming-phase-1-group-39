package models.cooking;

import java.util.Map;

public enum CookableFoods {
    FRIED_EGG(new Food(
            "Fried Egg", Map.of("Egg", 1), 50, null,  35)),

    BAKED_FISH(new Food(
            "Baked Fish", Map.of("Sardine", 1, "Salmon", 1, "Wheat", 1), 75, null, 100)),

    SALAD(new Food(
            "Salad", Map.of("Leek", 1, "Dandelion", 1), 113, null, 110)),

    OLMELET(new Food(
            "Olmelet", Map.of("Egg", 1, "Milk", 1), 100, null, 125)),

    PUMPKIN_PIE(new Food(
            "Pumpkin Pie", Map.of("Pumpkin", 1, "Wheat Flour", 1, "Milk", 1, "Sugar", 1), 225, null, 385)),

    SPAGHETTI(new Food(
            "Spaghetti", Map.of("Wheat Flour", 1, "Tomato", 1), 75, null, 120)),

    PIZZA(new Food(
            "Pizza", Map.of("Wheat Flour", 1, "Tomato", 1, "Cheese", 1), 150, null, 300)),

    TORTILLA(new Food(
            "Tortilla", Map.of("Corn", 1), 50, null, 50)),

    MAKI_ROLL(new Food(
            "Maki Roll", Map.of("Any Fish", 1, "Rice", 1, "Fiber", 1), 100, null, 220)),

    TRIPLE_SHOT_ESPRESSO(new Food(
            "Triple Shot Espresso", Map.of("Coffee", 3), 200, new FoodBuff("MaxEnergy", 100, 5), 450)),

    COOKIE(new Food(
            "Cookie", Map.of("Wheat Flour", 1, "Sugar", 1, "Egg", 1), 90, null,  140)),

    HASH_BROWNS(new Food(
            "Hash Browns", Map.of("Potato", 1, "Oil", 1), 90, new FoodBuff("Farming", 0, 5), 120)),

    PANCAKES(new Food(
            "Pancakes", Map.of("Wheat Flour", 1, "Egg", 1), 90, new FoodBuff("Foraging", 0, 11), 80)),

    FRUIT_SALAD(new Food(
            "Fruit Salad", Map.of("Blueberry", 1, "Melon", 1, "Apricot", 1), 263, null, 450)),

    RED_PLATE(new Food(
            "Red Plate", Map.of("Red Cabbage", 1, "Radish", 1), 240, new FoodBuff("MaxEnergy", 50, 3), 400)),

    BREAD(new Food(
            "Bread", Map.of("Wheat Flour", 1), 50, null, 60)),

    SALMON_DINNER(new Food(
            "Salmon Dinner", Map.of("Salmon", 1, "Amaranth", 1, "Kale", 1), 125, null, 300)),

    VEGETABLE_MEDLEY(new Food(
            "Vegetable Medley", Map.of("Tomato", 1, "Beet", 1), 165, null, 120)),

    FARMERS_LUNCH(new Food(
            "Farmer's Lunch", Map.of("Omelet", 1, "Parsnip", 1), 200, new FoodBuff("Farming", 0, 5) , 150)),

    SURVIVAL_BURGER(new Food(
            "Survival Burger", Map.of("Bread", 1, "Carrot", 1, "Eggplant", 1), 125, new FoodBuff("Foraging", 0, 5), 180)),

    DISH_O_THE_SEA(new Food(
            "Dish O' the Sea", Map.of("Sardine", 2, "Hash Browns", 1), 150, new FoodBuff("Fishing", 0, 5), 220)),

    SEAFOAM_PUDDING(new Food(
            "Seafoam Pudding", Map.of("Flounder", 1, "Midnight Carp", 1), 175, new FoodBuff("Fishing", 0, 10), 300)),

    MINERS_TREAT(new Food(
            "Miner's Treat", Map.of("Carrot", 2, "Sugar", 1, "Milk", 1), 125, new FoodBuff("Mining", 0, 5), 200));

    public final Food data;

    CookableFoods(Food data) {
        this.data = data;
    }
}
