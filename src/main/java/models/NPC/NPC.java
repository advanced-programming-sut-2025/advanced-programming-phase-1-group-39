package models.NPC;

import models.Item;
import models.ItemStack;
import models.Location;
import models.Result;

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

    public NPC(String name, String job, String personalityTraits, Location location, ArrayList<Item> favoriteItems) {
        this.name = name;
        this.job = job;
        this.personalityTraits = personalityTraits;
        this.location = location;
        this.favoriteItems = favoriteItems;
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


    // Auxiliary functions :

    protected ArrayList<Quest> generateNPCQuests(ArrayList<ItemStack> tasks, ArrayList<String> missions ) {
        ArrayList<Quest> quests = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Quest quest = new Quest(i+1, missions.get(i), tasks.get(i));
        }
        return quests;
    }


//    protected abstract
}







