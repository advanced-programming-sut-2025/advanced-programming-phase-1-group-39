package models.NPC;

import models.Item;
import models.Location;

import java.util.ArrayList;
import java.util.List;

public class NPC {

    private String name;
    private String job;
    private String personalityTraits;
    private Location location;
    private List<Item> favoriteItems;
    private List<Quest> quests = new ArrayList<>();

    // سازنده و متدهای مورد نیاز

    public NPC(String name, String job, String personality, Location location) {
        this.name = name;
        this.job = job;
        this.personalityTraits = personality;
        this.location = location;
        this.favoriteItems = new ArrayList<>();
        this.quests = new ArrayList<>();
    }

    public void addFavoriteItem(Item item) {
        this.favoriteItems.add(item);
    }

    public void addQuest(Quest quest) {
        this.quests.add(quest);
    }

    // متدهای getter و setter



}






//    private String name;
//    private String job;
//    private Location homeLocation;
//
//    private ArrayList<String> dialogues;
//    private HashMap<Weather, ArrayList<String>> weatherDialogues;
//    private HashMap<Season, ArrayList<String>> seasonalDialogues;
//
//    private Set<Item> favoriteItems;
//    private int friendshipPoints;
//    private int friendshipLevel;
//
//    private ArrayList<Quest> quests;


