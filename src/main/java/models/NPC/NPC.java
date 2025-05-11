package models.NPC;

import models.Item;
import models.Location;
import models.Result;

import java.util.ArrayList;
import java.util.List;

public abstract class NPC {

    private String name;
    private  String job;
    private String personalityTraits;
    private Location location;
    private ArrayList<Item> favoriteItems;

    public NPC(String name, String job, String personalityTraits, Location location, ArrayList<Item> favoriteItems) {
        this.name = name;
        this.job = job;
        this.personalityTraits = personalityTraits;
        this.location = location;
        this.favoriteItems = favoriteItems;

    }

    //public Result getRewardLevel1{}




}







