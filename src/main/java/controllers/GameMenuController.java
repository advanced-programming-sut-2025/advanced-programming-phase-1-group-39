package controllers;

import models.*;
import models.Enums.Menu;
import models.cropsAndFarming.CropManager;
import models.map.FarmType;
import models.map.Map;

import java.util.regex.Matcher;

public class GameMenuController {

    public Result startNewGame(Matcher matcher) {
        String username1 = matcher.group("username1");
        String username2 = matcher.group("username2");
        String username3 = matcher.group("username3");
        String otherUsers = matcher.group("otherUsers");

        if (username1 == null || username2 == null || username3 == null) {
            return new Result(false, "Invalid username or password");
        } else if (!isUsernameExist(username1)) {
            return new Result(false, "username1 not found in the system.");
        } else if (!isUsernameExist(username2)) {
            return new Result(false, "username2 not found in the system.");
        } else if (!isUsernameExist(username3)) {
            return new Result(false, "username3 not found in the system.");
        } else if (otherUsers != null) {
            return new Result(false, "You can only select three players to start the game.");
        } else if (hasCurrentGame(username1)) {
            return new Result(false, "username1 already has a current game.");
        } else if (hasCurrentGame(username2)) {
            return new Result(false, "username2 already has a current game.");
        } else if (hasCurrentGame(username3)) {
            return new Result(false, "username3 already has a current game.");
        } else {
            Player player1 = new Player(App.getApp().getLoggedInUser().getUserName(), 1);
            Player player2 = new Player(username1, 2);
            Player player3 = new Player(username2, 3);
            Player player4 = new Player(username3, 4);
            Game newGame = new Game(App.getApp().getCurrentGameId()+1,player1, player2, player3, player4);
            App.getApp().setCurrentGameId();
            App.getApp().setCurrentGame(newGame);
            App.getApp().addGame(newGame);
            App.getApp().setCurrentPlayer(player1);
            return new Result(true, "Congratulations!!!" + "\n" +
                    " The new game has been successfully created. " + "\n" +
                    "Now, each player must choose their game map in order, starting with the first player. \n" +
                    "The available map types are as follows: \n" + Map.showFarmTypesInfo());
        }
    }

    public Result choseMap(String mapNumber) {

        if (!isInteger(mapNumber)) {
            return new Result(false, "The map number must be a number.");
        } else if (Integer.parseInt(mapNumber) != 1 || mapNumber.length() != 2) {
            return new Result(false, "The map number must be either 1 or 2.");
        } else if (App.getApp().getCurrentPlayer().getId() == 4) {
            App.getApp().getCurrentGame().addRandomFarmForPlayer(App.getApp().getCurrentPlayer(),
                    FarmType.getFarmTypeById(Integer.parseInt(mapNumber)));
            App.getApp().getCurrentGame().startGame();
            App.getApp().setCurrentMenu(Menu.GAME_MENU);
            App.getApp().getGames().set(getIndexInMaps(App.getApp().getCurrentGame().getId()), App.getApp().getCurrentGame());
            return new Result(true, "The game map has been successfully created — let the adventure begin!");
        } else {
            App.getApp().getCurrentGame().addRandomFarmForPlayer(App.getApp().getCurrentPlayer(),
                    FarmType.getFarmTypeById(Integer.parseInt(mapNumber)));
            App.getApp().setCurrentPlayer(App.getApp().getCurrentGame().getPlayers().get(App.getApp().getCurrentPlayer()
                    .getId()));
            return new Result(true, "the game map was successfully selected. please let the next player choose their map.");
        }
    }

    public Result loadGame() {

        if (App.getApp().getLoggedInUser().getCurrentGame() == null) {
            return new Result(false, "You have not any game. please create a game first.");
        } else {

        }
    }





    public static Result saveGame() {return null;}
    public static Result exitGame() {return null;}
    public static Result deleteGame() {return null;}

    // Auxiliary functions :

    private boolean isUsernameExist(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCurrentGame(String username) {
        for (User user : App.getApp().getUsers()) {
            if (user.getUserName().equals(username)) {
                return user.getCurrentGame() != null;
            }
        }
        return false;
    }

    private boolean isInteger(String input) {
        if (input == null) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getIndexInMaps (int mapId) {
        for (int i = 0; i < App.getApp().getGames().size(); i++) {
            if (App.getApp().getGames().get(i).getId() == mapId) {
                return i;
            }
        }
        return -1;
    }

    private Player getPlayerFromPlayers()
}
