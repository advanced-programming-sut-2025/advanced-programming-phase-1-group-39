package com.StardewValley.models.services;


import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;

public class SaveAppManager {

    public static void saveApp() {
        App app = App.getApp();
        if (app.isStayLoggedIn()) {
            app.setRandomPassword(null);
            app.setRegisterSuccessful(false);
            app.setCurrentMenu(Menu.MAIN_MENU);
            AppDataManager.saveApp(app);
        } else {
            app.setRandomPassword(null);
            app.setRegisterSuccessful(false);
            app.setCurrentGame(null);
            app.setCurrentMenu(Menu.SIGNUP_MENU);
            AppDataManager.saveApp(app);
        }
    }

}
