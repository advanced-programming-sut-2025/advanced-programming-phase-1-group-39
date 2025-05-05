package views;

import models.App;
import models.Enums.Menu;
import models.services.AppDataManager;
import models.services.UsersDataManager;

import java.util.ArrayList;

public class ExitMenuView implements View {
    @Override
    public void checkCommand(String command) {
        App app = App.getApp();
        if (app.getLoggedInUser().getIsStayLoggedIn()) {
            app.setRandomPassword(null);
            app.setPendingUser(null);
            app.setRegisterSuccessful(false);
            AppDataManager.saveApp(app);
        } else {
            app.setLoggedInUser(null);
            app.setRandomPassword(null);
            app.setPendingUser(null);
            app.setRegisterSuccessful(false);
            app.setCurrentGame(null);
            app.setCurrentPlayer(null);
            app.setCurrentMenu(Menu.SIGNUP_MENU);
            app.setUsers(new ArrayList<>());
            app.setGames(new ArrayList<>());
            AppDataManager.saveApp(app);
        }

    }
}
