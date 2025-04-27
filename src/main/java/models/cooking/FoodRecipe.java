package models.cooking;

import models.Item;

import java.util.HashMap;

public class FoodRecipe {
    int energyCost = 3;
    HashMap<Item, Integer> ingredients;

    CookableFoods foodType;
}
