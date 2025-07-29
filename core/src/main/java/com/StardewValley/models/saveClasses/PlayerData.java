package com.StardewValley.models.saveClasses;

import com.StardewValley.models.Location;
import com.StardewValley.models.NPC.PlayerNPCInteraction;
import com.StardewValley.models.NPC.Quest;
import com.StardewValley.models.Player;
import com.StardewValley.models.Skill;
import com.StardewValley.models.animals.Animal;
import com.StardewValley.models.buildings.Building;
import com.StardewValley.models.crafting.CraftingRecipe;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerData {
    private int id;
    private int gameId;
    private Location location = new Location(0,0);
    private Location startOfFarm;
    private Location endOfFarm;

    private String username;
    private String nickname;

    private int numOfBadDays = 0;
    private Skill skills = new Skill();

    private ArrayList<String> craftingRecipesNames;
    private ArrayList<String> artisanMachineRecipesNames;
    private ArrayList<String> foodRecipesNames;

    private HashMap<String, Integer> NPCNamesFriendship = new HashMap<>();
    private ArrayList<QuestData> activeQuests = new ArrayList<>();

    private HashMap<String, Animal> animals = new HashMap<>();

    private int money;
    private int nightRevenue;

    private ArrayList<Building> playerFarmBuildings = new ArrayList<>();

    // NPC
    private ArrayList<PlayerNPCInteraction> friendships;

    private String spouseName;

    public PlayerData(Player player) {
    }
}
