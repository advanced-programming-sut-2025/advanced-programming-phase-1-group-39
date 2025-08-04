package com.StardewValley.models.Shops;


import com.StardewValley.models.Location;
import com.StardewValley.models.NPC.NPC;
import com.StardewValley.models.Result;
import com.StardewValley.models.buildings.Building;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Shop extends Building {
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
    public void showShopMenu(Stage stage, Skin skin) {}

    public boolean isInWorkingHour(int hour) {
        return hour >= openHour && hour <= closeHour;
    }

    public int getOpenHour() {
        return openHour;
    }

    public int getCloseHour() {
        return closeHour;
    }
}

