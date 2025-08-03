package com.StardewValley;

import com.StardewValley.graphicViews.*;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Player;
import com.StardewValley.models.User;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.models.services.GameAssetManager;
import com.StardewValley.models.services.SaveAppManager;
import com.StardewValley.views.AppView;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    public static SpriteBatch batch;

    @Override
    public void create() {
        AppDataManager.loadApp();
        main = this;
        batch = new SpriteBatch();


        switchScreen(getScreenByMenu(App.getApp().getCurrentMenu()));

        Thread terminalController = new Thread(() -> {
            new AppView().run();
        });
        terminalController.setDaemon(true);
        terminalController.start();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        AppDataManager.saveApp();
        screen.dispose();
        batch.dispose();
    }

    public static Main getMain() {
        return main;
    }

    public void switchScreen(Screen screen) {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        setScreen(screen);
    }

    public Screen getScreenByMenu(Menu menu) {
        return switch (menu) {
            case SIGNUP_MENU -> new SignupMenuScreen();
            case LOGIN_MENU -> new LoginMenuScreen();
            case MAIN_MENU -> new MainMenuScreen();
            case GAME_MENU -> new GameScreen();
            default -> null;
        };
    }

    public static SpriteBatch getBatch() {
        return batch;
    }
}
