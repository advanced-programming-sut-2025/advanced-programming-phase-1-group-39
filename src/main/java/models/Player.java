package models;

import models.Enums.Season;
import models.NPC.NPC;
import models.NPC.Quest;
import models.animals.AnimalProductQuality;
import models.animals.Fish;
import models.animals.FishType;
import models.artisan.ArtisanMachineRecipe;
import models.cooking.FoodBuff;
import models.cooking.FoodRecipe;
import models.crafting.CraftingRecipe;
import models.inventory.Inventory;
import models.tools.FishingPole;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private Location location;
    private double energy;
    private Skill skills;
    private Inventory inventory;

    private FoodBuff buff = null;

    private ArrayList<CraftingRecipe> craftingRecipes;
    private ArrayList<ArtisanMachineRecipe> artisanMachineRecipes;
    private ArrayList<FoodRecipe> foodRecipes;

    private HashMap<Player, Integer> playersFriendship;
    private HashMap<NPC, Integer> NPCsFriendship;

    private ArrayList<Quest> activeQuests;

    public boolean isConscious() {
        return energy > 0;
    }

    private int money = 0;
    private int nightRevenue = 0;

    public void setBuff(FoodBuff buff) {
        this.buff = buff;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getMoney() {
        return money;
    }
    public void changeMoney(int amount) {
        this.money += amount;
        if (money < 0) {
            this.money = 0;
        }
    }
    public void addToRevenue(int amount) {
        this.nightRevenue += amount;
    }
    public boolean hasEnoughMoney(int amount) {
        return money >= amount;
    }

    public double getEnergy() {
        return energy;
    }
    public void changeEnergy(double amount) {
        energy += amount;
    }
    public void getFish() {}

    public int getLevelOfFriendship(NPC npc) {return 0;}
    public void startTrade(Player player, TradeItem item) {}

    public void learnCraftingRecipe(CraftingRecipe recipe) {
        craftingRecipes.add(recipe);
    }
    public boolean hasLearnedCraftingRecipe(CraftingRecipe recipe) {
        return craftingRecipes.contains(recipe);
    }
    public String showCraftingRecipes() {
        StringBuilder sb = new StringBuilder();
        for (CraftingRecipe recipe : craftingRecipes) {
            sb.append(recipe.toString());
        }
        return sb.toString();
    }

    public void learnFoodRecipe(FoodRecipe recipe) {
        foodRecipes.add(recipe);
    }
    public boolean hasLearnedFoodRecipe(FoodRecipe recipe) {
        return foodRecipes.contains(recipe);
    }
    public String showFoodRecipes() {
        StringBuilder sb = new StringBuilder();
        for (FoodRecipe recipe : foodRecipes) {
            sb.append(recipe.toString());
        }
        return sb.toString();
    }

    public ArrayList<Fish> goFishing(FishingPole pole, Weather weather, Season season) { // Todo: not complete (legendary + type fishing pole)
        double M = weather.getFishingFactor();
        double R = Math.random();
        int skill = skills.getFishingLevel();

        int count = (int) Math.ceil((2 + skill) * M * R);
        count = Math.min(count, 6);

        List<FishType> seasonal = Arrays.stream(FishType.values())
                .filter(f -> f.season == season)
                .collect(Collectors.toList());

        if (skill == skills.getMaxFishingLevel()) {
            seasonal.addAll(Arrays.stream(FishType.values())
                    .filter(f -> f.name().equals(f.name().toUpperCase()))
                    .filter(f -> f.season == season)
                    .toList());
        }

        List<Fish> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            FishType randomType = seasonal.get((int) (Math.random() * seasonal.size()));

//            double qualityScore = Math.random() * (skill * 2) + pole.getType().;
//            double normalized = qualityScore / 7.0;
//            AnimalProductQuality quality = AnimalProductQuality.fromScore(normalized);
//
//            result.add(new Fish(randomType, quality));
        }

        return new ArrayList<>(result);
    }

}
