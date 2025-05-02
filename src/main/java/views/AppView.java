package views;

import models.App;
import models.Enums.Menu;
import models.Input;

public class AppView {
    public void run() {
        do {
            App.getApp().getCurrentMenu().checkInput(Input.getNextLine());
        } while (!App.getApp().getCurrentMenu().equals(Menu.ExitMenu));
    }
}
