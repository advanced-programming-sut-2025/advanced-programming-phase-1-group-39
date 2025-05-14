package models.NPC;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class NPC {

    protected String name;
    protected  String job;
    protected String personalityTraits;
    protected Location location;
    protected ArrayList<Item> favoriteItems;
    protected HashMap<String, String> dialogues;
    protected ArrayList<Quest> quests;
    protected ArrayList<ItemStack> tasks;
    protected ArrayList<String> missions;
    protected ArrayList<ItemStack> gifts;


    public NPC(String name, String job, String personalityTraits, Location location, ArrayList<Item> favoriteItems) {
        this.name = name;
        this.job = job;
        this.personalityTraits = personalityTraits;
        this.location = location;
        this.favoriteItems = favoriteItems;
        gifts = new ArrayList<>(List.of(new ItemStack(ItemManager.getItemByName("Iridium Bar"), 2),
                                        new ItemStack(ItemManager.getItemByName("Iron Bar"), 4),
                                        new ItemStack(ItemManager.getItemByName("Gold Bar"), 3),
                                        new ItemStack(ItemManager.getItemByName("Copper Bar"), 5)));
    }


    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Item> getFavoriteItems() {
        return favoriteItems;
    }

    public HashMap<String, String> getDialogues() {
        return dialogues;
    }

    public ArrayList<String> getMissions() {
        return missions;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    // Auxiliary functions :

    protected ArrayList<Quest> generateNPCQuests(ArrayList<ItemStack> tasks, ArrayList<String> missions ) {
        ArrayList<Quest> quests = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Quest quest = new Quest(i+1, tasks.get(i));
        }
        return quests;
    }

    public Quest getQuest(int level) {
        for (Quest quest : quests) {
            if (quest.getLevel() == level) {
                return quest;
            }
        }
        return null;
    }

    public ArrayList<ItemStack> getGifts() {
        return gifts;
    }

    public abstract void getRewardMission1(int friendShipLevel, Game game);
    public abstract void getRewardMission2(int friendShipLevel, Game game);
    public abstract void getRewardMission3(int friendShipLevel, Game game);


//    protected abstract
}







