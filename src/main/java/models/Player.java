package models;

import models.NPC.NPC;
import models.NPC.Quest;
import models.artisan.ArtisanMachineRecipe;
import models.cooking.FoodRecipe;
import models.crafting.CraftingRecipe;
import models.inventory.Inventory;
import models.trading.TradeItem;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    Location location;
    int energy;
    Skill skills;
    Inventory inventory;


    ArrayList<CraftingRecipe> craftingRecipes;
    ArrayList<ArtisanMachineRecipe> artisanMachineRecipes;
    ArrayList<FoodRecipe> foodRecipes;

    HashMap<Player, Integer> playersFriendship;
    HashMap<NPC, Integer> NPCsFriendship;

    ArrayList<Quest> activeQuests;

    public boolean isConscious() {
        return energy > 0;
    }

    public void setEnergy(int energy) {}
    public void getFish() {}

    public int getLevelOfFriendship(NPC npc) {return 0;}
    public void startTrade(Player player, TradeItem item) {}
}
