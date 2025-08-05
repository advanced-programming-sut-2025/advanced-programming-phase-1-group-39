package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.controllers.AppControllers;
import com.StardewValley.graphicViews.GameScreen;
import com.StardewValley.graphicViews.MainMenuScreen;
import com.StardewValley.graphicViews.PregameMenuScreen;
import com.StardewValley.models.*;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.map.FarmType;

import java.util.ArrayList;
import java.util.List;

public class PregameGuiController {
    private PregameMenuScreen screen;

    private Game game;

    public void setScreen(PregameMenuScreen screen) {
        this.screen = screen;
    }

    ArrayList<User> gameUsers = new ArrayList<>();

    public Result checkStartGame(String user2, String user3, String user4) {
        User loggedInUser = App.getApp().getLoggedInUser();
        ArrayList<String> usernames = new ArrayList<>(List.of(loggedInUser.getUserName(), user2,user3,user4));
        gameUsers.clear();

        // Emptiness of fields
        for (String username : usernames) {
            if (username.isEmpty())
                return new Result(false, "fill all 3 other username fields");
        }
        // existence of users in App
        for (String username : usernames) {
            User user = App.getApp().getUserByUsername(username);
            if (user == null) {
                return new Result(false, "user '" + username + "' not found");
            }
            if (gameUsers.contains(user)) {
                return new Result(false, "duplicated user '" + username + "'");
            }

            gameUsers.add(user);
        }
        // having current game by users
        for (User user : gameUsers) {
            if (user.getCurrentGame() != null)
                return new Result(false, "user " + user.getUserName() + " already have a game");
        }
        // TODO : check users being online
        return new Result(true, "");
    }

    public void makeMapOfGame(int[] mapIds) {
        // start making a game for users
        int gameId = Game.lastGameId;
        ArrayList<Player> players = new ArrayList<>();
        for (User user : gameUsers) {
            players.add(new Player(user.getUserName(), user.getNickname(), gameId));
        }
        game = new Game(players);

        App.getApp().addGame(game);
        App.getApp().setCurrentGame(game);
        for (User user : gameUsers) {
            user.setCurrentGame(game);
        }


        // starting the game
        int c = 0;
        for (Player player : players) {
            game.addRandomFarmForPlayer(player, FarmType.getFarmTypeById(mapIds[c++]));
        }
        game.addNpcMap();
    }

    public void startGame() {
        game.startGame();
        App.getApp().setCurrentMenu(Menu.GAME);
        Main.getMain().switchScreen(new GameScreen());
    }

    public Result loadGame() {
        return AppControllers.gameMenuController.loadGame();
    }

    public void goMainMenu() {
        Main.getMain().switchScreen(new MainMenuScreen());
        App.getApp().setCurrentMenu(Menu.MAIN_MENU);
    }

    public String getUserNickName(int index) {
        User user = gameUsers.get(index);
        return user.getNickname();
    }

    public Game getGame() {
        return game;
    }
}
