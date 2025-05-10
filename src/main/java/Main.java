import models.App;
import models.services.AppDataManager;
import views.AppView;

public class Main {

    public static void main(String[] args) {
        AppDataManager.loadApp();
        new AppView().run();
    }
}