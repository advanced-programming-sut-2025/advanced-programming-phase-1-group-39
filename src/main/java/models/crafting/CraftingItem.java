package models.crafting;

import java.util.Map;

public class CraftingItem {
    String name;
    Map<String, Integer> ingredients;
    String source;
    int sellPrice;

    public CraftingItem(String name, Map<String, Integer> ingredients, String source, int sellPrice) {
        this.name = name;
        this.ingredients = ingredients;
        this.source = source;
        this.sellPrice = sellPrice;
    }
}
