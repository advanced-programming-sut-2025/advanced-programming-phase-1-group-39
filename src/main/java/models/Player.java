package models;

import models.Enums.Season;
import models.NPC.NPC;
import models.NPC.Quest;
import models.animals.Animal;
import models.animals.Fish;
import models.animals.FishType;
import models.artisan.ArtisanMachineRecipe;
import models.buildings.Building;
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
    private String username;

    private Location location;
    private double energy;
    private Skill skills;
    private Inventory inventory;

    private FoodBuff buff = null;

    private HashMap<String, Animal> animals = new HashMap<>();

    private ArrayList<CraftingRecipe> craftingRecipes;
    private ArrayList<ArtisanMachineRecipe> artisanMachineRecipes;
    private ArrayList<FoodRecipe> foodRecipes;
    private ArrayList<Building> buildings = new ArrayList<>();

    private HashMap<Player, Integer> playersFriendship;
    private HashMap<NPC, Integer> NPCsFriendship;

    private ArrayList<Quest> activeQuests;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConscious() {
        return energy > 0;
    }

    private int money = 0;
    private int nightRevenue = 0;

    public void applyBuff(FoodBuff buff) {
        this.buff = buff;
    }

    public Location getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Skill getSkills() {
        return skills;
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

    public void addAnimal(Animal animal) {
        animals.put(animal.getName(), animal);
    }
    public int sellAnimal(Animal animal) {
        int money = (int) (animal.getPrice() * (((double) animal.getFriendship() /1000) + 0.3));
        changeMoney(money);
        animals.remove(animal.getName());
        return money;
    }
    public ArrayList<Animal> getAnimals() {
        return new ArrayList<>(animals.values());
    }
    public Animal getAnimal(String name) {
        return animals.get(name);
    }

    public void addToBuildings(Building building) {
        buildings.add(building);
    }
}
