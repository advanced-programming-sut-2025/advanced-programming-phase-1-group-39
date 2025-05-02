package views;

import models.App;
import models.Enums.Menu;
import models.Input;

import java.util.Scanner;

public class AppView {
    public void run() {
        do {
            App.getApp().getCurrentMenu().checkCommand(Input.getNextLine());
        } while (App.getApp().getCurrentMenu() != Menu.ExitMenu);
    }
}
