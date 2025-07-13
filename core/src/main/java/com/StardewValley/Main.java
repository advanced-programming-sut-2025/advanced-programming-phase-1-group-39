package com.StardewValley;

import models.services.AppDataManager;
import models.services.SaveAppManager;
import views.AppView;

// TODO : add Main context to the Graphic AppView
public class Main {
    public static void main(String[] args) {
        AppDataManager.loadApp();
        new AppView().run();
        SaveAppManager.saveApp();
    }
}