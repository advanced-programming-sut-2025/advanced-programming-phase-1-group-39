package controllers;

import models.App;
import models.Item;
import models.User;
import models.cropsAndFarming.CropManager;
import models.map.Map;
import models.Result;

import java.util.regex.Matcher;

public class GameMenuController {

    public Result startNewGame(Matcher matcher) {
        String username1 = matcher.group("username1");
        String username2 = matcher.group("username2");
        String username3 = matcher.group("username3");
        String otherUsers = matcher.group("otherUsers");

        if (!isUsernameExist(username1)) {
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

        }


    }




    private static Map createRandomMap() {return null;}
    public static Result chooseMap() {return null;}
    public static Result loadGame() {return null;}
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
}
