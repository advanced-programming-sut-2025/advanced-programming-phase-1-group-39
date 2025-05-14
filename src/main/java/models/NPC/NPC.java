package models.NPC;

import models.Enums.Season;
import models.Item;
import models.Location;
import models.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NPC {
    private String name;
    private String job;
    private Location homeLocation;

    public NPC(String name) {
        this.name = name;
    }

    private ArrayList<String> dialogues;
    private HashMap<Weather, ArrayList<String>> weatherDialogues;
    private HashMap<Season, ArrayList<String>> seasonalDialogues;

    private Set<Item> favoriteItems;
    private int friendshipPoints;
    private int friendshipLevel;

    private ArrayList<Quest> quests;

    public String getName() {
        return name;
    }
}

