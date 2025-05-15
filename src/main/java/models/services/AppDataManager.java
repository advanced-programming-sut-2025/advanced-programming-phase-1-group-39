package models.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.App;
import models.Enums.Menu;
import models.ItemManager;
import models.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class AppDataManager {
    private static final String APP_FILE_PATH = "src/main/data/app/appData.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveApp(App app) {
        try (FileWriter writer = new FileWriter(APP_FILE_PATH)) {
            gson.toJson(app, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadApp() {
        ItemManager.loadItems();
        File file = new File(APP_FILE_PATH);
        ItemManager.loadItems();
        if (!file.exists() || file.length() == 0) {
            App app = App.getApp();
            initializeDefaults(app);
            saveApp(app);
        } else {
            try (FileReader reader = new FileReader(file)) {
                App loadedApp = gson.fromJson(reader, App.class);
                App.setInstance(loadedApp);
                for (User user : App.getApp().getUsers()) {
                    user.ensureInitialized();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initializeDefaults(App app) {
        app.setLoggedInUser(null);
        app.setRandomPassword(null);
        app.setPendingUser(null);
        app.setRegisterSuccessful(false);
        app.setCurrentGame(null);
        app.setLastGameId(101);
        app.setCurrentMenu(Menu.SIGNUP_MENU);
        app.setUsers(new ArrayList<>());
        app.setGames(new ArrayList<>());
    }
}