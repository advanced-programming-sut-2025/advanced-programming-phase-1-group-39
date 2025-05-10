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

    // Todo: location and width and width in every shop is not true, must be initialized
    public Shop(String name, Location location, int width, int height, int openHour, int closeHour, NPC owner) {
        super(name, location, width, height);
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.owner = owner;
    }

    public void handleCommand(String command){}
    public String showAllProducts(){return null;}
    public String showAvailableProducts() {return null;}
    //public Result purchase(String product, int quantity){return null;}
    public void endDay() {};
}

