package models.Shops;

import models.ItemStack;
import models.Location;
import models.NPC.NPC;
import models.Result;
import models.buildings.Building;

import java.util.*;

public class Shop extends Building {
    private String name;
    private int openHour;
    private int closeHour;
    private NPC owner;

    public Shop(String name, Location location, int width, int height, int openHour, int closeHour, NPC owner) {
        super(name, location, width, height);
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.owner = owner;
    }

    public Result purchase(String productName, int amount) {return null;}
    public String showAllProducts(){return null;}
    public String showAvailableProducts() {return null;}
    public void endDay() {};
}

