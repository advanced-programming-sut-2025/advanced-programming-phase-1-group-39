import models.App;
import models.services.AppDataManager;
import models.services.SaveAppManager;
import models.services.UsersDataManager;
import views.AppView;

public class Main {

    public static void main(String[] args) {
        UsersDataManager.loadUsers();
        AppDataManager.loadApp();
        new AppView().run();
        SaveAppManager.saveApp();
        UsersDataManager.saveUsers(App.getApp().getUsers());
    }

}