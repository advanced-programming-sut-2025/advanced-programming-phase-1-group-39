package com.StardewValley.models;


import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.models.Enums.Direction;
import com.StardewValley.models.Enums.Season;
import com.StardewValley.models.NPC.PlayerNPCInteraction;
import com.StardewValley.models.animals.*;
import com.StardewValley.models.buildings.*;
import com.StardewValley.models.cooking.FoodBuff;
import com.StardewValley.models.cooking.FoodRecipe;
import com.StardewValley.models.crafting.CraftingRecipe;
import com.StardewValley.models.inventory.Inventory;
import com.StardewValley.models.map.AnsiColors;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.map.Map;
import com.StardewValley.models.tools.*;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private int gameId;

    private FarmType farmType;

    // Based on Tiles
    private Location startOfFarm;
    private Location endOfFarm;

    // graphic - based on pixels
    private float x, y;
    private boolean isMoving;

    private String username;
    private String nickname;

    private transient FoodBuff buff = null;

    private double energy = Constants.MAX_ENERGY;
    private double turnEnergy = Constants.MAX_ENERGY_PER_TURN;
    private boolean energyCheated = false;
    private boolean energyUnlimited = false;

    private int numOfBadDays = 0;

    private Skill skills = new Skill();
    private Inventory inventory;


    private ArrayList<String> learnedCraftingRecipeNames = new ArrayList<>();
    private ArrayList<String> learnedFoodRecipeNames = new ArrayList<>();

    // animals
    private HashMap<String, Animal> animals = new HashMap<>();

    private int money = 5000;
    private int nightRevenue = 0;

    private ArrayList<Building> playerFarmBuildings = new ArrayList<>();

    // NPC
    private ArrayList<PlayerNPCInteraction> friendships = initialPlayersFriendship();
    private String spouseName;
    private ArrayList<String> abigalReceivedQuests = new ArrayList<>();
    private ArrayList<String> harveyReceivedQuests = new ArrayList<>();
    private ArrayList<String> leahReceivedQuests = new ArrayList<>();
    private ArrayList<String> robinReceivedQuests = new ArrayList<>();
    private ArrayList<String> sebastianReceivedQuests = new ArrayList<>();
    private ArrayList<String> abigailDoQuests = new ArrayList<>();
    private ArrayList<String> harveyDoQuests = new ArrayList<>();
    private ArrayList<String> leahDoQuests = new ArrayList<>();
    private ArrayList<String> robinDoQuests = new ArrayList<>();
    private ArrayList<String> sebastianDoQuests = new ArrayList<>();

    // Graphic
    private transient Direction direction = Direction.NONE;
    private transient Color color;

    private transient GameScreen.PlayerState currentState = GameScreen.PlayerState.WalkingOrIdle;
    private transient float animationStateTime = 0f;


    // Inventory
    private final Integer maxInventorySize = 10;
    private int selectedSlot = -1;


    public Player(String username, String nickname, int gameId) {
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
        this.nickname = nickname;

        this.gameId = gameId;
    }

    public Player() {
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
        return Math.floor(energy * 100) / 100;
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
            if (turnEnergy <= 0) {
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

    public boolean isEnergyUnlimited() {
        return energyUnlimited;
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

    public boolean isBuildGreenhouse() {
        return ((GreenHouse) getBuildingByName("greenhouse")).isBuild();
    }

    public boolean canBuildGreenHouse() {
        return inventory.hasEnoughStack("Wood", 500) && money >= 1000;
    }

    public void buildGreenHouse() {
        inventory.pickItem("Wood", 500);
        changeMoney(-1000);
        for (Building building : playerFarmBuildings) {
            if (building.getName().equals("greenhouse")) ((GreenHouse) building).build();
        }
    }

    public void addFirstBuildingObjects(Game game) {
        Building cabin = new Cabin(new Location(startOfFarm.x() + 70, startOfFarm.y() + 5));
        Building greenhouse = new GreenHouse(new Location(startOfFarm.x() + 25, startOfFarm.y() + 0));
        Building shippingBin = new ShippingBin("Shipping Bin", new Location(startOfFarm.x() + 77, startOfFarm.y() + 10), 2, 1);

        addToBuildings(cabin);
        addToBuildings(greenhouse);
        addToBuildings(shippingBin);

        for (Building building : playerFarmBuildings) {
            building.updateMap(game.getMap());
            building.updateMap(game.getBaseMap());
        }
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
        learnedCraftingRecipeNames.add(recipe.getName());
    }

    public boolean hasLearnedCraftingRecipe(CraftingRecipe recipe) {
        return learnedCraftingRecipeNames.contains(recipe.getName());
    }

    public String showCraftingRecipes() {
        StringBuilder sb = new StringBuilder();
        for (String recipeName : learnedCraftingRecipeNames) {
            CraftingRecipe recipe = CraftingRecipe.getRecipeByName(recipeName);
            sb.append(recipe);
        }
        return sb.toString();
    }

    public void learnFoodRecipe(FoodRecipe recipe) {
        if (hasLearnedFoodRecipe(recipe)) return;
        learnedFoodRecipeNames.add(recipe.name());
    }

    public boolean hasLearnedFoodRecipe(FoodRecipe recipe) {
        return learnedFoodRecipeNames.contains(recipe.name());
    }

    public String showFoodRecipes() {
        StringBuilder sb = new StringBuilder();
        for (String recipeName : learnedFoodRecipeNames) {
            FoodRecipe recipe = FoodRecipe.getRecipeByName(recipeName);
            sb.append(recipe);
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


    // Animals
    public void addAnimal(Animal animal) {
        animals.put(animal.getName(), animal);
    }

    public AnimalBuilding getAnimalLivingPlaceBuilding(Animal animal) {
        AnimalBuilding animalBuilding;
        for (Building building : this.playerFarmBuildings) {
            if (building instanceof AnimalBuilding) {
                if (((AnimalBuilding) building).hasAnimal(animal)) {
                    animalBuilding = (AnimalBuilding) building;
                    return animalBuilding;
                }
            }
        }
        return null;
    }

    public int sellAnimal(Animal animal) {
        int money = (int) (animal.getPrice() * (((double) animal.getFriendship() / 1000) + 0.3));
        changeMoney(money);
        animals.remove(animal.getName());

        AnimalBuilding animalBuilding = getAnimalLivingPlaceBuilding(animal);
        if (animalBuilding != null) {
            animalBuilding.removeAnimal(animal);
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
        int dx = this.getTileLocation().x() - location.x();
        int dy = this.getTileLocation().y() - location.y();

        return dx <= 1 && dy <= 1 && dx >= -1 && dy >= -1;
    }


    public boolean isNear(Location location1, Location location2) {
        int dx = location1.x() - location2.x();
        int dy = location1.y() - location2.y();

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

    public String getNickname() {
        return nickname;
    }

    // marriage
    public String getSpouseUsername() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
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
            case 0:
                learnRecipes(
                        List.of(FoodRecipe.FRIED_EGG, FoodRecipe.BAKED_FISH, FoodRecipe.SALAD),
                        null
                );
                break;
            case 1:
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
            case 1:
                learnFoodRecipe(FoodRecipe.MINERS_TREAT);
                learnCraftingRecipe(CraftingRecipe.CHERRY_BOMB);
                break;
            case 2:
                learnCraftingRecipe(CraftingRecipe.BOMB);
                break;
            case 3:
                learnCraftingRecipe(CraftingRecipe.MEGA_BOMB);
                break;
        }

        // Farming level
        switch (skills.getFarmingLevel()) {
            case 1:
                learnRecipes(
                        List.of(FoodRecipe.FARMERS_LUNCH), List.of(
                                CraftingRecipe.SPRINKLER, CraftingRecipe.BEE_HOUSE
                        )
                );
                break;
            case 2:
                learnRecipes(
                        null, List.of(CraftingRecipe.QUALITY_SPRINKLER, CraftingRecipe.DELUXE_SCARECROW,
                                CraftingRecipe.CHEESE_PRESS, CraftingRecipe.PRESERVES_JAR)
                );
                break;
            case 3:
                learnRecipes(
                        null, List.of(CraftingRecipe.IRIDIUM_SPRINKLER, CraftingRecipe.KEG,
                                CraftingRecipe.LOOM, CraftingRecipe.OIL_MAKER)
                );
                break;
        }

        // Fishing
        switch (skills.getFishingLevel()) {
            case 2:
                learnFoodRecipe(FoodRecipe.DISH_O_THE_SEA);
                break;
            case 3:
                learnFoodRecipe(FoodRecipe.SEAFOAM_PUDDING);
        }
    }

    public ArrayList<Building> getFarmBuildings() {
        return playerFarmBuildings;
    }

    public void setFarmType(FarmType farmType) {
        this.farmType = farmType;
    }

    public FarmType getFarmType() {
        return farmType;
    }

    // Graphic
    // For Map - pixel
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setLocationAbsolut(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // For tiles array
    public Location getTileLocation() {
        int tileX = (int) (this.x / Map.TILE_SIZE);
        int tileY = Constants.WORLD_MAP_HEIGHT - 1 - (int) (this.y / Map.TILE_SIZE);
        return new Location(tileX, tileY);
    }

    public void setLocationInTilesAbsolut(float x, float y) {
        this.x = (x) * Map.TILE_SIZE;
        this.y = (Constants.WORLD_MAP_HEIGHT - y - 1) * Map.TILE_SIZE;
    }

    public void setLocationInTilesRelative(int x, int y) {
        this.x = (startOfFarm.x() + x) * Map.TILE_SIZE;
        this.y = (Constants.WORLD_MAP_HEIGHT - (startOfFarm.y() + y) - 1) * Map.TILE_SIZE;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    // Inventory
    public Integer getMaxInventorySize() {
        return maxInventorySize;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
        selectedSlot = Math.min(selectedSlot, inventory.getInventoryItems().size() - 1);
        selectedSlot = Math.max(selectedSlot, 0);
        this.inventory.setInHand(inventory.getInventoryItems().get(selectedSlot));
    }

    // state of player
    public boolean isMoving() { return isMoving; }

    public void setMoving(boolean moving) { this.isMoving = moving; }

    public GameScreen.PlayerState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameScreen.PlayerState state) {
        this.currentState = state;
    }


    public float getAnimationStateTime() {
        return animationStateTime;
    }

    public void updateAnimationStateTime(float delta) {
        this.animationStateTime += delta;
    }

    public ArrayList<String> getAbigalReceivedQuests() {
        return abigalReceivedQuests;
    }

    public ArrayList<String> getHarveyReceivedQuests() {
        return harveyReceivedQuests;
    }

    public ArrayList<String> getLeahReceivedQuests() {
        return leahReceivedQuests;
    }

    public ArrayList<String> getRobinReceivedQuests() {
        return robinReceivedQuests;
    }

    public ArrayList<String> getSebastianReceivedQuests() {
        return sebastianReceivedQuests;
    }

    public ArrayList<String> getAbigailDoQuests() {
        return abigailDoQuests;
    }

    public ArrayList<String> getHarveyDoQuests() {
        return harveyDoQuests;
    }

    public ArrayList<String> getRobinDoQuests() {
        return robinDoQuests;
    }

    public ArrayList<String> getSebastianDoQuests() {
        return sebastianDoQuests;
    }

    public ArrayList<String> getLeahDoQuests() {
        return leahDoQuests;
    }
}