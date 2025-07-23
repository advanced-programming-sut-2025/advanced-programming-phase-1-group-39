package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.MainMenuScreen;
import com.StardewValley.graphicViews.PregameMenuScreen;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.Result;
import com.StardewValley.models.User;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

public class PregameGuiController {
    private PregameMenuScreen screen;

    public void setScreen(PregameMenuScreen screen) {
        this.screen = screen;
    }


    public Result checkStartGame(String user2, String user3, String user4) {
        ArrayList<String> usernames = new ArrayList<>(List.of(user2,user3,user4));
        ArrayList<User> gameUsers = new ArrayList<>();
        gameUsers.add(App.getApp().getLoggedInUser());

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
        // TODO : check users playing game
        return new Result(true, "");
    }

    public Result loadGame() {
        return null;
    }

    public void goMainMenu() {
        Main.getMain().switchScreen(new MainMenuScreen());
        App.getApp().setCurrentMenu(Menu.MAIN_MENU);
    }
}
