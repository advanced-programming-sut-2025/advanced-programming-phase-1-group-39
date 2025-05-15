package models;

import models.Enums.Season;
import models.NPC.NPC;
import models.NPC.PlayerNPCInteraction;
import models.NPC.Quest;
import models.animals.Animal;
import models.animals.AnimalProductQuality;
import models.animals.Fish;
import models.animals.FishType;
import models.artisan.ArtisanMachineRecipe;
import models.buildings.Building;
import models.buildings.Cabin;
import models.buildings.GreenHouse;
import models.buildings.ShippingBin;
import models.cooking.FoodBuff;
import models.cooking.FoodRecipe;
import models.crafting.CraftingRecipe;
import models.inventory.Inventory;
import models.map.AnsiColors;
import models.tools.*;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private int gameId;

    private Location location = new Location(0,0);
    private Location startOfFarm;
    private Location endOfFarm;

    private String username;

    FoodBuff buff = null;

    private double energy = Constants.MAX_ENERGY;
    private double turnEnergy = Constants.MAX_ENERGY_PER_TURN;
    private boolean energyCheated = false;
    private boolean energyUnlimited = false;

    private int numOfBadDays = 0;

    private Skill skills = new Skill();
    private Inventory inventory;


    private ArrayList<CraftingRecipe> craftingRecipes;
    private ArrayList<ArtisanMachineRecipe> artisanMachineRecipes;
    private ArrayList<FoodRecipe> foodRecipes;

    private HashMap<Player, Integer> playersFriendship;
    private HashMap<NPC, Integer> NPCsFriendship;

    private ArrayList<Quest> activeQuests;

    // animals
    private HashMap<String, Animal> animals = new HashMap<>();

    private int money = 0;
    private int nightRevenue = 0;

    private ArrayList<Building> playerFarmBuildings = new ArrayList<>();

    // NPC
    private ArrayList<PlayerNPCInteraction> friendships = initialPlayersFriendship();

    public Player(String username, int gameId) {
        ItemStack hoe = new ItemStack(new Hoe(), 1);
        ItemStack pickaxe = new ItemStack(new Pickaxe(), 1);
        ItemStack axe = new ItemStack(new Axe(), 1);
        ItemStack scythe = new ItemStack(new Scythe(), 1);
        ItemStack wateringCan = new ItemStack(new WateringCan(), 1);
        ItemStack trainingRod = new ItemStack(new FishingPole("Training Rod", FishingPoleType.TRAINING_ROD), 1);

        this.inventory = new Inventory(
                List.of(hoe, pickaxe, axe, scythe, wateringCan, trainingRod)
        );

        this.username = username;

        this.gameId = gameId;
    }

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

    public void startTrade(Player player, TradeItem item) {}

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
    public double getTurnEnergy() {
        return turnEnergy;
    }

    public double getEnergy() {
        return Math.floor(energy*100)/100;
    }

    public String getColoredEnergy() {
        if (energyUnlimited) return (AnsiColors.ANSI_LIGHT_GREEN_BOLD + "UNLIMITED" + AnsiColors.ANSI_RESET);
        StringBuilder sb = new StringBuilder();
        if (energy > 150) sb.append(AnsiColors.ANSI_LIGHT_GREEN_BOLD);
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

    // ID
    public int getGameId() {
        return gameId;
    }

    // home location
    public Location getHomeLocation() {
        return new Location(startOfFarm.x() + 74, startOfFarm.y() + 8);
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

        if (skill == Constants.MAX_SKILL_LEVEL) {
            seasonal.addAll(Arrays.stream(FishType.values())
                    .filter(f -> f.name().equals(f.name().toUpperCase()))
                    .filter(f -> f.season == season)
                    .toList());
        }

        List<Fish> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            FishType randomType = seasonal.get((int) (Math.random() * seasonal.size()));

            double qualityScore = Math.random() * (skill * 2) + pole.getPoleType().getMultiplier();
            double normalized = qualityScore / 7.0;
            AnimalProductQuality quality = AnimalProductQuality.fromScore(normalized);

            Fish fish = randomType.create();
            fish.setQuality(quality);
            result.add(fish);
        }

        return new ArrayList<>(result);
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
    public boolean hasEnoughMoney(int amount) {
        return money >= amount;
    }

    public boolean canBuildGreenHouse() {
        return inventory.hasEnoughStack("Wood", 500) && money >= 1000;
    }

    public void buildGreenHouse() {
        inventory.pickItem("Wood", 500);
        changeMoney(1000);
        for (Building building : playerFarmBuildings) {
            if (building.getName().equals("greenhouse")) ((GreenHouse)building).build();
        }
    }

    public void addFirstBuildingObjects(Game game) {
        Building cabin = new Cabin(new Location(startOfFarm.x() + 70, startOfFarm.y() + 5));
        Building greenhouse = new GreenHouse(new Location(startOfFarm.x() + 25, startOfFarm.y() + 0));
        Building shippingBin = new ShippingBin("Shipping Bin", new Location(startOfFarm.x() + 77, startOfFarm.y() + 10), 1, 1);
        addToBuildings(cabin);
        addToBuildings(greenhouse);
        addToBuildings(shippingBin);
    }

    public void addToBuildings(Building building) {
        playerFarmBuildings.add(building);
    }
    public Building getBuildingByName(String name) {
        for (Building building : playerFarmBuildings) {
            if (building.getName().equalsIgnoreCase(name)) return building;
        }
        return null;
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


    public void addAnimal(Animal animal) {
        animals.put(animal.getName(), animal);
    }
    public ArrayList<Animal> getAnimals() {
        return new ArrayList<>(animals.values());
    }
    public Animal getAnimal(String name) {
        return animals.get(name);
    }
    public Animal getAnimalByLocation(Location location) {
        for (Animal animal : animals.values()) {
            if (animal.getLocation().equals(location)) return animal;
        }
        return null;
    }

    public boolean isNearLocation(Location location) {
        int dx = this.location.x() - location.x();
        int dy = this.location.y() - location.y();

        return dx <= 1 && dy <= 1 && dx >= -1 && dy >= -1;
    }

    public Skill getSkills() {
        return skills;
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

    // Foods
    public void applyBuff(FoodBuff buff) {
        this.buff = buff;
    }

    public String getUsername() {
        return username;
    }
}