package com.StardewValley.views;


import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Input;

public class AppView {
    public void run() {
        do {
            App.getApp().getCurrentMenu().checkInput(Input.getNextLine());
        } while (!App.getApp().getCurrentMenu().equals(Menu.ExitMenu));
    }
}
