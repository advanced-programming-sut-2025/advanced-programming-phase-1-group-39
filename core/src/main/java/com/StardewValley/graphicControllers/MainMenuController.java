package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.graphicViews.LoginMenuScreen;
import com.StardewValley.graphicViews.MainMenuScreen;
import com.StardewValley.graphicViews.ProfileMenuScreen;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuController {
    private MainMenuScreen view;

    public void setMenuView(MainMenuScreen mainMenuView) {
        this.view = mainMenuView;
    }

    public void handleMainMenu() {
        if (view != null) {

            view.getGameMenuButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setCurrentMenu(Menu.GAME_MENU);
                    Main.getMain().switchScreen(new GameScreen());
                }
            });

            view.getProfileMenuButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setCurrentMenu(Menu.PROFILE_MENU);
                    Main.getMain().switchScreen(new ProfileMenuScreen());
                }
            });

            view.getLogoutButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setLoggedInUser(null);
                    App.getApp().setStayLoggedIn(false);
                    App.getApp().setCurrentMenu(Menu.LOGIN_MENU);
                    Main.getMain().switchScreen(new LoginMenuScreen());

                }
            });
        }
    }
}
