package com.StardewValley.models.services;


import com.StardewValley.models.App;
import com.StardewValley.models.Constants;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.User;

import java.io.FileWriter;
import java.util.ArrayList;

public class SaveAppManager {

    public static void saveApp() {
        AppDataManager.saveApp();
//        if (app.isStayLoggedIn()) {
//            app.setRandomPassword(null);
//            app.setPendingUser(null);
//            app.setRegisterSuccessful(false);
//            app.setCurrentMenu(Menu.MAIN_MENU);
//            AppDataManager.saveApp(app);
//        } else {
//            app.setLoggedInUser(null);
//            app.setRandomPassword(null);
//            app.setPendingUser(null);
//            app.setRegisterSuccessful(false);
//            app.setCurrentGame(null);
//            app.setCurrentMenu(Menu.SIGNUP_MENU);
//            AppDataManager.saveApp(app);
//        }
    }
}
