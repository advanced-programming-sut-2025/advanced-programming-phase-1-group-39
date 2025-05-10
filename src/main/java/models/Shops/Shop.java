package models.Shops;

import models.ItemStack;
import models.NPC.NPC;
import models.Result;
import models.buildings.Building;

import java.util.*;

public class Shop extends Building {
    private String name;
    private int openHour;
    private int closeHour;
    private NPC owner;

    public Shop(String name, int openHour, int closeHour, NPC owner) {
        super(name);
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

