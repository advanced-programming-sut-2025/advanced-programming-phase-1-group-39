package views;

import models.App;
import services.UsersDataManager;

public class ExitMenuView implements View {
    @Override
    public void checkCommand(String command) {
        UsersDataManager.saveUsers(App.getApp().getUsers());
    }
}
