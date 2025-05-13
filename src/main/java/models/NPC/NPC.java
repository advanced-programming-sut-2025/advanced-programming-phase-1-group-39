package models.NPC;

import models.Item;
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
    protected String filePath;
    protected HashMap<String, String> dialogues;

    public NPC(String name, String job, String personalityTraits, Location location, ArrayList<Item> favoriteItems,
               String filePath) {
        this.name = name;
        this.job = job;
        this.personalityTraits = personalityTraits;
        this.location = location;
        this.favoriteItems = favoriteItems;
        this.filePath = filePath;
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
}







