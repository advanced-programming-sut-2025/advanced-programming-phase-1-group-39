package models.services;

import models.App;
import models.Enums.Menu;

import java.util.ArrayList;

public class SaveAppManager {

    public static void saveApp() {
        App app = App.getApp();
        if (app.isStayLoggedIn()) {
            app.setRandomPassword(null);
            app.setPendingUser(null);
            app.setRegisterSuccessful(false);
            app.setCurrentMenu(Menu.MAIN_MENU);
            AppDataManager.saveApp(app);
        } else {
            app.setLoggedInUser(null);
            app.setRandomPassword(null);
            app.setPendingUser(null);
            app.setRegisterSuccessful(false);
            app.setCurrentGame(null);
            app.setCurrentPlayer(null);
            app.setCurrentMenu(Menu.SIGNUP_MENU);
            AppDataManager.saveApp(app);
        }
    }

}
