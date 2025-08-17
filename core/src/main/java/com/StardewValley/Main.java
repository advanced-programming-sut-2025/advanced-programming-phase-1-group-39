package com.StardewValley;

import com.StardewValley.graphicViews.*;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.GameSetting;
import com.StardewValley.models.Result;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.network.client.NetworkClient;
import com.StardewValley.network.shares.Lobby;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    public static SpriteBatch batch;

    private NetworkClient networkClient = null;

    @Override
    public void create() {
        AppDataManager.loadApp();
        main = this;
        batch = new SpriteBatch();

        switchScreen(getScreenByMenu(App.getApp().getCurrentMenu()));

///  for test only
//        Thread terminalController = new Thread(() -> {
//            new AppView().run();
//        });
//        terminalController.setDaemon(true);
//        terminalController.start();
    }

    public void goOnline() {
        networkClient = new NetworkClient();
        networkClient.connect("127.0.0.1", 5050);

        switchScreen(new LobbyScreen(networkClient));
    }


    // --- Events ---
    public void onConnectionSuccess() {
        networkClient.sendUserDataToServer(App.getApp().getLoggedInUser());
        switchScreen(new LobbyScreen(networkClient));
    }

    public void onConnectionFailed(String reason) {
        System.out.println("Connection Failed: " + reason);
        switchScreen(new MainMenuScreen());
    }

    public void onDisconnectedFromServer(String reason) {
        System.out.println("Disconnected: " + reason);
        if (networkClient != null) {
            networkClient = null;
        }
        App.getApp().setCurrentLobby(null);
        App.getApp().setCurrentGame(null);

        switchScreen(new MainMenuScreen());
    }

    public void onJoinLobbyResponse(Result result) {
        Screen currentScreen = getScreen();
        if (currentScreen instanceof LobbyScreen) {
            ((LobbyScreen) currentScreen).handleJoinResponse(result);
        }
    }

    public void onLeftLobby(String message) {
        Screen currentScreen = getScreen();
        if (currentScreen instanceof LobbyScreen) {
            ((LobbyScreen) currentScreen).closeJoinedLobbyWindow();
        }
    }

    public void onUpdateLobby(Lobby lobby) {
        Screen currentScreen = getScreen();
        if (currentScreen instanceof LobbyScreen) {
            ((LobbyScreen) currentScreen).updateJoinedLobbyWindow(lobby);
        }
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
