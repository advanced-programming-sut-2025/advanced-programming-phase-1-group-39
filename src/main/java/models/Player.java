package models;

import models.NPC.NPC;
import models.NPC.Quest;
import models.artisan.ArtisanMachineRecipe;
import models.cooking.FoodBuff;
import models.cooking.FoodRecipe;
import models.crafting.CraftingRecipe;
import models.inventory.Inventory;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private int id;
    private int gameId;

    private Location location = new Location(0, 0);
    private Location startOfFarm;
    private Location endOfFarm;
    private String username;

    FoodBuff buff = null;

    private int energy = 200;
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

    private ArrayList<PlayerNPCInteraction> friendships = initialPlayersFriendship();

    public Player(String username, int id, int gameId) {
        this.username = username;
        this.id = id;
        this.gameId = gameId;
    }


    public boolean isConscious() {
        return energy > 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getEnergy() {
        return energy;
    }

    public void changeEnergy(int amount) {
        energy += amount;
    }

    public void goFishing() {
    }

    public int getLevelOfFriendship(NPC npc) {
        return 0;
    }

    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameId;
    }

    public void startTrade(Player player, TradeItem item) {
    }

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

    public String getUsername() {
        return username;
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

    // NPC

    public ArrayList<PlayerNPCInteraction> initialPlayersFriendship() {
        ArrayList<PlayerNPCInteraction> friendship = new ArrayList<>();
        friendship.add(new PlayerNPCInteraction("sebastian", 20));
        friendship.add(new PlayerNPCInteraction("abigail", 40));
        friendship.add(new PlayerNPCInteraction("harvey", 60));
        friendship.add(new PlayerNPCInteraction("leah", 80));
        friendship.add(new PlayerNPCInteraction("robin", 100));
        return friendship;
    }

    public PlayerNPCInteraction getFriendship(String NPCName) {
        for (PlayerNPCInteraction friendshipInteraction : friendships) {
            if (friendshipInteraction.getNPCName().equals(NPCName)) {
                return friendshipInteraction;
            }
        }
        return null;
    }

    public ArrayList<PlayerNPCInteraction> getAllFriendships() {
        return friendships;
    }
}