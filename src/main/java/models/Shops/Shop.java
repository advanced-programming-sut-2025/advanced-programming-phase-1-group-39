package models.Shops;

import models.ItemStack;
import models.NPC.NPC;
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

    void handleCommand(String command){}
    String showAllProducts(){return null;}
    String showAvailableProducts() {return null;}
    void purchase(String product, int quantity){}
    void addProduct(String product){}
}

