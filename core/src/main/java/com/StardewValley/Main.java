package com.StardewValley;

import com.StardewValley.graphicControllers.SignupMenuController;
import com.StardewValley.graphicViews.GameView;
import com.StardewValley.graphicViews.SignupMenuView;
import com.StardewValley.models.App;
import com.StardewValley.models.Player;
import com.StardewValley.models.map.FarmType;
import com.StardewValley.models.services.AppDataManager;
import com.StardewValley.models.services.SaveAppManager;
import com.StardewValley.views.AppView;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private static SpriteBatch batch;

    @Override
    public void create() {
        AppDataManager.loadApp();
        main = this;
        batch = new SpriteBatch();

        /// /////// test //////////
//        App app = App.getApp();
//        Player player1 = new Player("mmd1", 101);
//        Player player2 = new Player("mmd2", 102);
//        Player player3 = new Player("mmd3", 103);
//        Player player4 = new Player("mmd4", 104);
//        com.StardewValley.models.Game newGame = new com.StardewValley.models.Game(app.getLastGameId() + 1, player1, player2, player3, player4);
//        app.setLastGameId(app.getLastGameId() + 1);
//        app.setCurrentGame(newGame);
//        app.addGame(newGame);
//        app.getCurrentGame().setPlayerInTurn(player1);
//
//        com.StardewValley.models.Game currentGame = app.getCurrentGame();
//        currentGame.addRandomFarmForPlayer(player1, FarmType.getFarmTypeById(0));
//        currentGame.addRandomFarmForPlayer(player2, FarmType.getFarmTypeById(1));
//        currentGame.addRandomFarmForPlayer(player3, FarmType.getFarmTypeById(0));
//        currentGame.addRandomFarmForPlayer(player4, FarmType.getFarmTypeById(1));
//        currentGame.startGame();

        /// /////test//////////////

//        switchScreen(new GameView());
//
//        Thread terminalController = new Thread(() -> {
//            new AppView().run();
//        });
//        terminalController.start();

        /// /////test//////////////
        Main.getMain().setScreen(new SignupMenuView());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        SaveAppManager.saveApp();
        batch.dispose();
    }

    public static Main getMain() {
        return main;
    }

    public static SpriteBatch getBatch() { return batch; }

    public void switchScreen(Screen screen) {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        setScreen(screen);
    }
}
