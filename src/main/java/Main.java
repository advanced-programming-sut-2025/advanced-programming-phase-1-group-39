import models.App;
import models.services.AppDataManager;
import models.services.SaveAppManager;
import models.services.UsersDataManager;
import views.AppView;

public class Main {

    public static void main(String[] args) {
        AppDataManager.loadApp();
        UsersDataManager.loadUsers();
        new AppView().run();
        UsersDataManager.saveUsers(App.getApp().getUsers());
        SaveAppManager.saveApp();

    }

}