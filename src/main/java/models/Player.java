package models;

import models.Enums.Season;
import models.NPC.NPC;
import models.NPC.PlayerNPCInteraction;
import models.NPC.Quest;
import models.animals.*;
import models.artisan.ArtisanMachineRecipe;
import models.buildings.*;
import models.cooking.FoodBuff;
import models.cooking.FoodRecipe;
import models.crafting.CraftingItem;
import models.crafting.CraftingRecipe;
import models.inventory.Inventory;
import models.map.AnsiColors;
import models.tools.*;
import models.tools.FishingPole;
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


    private ArrayList<CraftingRecipe> craftingRecipes = new ArrayList<>();
    private ArrayList<ArtisanMachineRecipe> artisanMachineRecipes = new ArrayList<>();
    private ArrayList<FoodRecipe> foodRecipes = new ArrayList<>();

    private HashMap<NPC, Integer> NPCsFriendship = new HashMap<>();

    private ArrayList<Quest> activeQuests = new ArrayList<>();

    // animals
    private HashMap<String, Animal> animals = new HashMap<>();

    private int money = 0;
    private int nightRevenue = 0;

    private ArrayList<Building> playerFarmBuildings = new ArrayList<>();

    // NPC
    private ArrayList<PlayerNPCInteraction> friendships = initialPlayersFriendship();

    private String spouseName;

    public Player(String username, int gameId) {
        ItemStack hoe = new ItemStack(new Hoe(), 1);
        ItemStack pickaxe = new ItemStack(new Pickaxe(), 1);
        ItemStack axe = new ItemStack(new Axe(), 1);
        ItemStack scythe = new ItemStack(new Scythe(), 1);
        ItemStack wateringCan = new ItemStack(new WateringCan(), 1);
//        ItemStack trainingRod = new ItemStack(new FishingPole("Training Rod", FishingPoleType.TRAINING_ROD), 1);

        this.inventory = new Inventory(
                List.of(hoe, pickaxe, axe, scythe, wateringCan)
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

    public void setTurnEnergy(double turnEnergy) {
        this.turnEnergy = turnEnergy;
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

    public ArrayList<Fish> goFishing(FishingPole pole, Weather weather, Season season) {
        double M = weather.getFishingFactor();
        double R = Math.random();
        int skill = skills.getFishingLevel();

        int count = (int) Math.ceil((2 + skill) * M * R);
        count = Math.min(count, 6);

        List<FishType> seasonal = Arrays.stream(FishType.values())
                .filter(f -> f.season == season)
                .filter(f -> !f.isLegendary())
                .collect(Collectors.toList());

        if (skill == Constants.MAX_SKILL_LEVEL) {
            seasonal.addAll(Arrays.stream(FishType.values())
                    .filter(FishType::isLegendary)
                    .filter(f -> f.season == season)
                    .collect(Collectors.toList()));
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
        if (spouseName != null) {
            return App.getApp().getCurrentGame().hasEnoughMoney(this, amount);
        }
        return money >= amount;
    }

    public int getMoney() {
        return money;
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
        if (hasLearnedFoodRecipe(recipe)) return;
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

    public void learnRecipes(List<FoodRecipe> foodRecipes, List<CraftingRecipe> craftingRecipes) {
        if (foodRecipes != null) {
            for (FoodRecipe recipe : foodRecipes) {
                learnFoodRecipe(recipe);
            }
        }
        if (craftingRecipes != null) {
            for (CraftingRecipe craftingRecipe : craftingRecipes) {
                learnCraftingRecipe(craftingRecipe);
            }
        }
    }



    public void addAnimal(Animal animal) {
        animals.put(animal.getName(), animal);
    }

    public int sellAnimal(Animal animal) {
        int money = (int) (animal.getPrice() * (((double) animal.getFriendship() /1000) + 0.3));
        changeMoney(money);
        animals.remove(animal.getName());

        AnimalBuilding animalBuilding;
        for (Building building : this.playerFarmBuildings) {
            if (building instanceof AnimalBuilding) {
                if (((AnimalBuilding) building).hasAnimal(animal)) {
                    animalBuilding = (AnimalBuilding) building;
                    animalBuilding.removeAnimal(animal);
                }
            }
        }
        return money;
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

    // marriage

    public String getSpouseName() {
        return spouseName;
    }

    public AnimalBuilding getAnimalBuilding(LivingPlace type) {
        for (Building building : playerFarmBuildings) {
            if (building instanceof AnimalBuilding) {
                AnimalBuilding animalBuilding = (AnimalBuilding) building;
                if (animalBuilding.getType() == type && animalBuilding.hasCapacity(1)) {
                    return animalBuilding;
                }
            }
        }
        return null;
    }

    // skill
    public void learnNewRecipes() {
        // Foraging level
        switch (skills.getForagingLevel()) {
            case 0 :
                learnRecipes(
                        List.of(FoodRecipe.FRIED_EGG, FoodRecipe.BAKED_FISH, FoodRecipe.SALAD),
                        null
                );
                break;
            case 1 :
                learnCraftingRecipe(CraftingRecipe.CHARCOAL_KILN);
                break;
            case 2:
                learnFoodRecipe(FoodRecipe.VEGETABLE_MEDLEY);
                break;
            case 3:
                learnFoodRecipe(FoodRecipe.SURVIVAL_BURGER);
                break;
            case 4:
                learnCraftingRecipe(CraftingRecipe.MYSTIC_TREE_SEED);
        }
        // Mining level
        switch (skills.getMiningLevel()) {
            case 1 :
                learnFoodRecipe(FoodRecipe.MINERS_TREAT);
                learnCraftingRecipe(CraftingRecipe.CHERRY_BOMB);
                break;
            case 2 :
                learnCraftingRecipe(CraftingRecipe.BOMB);
                break;
            case 3 :
                learnCraftingRecipe(CraftingRecipe.MEGA_BOMB);
                break;
        }

        // Farming level
        switch (skills.getFarmingLevel()) {
            case 1 :
                learnRecipes(
                        List.of(FoodRecipe.FARMERS_LUNCH), List.of(
                                CraftingRecipe.SPRINKLER, CraftingRecipe.BEE_HOUSE
                        )
                );
                break;
            case 2:
                learnRecipes(
                        null, List.of (CraftingRecipe.QUALITY_SPRINKLER, CraftingRecipe.DELUXE_SCARECROW,
                                CraftingRecipe.CHEESE_PRESS, CraftingRecipe.PRESERVES_JAR)
                );
                break;
            case 3 :
                learnRecipes(
                        null, List.of (CraftingRecipe.IRIDIUM_SPRINKLER, CraftingRecipe.KEG ,
                                CraftingRecipe.LOOM, CraftingRecipe.OIL_MAKER)
                );
                break;
        }

        // Fishing
        switch (skills.getFishingLevel()) {
            case 2 :
                learnFoodRecipe(FoodRecipe.DISH_O_THE_SEA);
                break;
            case 3 :
                learnFoodRecipe(FoodRecipe.SEAFOAM_PUDDING);
        }
    }
}