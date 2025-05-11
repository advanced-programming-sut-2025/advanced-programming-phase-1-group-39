package models;

import models.NPC.NPC;
import models.NPC.Quest;
import models.artisan.ArtisanMachineRecipe;
import models.buildings.Building;
import models.buildings.Cabin;
import models.buildings.GreenHouse;
import models.cooking.FoodBuff;
import models.cooking.FoodRecipe;
import models.crafting.CraftingRecipe;
import models.inventory.Inventory;
import models.map.AnsiColors;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private Location location = new Location(0,0);
    private Location startOfFarm;
    private Location endOfFarm;

    FoodBuff buff = null;

    private double energy = Constants.MAX_ENERGY;
    private double turnEnergy = Constants.MAX_ENERGY_PER_TURN;
    private boolean energyCheated = false;
    private boolean energyUnlimited = false;

    private int numOfBadDays = 0;

    private Skill skills = new Skill();
    private Inventory inventory = new Inventory();


    private ArrayList<CraftingRecipe> craftingRecipes;
    private ArrayList<ArtisanMachineRecipe> artisanMachineRecipes;
    private ArrayList<FoodRecipe> foodRecipes;

    private HashMap<Player, Integer> playersFriendship;
    private HashMap<NPC, Integer> NPCsFriendship;

    private ArrayList<Quest> activeQuests;

    private int money = 0;
    private int nightRevenue = 0;

    private ArrayList<Building> playerFarmBuildings = new ArrayList<>();

    public Player() {}

    public boolean isConscious() {
        return energy > 0;
    }

    public boolean haveBadDay() {
        return numOfBadDays > 0;
    }
    public void changeBadDays(int amount) {
        numOfBadDays += amount;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void goFishing() {}

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

    public Location getLocation() {
        return location;
    }

    public void setLocationAbsolut(int x, int y) {
        this.location = new Location(x, y);
    }

    public void setLocationRelative(int x, int y) {
        this.location = new Location(x + startOfFarm.x(), y + startOfFarm.y());
    }

    public void setFarmBound(Location startOfFarm) {
        this.startOfFarm = startOfFarm;
        this.endOfFarm = new Location(startOfFarm.x() + Constants.FARM_WIDTH,
                startOfFarm.y() + Constants.FARM_HEIGHT);
    }

    public Location getStartOfFarm() {
        return startOfFarm;
    }

    public Location getEndOfFarm() {
        return endOfFarm;
    }

    public boolean isInPlayerFarm(Location location) {
        return location.x() >= startOfFarm.x() && location.x() <= endOfFarm.x()
                && location.y() >= startOfFarm.y() && location.y() <= endOfFarm.y();
    }

    // energy
    public double getEnergy() {
        return Math.floor(energy*100)/100;
    }

    public String getColoredEnergy() {
        if (energyUnlimited) return (AnsiColors.ANSI_GREEN_BOLD + "UNLIMITED" + AnsiColors.ANSI_RESET);
        StringBuilder sb = new StringBuilder();
        if (energy > 150) sb.append(AnsiColors.ANSI_GREEN_BOLD);
        else if (energy > 100) sb.append(AnsiColors.ANSI_LIGHT_YELLOW_BOLD);
        else if (energy > 50) sb.append(AnsiColors.ANSI_LIGHT_ORANGE_BOLD);
        else sb.append(AnsiColors.ANSI_RED_BOLD);

        return sb.toString() + getEnergy() + AnsiColors.ANSI_RESET;
    }

    public void changeEnergy(double amount) {
        if (energyUnlimited) return;

        else if (energyCheated) {
            energy += amount;
            if (energy < Constants.MAX_ENERGY) resetCheatedEnergy();
            energy = Math.max(0, energy);
            return;
        }
        energy += amount;
        energy = energy > Constants.MAX_ENERGY ? Constants.MAX_ENERGY : (energy < 0 ? 0 : energy);

        if (amount < 0) {
            turnEnergy += amount;
            if (turnEnergy < 0) {
                turnEnergy = 0;
                energy = 0;
            }
        }
    }

    public void setEnergy(double energy) {
        this.energy = energy;
        if (energyCheated) return;
        this.energy = Math.min(energy, Constants.MAX_ENERGY);
        this.energy = Math.max(0, this.energy);
    }

    public void setCheatedEnergy() {
        energyCheated = true;
    }
    public void resetCheatedEnergy() {
        energyCheated = false;
    }

    public void setEnergyUnlimited() {
        energyUnlimited = true;
    }
    public void resetEnergyUnlimited() {
        energyUnlimited = false;
    }

    public boolean hasEnoughEnergy(double amount) {
        if (energyUnlimited) return true;
        return energy >= amount;
    }

    public void resetHourlyEnergyLimit() {
        turnEnergy = Math.min(Constants.MAX_ENERGY_PER_TURN, energy);
    }
    // home location
    public Location getHomeLocation() {
        return new Location(startOfFarm.x() + 74, startOfFarm.y() + 8);
    }

    // Money handling
    public void addNightRevenueToMoney() {
        money += nightRevenue;
        nightRevenue = 0;
    }
    public void addToRevenue(int amount) {
        nightRevenue += amount;
    }

    public void changeMoney(int amount) {
        money += amount;
        if (money < 0) money = 0;
    }

    public boolean canBuildGreenHouse() {
        return inventory.hasEnoughStack("wood", 500) && money >= 1000;
    }

    public void buildGreenHouse() {
        inventory.pickItem("wood", 500);
        changeMoney(1000);
        for (Building building : playerFarmBuildings) {
            if (building.getName().equals("greenhouse")) ((GreenHouse)building).build();
        }
    }

    public void addFirstBuildingObjects() {
        Cabin cabin = new Cabin(new Location(startOfFarm.x() + 70, startOfFarm.y() + 5));
        GreenHouse greenHouse = new GreenHouse(new Location(startOfFarm.x() + 25, startOfFarm.y() + 0));
    }
}