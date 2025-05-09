package models;

import models.NPC.NPC;
import models.NPC.Quest;
import models.artisan.ArtisanMachineRecipe;
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

    private double energy = 2;
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

    public Player() {}


    public boolean isConscious() {
        return energy > 0;
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

    public boolean isInPlayerFarm(Location location) {
        return location.x() >= startOfFarm.x() && location.x() <= endOfFarm.x()
                && location.y() >= startOfFarm.y() && location.y() <= endOfFarm.y();
    }

    // energy
    public double getEnergy() {
        return Math.floor(energy*100)/100;
    }

    public String getColoredEnergy() {
        StringBuilder sb = new StringBuilder();
        if (energy > 150) sb.append(AnsiColors.ANSI_GREEN_BOLD);
        else if (energy > 100) sb.append(AnsiColors.ANSI_LIGHT_YELLOW_BOLD);
        else if (energy > 50) sb.append(AnsiColors.ANSI_LIGHT_ORANGE_BOLD);
        else sb.append(AnsiColors.ANSI_RED_BOLD);

        return sb.toString() + getEnergy() + AnsiColors.ANSI_RESET;
    }

    public void changeEnergy(double amount) {
        if (energy == Constants.INFINITY) return;
        energy += amount;
        if (energy < 0) energy = 0;
    }
}