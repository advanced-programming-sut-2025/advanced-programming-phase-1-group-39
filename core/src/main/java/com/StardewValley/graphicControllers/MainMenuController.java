package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.GameView;
import com.StardewValley.graphicViews.LoginMenuView;
import com.StardewValley.graphicViews.MainMenuView;
import com.StardewValley.graphicViews.ProfileMenuView;
import com.StardewValley.models.App;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuController {
    private MainMenuView view;

    public void setMenuView(MainMenuView mainMenuView) {
        this.view = mainMenuView;
    }

    public void handleMainMenu() {
        if (view != null) {

            view.getGameMenuButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getGameMenuButton().isChecked()) {
                        Main.getMain().setScreen(new GameView());
                    }
                }
            });

            view.getProfileMenuButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getProfileMenuButton().isChecked()) {
                        Main.getMain().setScreen(new ProfileMenuView());
                    }
                }
            });

            view.getLogoutButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getLogoutButton().isChecked()) {
                        App.getApp().setLoggedInUser(null);
                        App.getApp().setStayLoggedIn(false);
                        Main.getMain().setScreen(new LoginMenuView());
                    }
                }
            });
        }
    }
}
