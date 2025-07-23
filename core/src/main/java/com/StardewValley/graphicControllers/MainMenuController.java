package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.graphicViews.LoginMenuScreen;
import com.StardewValley.graphicViews.MainMenuScreen;
import com.StardewValley.graphicViews.ProfileMenuScreen;
import com.StardewValley.models.App;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuController {
    private MainMenuScreen view;

    public void setMenuView(MainMenuScreen mainMenuScreen) {
        this.view = mainMenuScreen;
    }

    public void handleMainMenu() {
        if (view != null) {

            view.getGameMenuButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getGameMenuButton().isChecked()) {
                        Main.getMain().setScreen(new GameScreen());
                    }
                }
            });

            view.getProfileMenuButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getProfileMenuButton().isChecked()) {
                        Main.getMain().setScreen(new ProfileMenuScreen());
                    }
                }
            });

            view.getLogoutButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getLogoutButton().isChecked()) {
                        App.getApp().setLoggedInUser(null);
                        App.getApp().setStayLoggedIn(false);
                        Main.getMain().setScreen(new LoginMenuScreen());
                    }
                }
            });
        }
    }
}
